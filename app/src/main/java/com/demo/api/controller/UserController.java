package com.demo.api.controller;

import com.demo.api.controller.request.UserRequest;
import com.demo.api.dto.UserDTO;
import com.demo.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api/users")
@Api(tags = "Simple User CRUD API")
public class UserController {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    public UserController(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @ApiOperation(value = "Returns a page of users based on the page and pageSize")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> allUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(this.userService.getAllUsers(page, pageSize));
    }

    @ApiOperation(value = "Returns a list of users based on filters passed as query params")
    @GetMapping("/filter")
    public ResponseEntity<List<UserDTO>> listUsersByFilter(@RequestParam(required = false) Optional<String> cpf, @RequestParam(required = false) Optional<String> nome ){
        if (cpf.isPresent()) {
            return this.userService.getByCpf(cpf.get())
                                   .map(userDTO -> ResponseEntity.ok().body(List.of(userDTO)))
                                   .orElseGet( () -> ResponseEntity.notFound().build());
        }
        if (nome.isPresent()) {
            return ResponseEntity.ok().body(this.userService.searchByNameLike(nome.get()));
        }

        return ResponseEntity.badRequest().body(null);
    }

    @ApiOperation(value = "Create a new user")
    @PostMapping
    public void saveUser(@RequestBody @Valid @NotNull UserRequest newUser) {

        UserDTO userDTO = new UserDTO(newUser);
        this.userService.saveUser(userDTO);
    }

    @ApiOperation(value = "Update an existing user")
    @PutMapping("/{userId}")
    public void updateUser(@PathVariable @NotNull Long userId, @RequestBody @Valid @NotNull UserRequest user) {

        if (userService.getById(userId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não encontrado");

        UserDTO userDTO = new UserDTO(user);
        userDTO.setId(userId);

        this.userService.saveUser(userDTO);
    }

    @ApiOperation(value = "Deletes an existing user by Id")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @NotNull Long userId) {
        this.userService.deleteUser(userId);
    }

    @ApiOperation(value = "Update an existing user partially by the values passed")
    @PatchMapping("/{userId}")
    public void saveManager(@PathVariable @NotNull Long userId, @ApiParam(name = "changes",
                                                                          value = "User values (cpf, nome, dataNascimento)",
                                                                          example = "{'nome': 'João'}",
                                                                          examples = @Example(value = {@ExampleProperty("{'nome': 'João', 'dataNascimento' : '25/11/2000'}") }),
                                                                          type= "HashMap<String, String>",
                                                                          required = true) @NotNull @RequestBody HashMap<String, String> changes) {

        Optional<UserDTO> userDTO = this.userService.getById(userId);

        if (!userDTO.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não encontrado");

        changes.forEach(
                (change, value) -> {
                    switch (change){
                        case "cpf": userDTO.get().setCpf((String) value); break;
                        case "nome": userDTO.get().setNome((String) value); break;
                        case "dataNascimento": userDTO.get().setDataNascimento(LocalDate.parse(value)); break;
                    }
                }
        );

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO.get(), UserDTO.class);

        if(!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, violations.toString());
        }

        this.userService.saveUser(userDTO.get());
    }
}
