package company_financial_analyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Department {
    private final String name;
    private long totalExpense = 0;
    private final Map<String, Long> categoryExpenses = new HashMap<>();
    private final Map<Integer, Long> yearlyExpenses = new HashMap<>();

    public Department(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty.");
        }
        this.name = name;
    }

    public void addExpense(String category, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Expense amount cannot be negative.");
        }
        totalExpense += amount;
        categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0L) + amount);

        int year = extractYearFromDate(category);
        if (year != -1) {
            yearlyExpenses.put(year, yearlyExpenses.getOrDefault(year, 0L) + amount);
        }
    }

    public long getYearlyExpense(int year) {
        return yearlyExpenses.getOrDefault(year, 0L);
    }

    private int extractYearFromDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        try {
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            return calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            System.err.println("Invalid date format: " + date);
            return -1;
        }
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
        categories.sort(Comparator.comparingLong(Category::getAmount).reversed());
        return categories;
    }
}