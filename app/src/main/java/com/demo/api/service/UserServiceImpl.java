package com.demo.api.service;

import com.demo.api.domain.User;
import com.demo.api.dto.UserDTO;
import com.demo.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(ObjectMapper objectMapper, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        this.userRepository.save(User.fromDTO(userDTO));
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int pageSize) {
        Pageable paging = PageRequest.of(page, pageSize);
        return userRepository.findAllUsersWithPagination(paging).map(user -> new UserDTO(user));
    }

    @Override
    public List<UserDTO> searchByNameLike(String name) {
        return userRepository.searchByNameLike(name).stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        Optional<User> user = userRepository.findUserById(id);
        return user.isPresent() ? Optional.of(new UserDTO(user.get())) : Optional.empty();
    }

    @Override
    public Optional<UserDTO> getByCpf(String cpf) {
        Optional<User> user = userRepository.findUserByCpf(cpf);
        return user.isPresent() ? Optional.of(new UserDTO(user.get())) : Optional.empty();
    }
}
