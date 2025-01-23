package br.com.compass.model;

public enum AccountType {
    SAVINGS("Savings Account"),
    CHECKING("Checking Account"),
    SALARY("Salary Account");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
