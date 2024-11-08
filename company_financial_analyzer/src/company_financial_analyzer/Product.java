package company_financial_analyzer;

public class Product {
    private final String name;
    private long totalRevenue;

    public Product(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        this.name = name;
        this.totalRevenue = 0;
    }

    public void addRevenue(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Revenue amount cannot be negative.");
        }
        totalRevenue += amount;
    }

    public String getName() {
        return name;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }
}