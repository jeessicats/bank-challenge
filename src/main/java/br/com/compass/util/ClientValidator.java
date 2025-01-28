package br.com.compass.util;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("(\\d{11})");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\d{10,11})");

    public static boolean isValidDate(LocalDate date) {
        return date != null;
    }

    public static boolean isNotInFuture(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static boolean isValidAge(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        int age = today.getYear() - birthDate.getYear();

        if (birthDate.plusYears(age).isAfter(today)) {
            age--;
        }

        return age >= 18 && age <= 100;
    }

    public static boolean isValidCpf(String cpf) {
        return cpf != null && CPF_PATTERN.matcher(cpf).matches();
    }

    public static String promptForValidCpf(Scanner scanner) {
        while (true) {
            System.out.print("Enter CPF (11 digits, numbers only): ");
            String cpf = scanner.nextLine().trim();

            if (isValidCpf(cpf)) {
                return cpf;
            }
            System.out.println("Invalid CPF format. Please try again.");
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*\\d.*")
                && password.matches(".*[A-Z].*");
    }

    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
