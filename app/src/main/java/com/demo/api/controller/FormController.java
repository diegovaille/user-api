package com.demo.api.controller;

import com.demo.api.domain.Pager;
import com.demo.api.dto.UserDTO;
import com.demo.api.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@ApiIgnore
public class FormController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    private final UserService userService;

    public FormController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView findAll(@RequestParam(value = "pageSize") Optional<Integer> pageSize,
                                @RequestParam(value = "page") Optional<Integer> page) {
        ModelAndView mv = new ModelAndView("/index");
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<UserDTO> users =  userService.getAllUsers(evalPage, evalPageSize);

        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);
        mv.addObject("selectedPageSize", evalPageSize);
        mv.addObject("pageSizes", PAGE_SIZES);
        mv.addObject("pager", pager);
        mv.addObject("users", users);

        return mv;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("isEdit", true);
        return add(userService.getById(id).get());
    }

    @GetMapping("/add")
    public ModelAndView add(UserDTO user) {
        ModelAndView mv = new ModelAndView("/add-user");
        mv.addObject("user", user);

        return mv;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return findAll(Optional.empty(), Optional.empty());
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid UserDTO userDTO, Model model, BindingResult result) {
        if(result.hasErrors()) {
            return add(userDTO);
        }

        userService.saveUser(userDTO);

        return findAll(Optional.empty(), Optional.empty());
    }
}