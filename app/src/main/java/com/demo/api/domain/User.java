package com.demo.api.domain;

import com.demo.api.dto.UserDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public User() { }

    public User(Long id, String name, String cpf, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public static User fromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getNome(), userDTO.getCpf(), userDTO.getDataNascimento());
    }
}
