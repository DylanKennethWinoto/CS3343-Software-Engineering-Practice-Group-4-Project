package company_financial_analyzer;

public class Product {
    private String name;
    private long totalRevenue = 0;

    public Product(String name) {
        this.name = name;
    }

    public void addRevenue(long amount) {
        totalRevenue += amount;
    }

    public String getName() {
        return name;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }
}
