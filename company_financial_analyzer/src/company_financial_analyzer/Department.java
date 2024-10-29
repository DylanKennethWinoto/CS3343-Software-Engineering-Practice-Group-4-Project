package company_financial_analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Department {
    private String name;
    private long totalExpense = 0;
    private Map<String, Long> categoryExpenses = new HashMap<>();

    public Department(String name) {
        this.name = name;
    }

    public void addExpense(String category, long amount) {
        totalExpense += amount;
        categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0L) + amount);
    }

    public String getName() {
        return name;
    }

    public long getTotalExpense() {
        return totalExpense;
    }

    public List<Category> getTopCategories() {
        List<Category> categories = new ArrayList<>();
        for (Map.Entry<String, Long> entry : categoryExpenses.entrySet()) {
            categories.add(new Category(entry.getKey(), entry.getValue()));
        }
        categories.sort((a, b) -> Long.compare(b.getAmount(), a.getAmount())); // Sort descending
        return categories;
    }
}