package company_financial_analyzer;

public class Category {
    private final String name;
    private final long amount;

    public Category(String name, long amount) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }
}