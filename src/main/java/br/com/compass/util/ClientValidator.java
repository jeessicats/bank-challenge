package br.com.compass.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ClientValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile(
            "(\\d{11})", Pattern.COMMENTS
    );

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Regex para validação de números de telefone
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\d{10,11})");

    // Verifica se a data não é nula
    public static boolean isValidDate(LocalDate date) {
        return date != null;
    }

    // Verifica se a data está no passado
    public static boolean isNotInFuture(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    // Verifica se o cliente tem pelo menos 18 anos
    public static boolean isValidAge(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        int age = today.getYear() - birthDate.getYear();

        if (birthDate.plusYears(age).isAfter(today)) {
            age--; // Ajusta a idade se o aniversário ainda não aconteceu este ano
        }

        return age >= 18 && age <= 100; // Verifica se a idade está entre 18 e 100 anos
    }

    // Verifica se o CPF está em um formato válido
    public static boolean isValidCpf(String cpf) {
        return cpf != null && CPF_PATTERN.matcher(cpf).matches();
    }

    // Verifica se o email está em um formato válido
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // Verifica se a senha atende aos critérios mínimos
    public static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*\\d.*") // Contém pelo menos um número
                && password.matches(".*[A-Z].*"); // Contém pelo menos uma letra maiúscula
    }

    // Verifica se o valor não é nulo ou vazio
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Valida o número de telefone
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
