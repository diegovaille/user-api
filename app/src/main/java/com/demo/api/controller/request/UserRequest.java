package com.demo.api.controller.request;

import com.demo.api.validator.ValidCPF;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@JsonPropertyOrder({ "nome", "cpf", "dataNascimento"})
public class UserRequest implements Serializable {

    @NotNull(message = "Nome deve ser preenchido")
    @NotBlank(message = "Nome não pode ser em branco")
    @ApiModelProperty(value = "Nome da pessoa", example = "João", position = 0)
    private String nome;

    @NotNull(message = "CPF deve ser preenchido")
    @NotBlank(message = "CPF não pode ser em branco")
    @ValidCPF
    @ApiModelProperty(value = "CPF da pessoa", example = "381.783.630-97", position = 1)
    private String cpf;

    @NotNull(message = "Data de Nascimento deve ser preenchida")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(value = "Data de Nascimento", example = "23/12/1990", position = 2)
    private LocalDate dataNascimento;

    public UserRequest() {
    }

    public String getNome() {
        return nome;
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
}
