package company_financial_analyzer;

public class Category {
    private String name;
    private long amount;

    public Category(String name, long amount) {
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
