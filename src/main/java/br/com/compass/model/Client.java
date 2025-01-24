package br.com.compass.model;

import java.time.LocalDate;

public class Client {

    private String fullName;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private String email;
    private String streetName;
    private int streetNumber;
    private String neighborhood;
    private String postalCode;
    private String city;
    private String state;
    private String country;

    // Construtor vazio
    public Client() {
    }

    // Construtor com todos os atributos
    public Client(String fullName, LocalDate birthDate, String cpf, String phoneNumber, String email,
                  String streetName, int streetNumber, String neighborhood, String postalCode,
                  String city, String state, String country) {
        setFullName(fullName);
        setBirthDate(birthDate);
        setCpf(cpf);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setStreetName(streetName);
        setStreetNumber(streetNumber);
        setNeighborhood(neighborhood);
        setPostalCode(postalCode);
        setCity(city);
        setState(state);
        setCountry(country);
    }

    // Getters e setters com validações
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date must be a valid date in the past.");
        }
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid CPF. It must contain 11 digits.");
        }
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty.");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        if (streetName == null || streetName.isEmpty()) {
            throw new IllegalArgumentException("Street name cannot be null or empty.");
        }
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        if (streetNumber <= 0) {
            throw new IllegalArgumentException("Street number must be greater than 0.");
        }
        this.streetNumber = streetNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.isEmpty()) {
            throw new IllegalArgumentException("Neighborhood cannot be null or empty.");
        }
        this.neighborhood = neighborhood;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (postalCode == null || !postalCode.matches("\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("Invalid postal code format. It must be in the format DDDDD-DDD.");
        }
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty.");
        }
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state == null || state.isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty.");
        }
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty.");
        }
        this.country = country;
    }

    // toString() atualizado
    @Override
    public String toString() {
        return "Client Details:" + "\n" +
                "Full Name: " + fullName + "\n" +
                "Birth Date: " + birthDate + "\n" +
                "CPF: " + cpf + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "Email: " + email + "\n" +
                "Address: " + streetName + ", " + streetNumber + " - " + neighborhood + "\n" +
                "Postal Code: " + postalCode + "\n" +
                "City: " + city + ", State: " + state + ", Country: " + country;
    }
}