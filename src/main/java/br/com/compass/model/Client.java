package br.com.compass.model;

import java.time.LocalDate;

public class Client {
    private Integer idClient;
    private String fullName;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private String email;
    private String password;

    // Construtor completo sem o id
    public Client(String fullName, LocalDate birthDate, String cpf, String phoneNumber, String email, String password) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // Construtor completo com o id
    public Client(Integer idClient, String fullName, LocalDate birthDate, String cpf, String phoneNumber, String email, String password) {
        this.idClient = idClient;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // Getters e setters
    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
