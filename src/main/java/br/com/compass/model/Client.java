package br.com.compass.model;

import java.time.LocalDate;

public class Client {

    private String name;
    private LocalDate birthDate;
    private String cpf;
    private String phone;
    private String email;
    private String address;

    public Client() {
    }

    public Client(String name, LocalDate birthDate, String cpf, String phone, String email, String address) {
        this.name = name;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (cpf != null && cpf.matches("\\d{11}")) {
            this.cpf = cpf;
        } else {
            throw new IllegalArgumentException("Invalid CPF. It must contain 11 digits.");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return  "Name: " + name + "\n" +
                "Birth Date: " + birthDate + "\n" +
                "CPF: " + cpf + "\n" +
                "Phone: " + phone + "\n" +
                "Email: " + email + "\n" +
                "Address: " + address;
    }
}
