package br.com.compass.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Valida se o CPF tem 11 dígitos e segue o padrão básico
    public static boolean isValidCpf(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Validação simplificada - Você pode implementar a lógica completa de validação de dígitos verificadores aqui
        return !cpf.chars().allMatch(c -> c == cpf.charAt(0)); // Verifica se todos os dígitos são iguais
    }

    // Valida o formato de e-mail
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // Valida se a senha atende aos critérios mínimos (8 caracteres, número e letra maiúscula)
    public static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*\\d.*") // Pelo menos um número
                && password.matches(".*[A-Z].*"); // Pelo menos uma letra maiúscula
    }
}
