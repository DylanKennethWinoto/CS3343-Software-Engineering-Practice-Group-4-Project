package company_financial_analyzer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import java.util.Calendar;

public class Department {
    private String name;
    private long totalExpense = 0;
    private Map<String, Long> categoryExpenses = new HashMap<>();
    private Map<Integer, Long> yearlyExpenses = new HashMap<>();

    public Department(String name) {
        this.name = name;
    }

    public void addExpense(String category, long amount) {
        totalExpense += amount;
        categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0L) + amount);
        
        // Assuming you have a method to extract year from the date
        int year = extractYearFromDate(category);
        yearlyExpenses.put(year, yearlyExpenses.getOrDefault(year, 0L) + amount);
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
            System.out.println("Invalid date format: " + date);
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
        categories.sort((a, b) -> Long.compare(b.getAmount(), a.getAmount())); // Sort descending
        return categories;
    }
}