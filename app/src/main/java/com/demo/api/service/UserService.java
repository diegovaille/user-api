package com.demo.api.service;

import com.demo.api.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserDTO userDTO);

    void deleteUser(Long userId);

    Page<UserDTO> getAllUsers(int page, int pageSize);

    List<UserDTO> searchByNameLike(String name);

    Optional<UserDTO> getById(Long id);

    Optional<UserDTO> getByCpf(String cpf);

}
