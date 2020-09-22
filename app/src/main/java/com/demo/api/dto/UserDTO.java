package com.demo.api.dto;

import com.demo.api.controller.request.UserRequest;
import com.demo.api.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class UserDTO implements Serializable {

    private Long id;

    private String nome;

    private String cpf;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public UserDTO() {
    }

    public UserDTO(UserRequest user) {
        this.cpf = user.getCpf();
        this.dataNascimento = user.getDataNascimento();
        this.nome = user.getNome();
    }

    public UserDTO(User user) {
        this.cpf = user.getCpf();
        this.dataNascimento = user.getBirthDate();
        this.nome = user.getName();
        this.id = user.getId();
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
