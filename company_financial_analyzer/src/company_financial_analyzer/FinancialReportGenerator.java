package company_financial_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FinancialReportGenerator {
    private Map<String, Department> departments = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private long totalRevenue = 0;
    private long totalExpenses = 0;
    private List<String> targetDates = new ArrayList<>();
    
    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public void setTargetDates(String timePeriod) {
        targetDates.clear();
        if (timePeriod.matches("\\d{4}")) { // YYYY
            for (int month = 1; month <= 12; month++) {
                targetDates.add(String.format("%s/%d", monthToText(month), Integer.parseInt(timePeriod)));
            }
        } else if (timePeriod.matches("(Q[1-4])/\\d{4}")) { // Q1/YYYY
            String[] parts = timePeriod.split("/");
            String quarter = parts[0];
            String year = parts[1];
            int startMonth = (Integer.parseInt(quarter.substring(1)) - 1) * 3 + 1;
            for (int month = startMonth; month < startMonth + 3; month++) {
                targetDates.add(String.format("%s/%d", monthToText(month), Integer.parseInt(year)));
            }
        } else if (timePeriod.matches("\\w{3}/\\d{4}")) { // MMM/yyyy
            targetDates.add(timePeriod);
        } else if (timePeriod.matches("\\w{3}/\\d{4}-\\w{3}/\\d{4}")) { // MMM/yyyy-MMM/yyyy
            String[] parts = timePeriod.split("-");
            String start = parts[0];
            String end = parts[1];
            
            String[] startParts = start.split("/");
            String[] endParts = end.split("/");
            
            int startMonth = textToMonth(startParts[0]);
            int endMonth = textToMonth(endParts[0]);
            int startYear = Integer.parseInt(startParts[1]);
            int endYear = Integer.parseInt(endParts[1]);

            for (int year = startYear; year <= endYear; year++) {
                int monthStart = (year == startYear) ? startMonth : 1;
                int monthEnd = (year == endYear) ? endMonth : 12;
                for (int month = monthStart; month <= monthEnd; month++) {
                    targetDates.add(String.format("%s/%d", monthToText(month), year));
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid time period format.");
        }
    }

    private String monthToText(int month) {
        return (month >= 1 && month <= 12) ? months[month - 1] : null;
    }

    private int textToMonth(String month) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) {
                return i + 1;
            }
        }
        return -1;
    }

    public void addEntry(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            System.out.println("Invalid line format: " + line);
            return;
        }

        String date = parts[0];
        String name = parts[1];
        String category = parts[2];
        long amount = Long.parseLong(parts[3].trim());

        boolean dateMatches = targetDates.stream().anyMatch(targetDate -> date.contains(targetDate));
        if (!dateMatches) {
            System.out.println("Skipping line (date mismatch): " + line);
            return;
        }

        if (name.startsWith("Product")) {
            totalRevenue += amount;
            products.computeIfAbsent(name, k -> new Product(name)).addRevenue(amount);
        } else {
            totalExpenses += amount;
            departments.computeIfAbsent(name, k -> new Department(name)).addExpense(category, amount);
        }
    }

    public void weightedAveragePredict(List<Long> expenses) {
        if (expenses.isEmpty()) {
            System.out.println("No expense data available for prediction.");
            return;
        }
        
        int n = expenses.size();
        double weightedSum = 0;
        int totalWeight = 0;
        
        for (int i = 0; i < n; i++) {
            int weight = i + 1;
            weightedSum += expenses.get(i) * weight;
            totalWeight += weight;
        }
        
        double prediction = weightedSum / totalWeight;
        System.out.printf("Predicted future expense: %.2f\n", prediction*12, ("Year"));
    }

    public String generateReport(String timePeriod) {
        StringBuilder reportBuilder = new StringBuilder();

        long totalProfit = totalRevenue - totalExpenses;

        Map<String, Double> departmentPercentages = new HashMap<>();
        for (Department dept : departments.values()) {
            double percentage = (dept.getTotalExpense() * 100.0) / totalExpenses;
            departmentPercentages.put(dept.getName(), percentage);
        }

        List<Map.Entry<String, Double>> sortedDepartments = departmentPercentages.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        reportBuilder.append(timePeriod).append(" Financial Report\n\n");

        for (Map.Entry<String, Double> entry : sortedDepartments) {
            String deptName = entry.getKey();
            double percentage = entry.getValue();

            Department dept = departments.get(deptName);

            reportBuilder.append(String.format("%s Total Expense = %d (%.1f%%)\n",
                    dept.getName(), dept.getTotalExpense(), percentage));
            reportBuilder.append(String.format("%s Top 3 Categories:\n", dept.getName()));
            List<Category> topCategories = dept.getTopCategories();

            for (int i = 0; i < 3; i++) {
                if (i < topCategories.size()) {
                    reportBuilder.append(String.format("%d. %s - %d\n", i + 1,
                            topCategories.get(i).getName(), topCategories.get(i).getAmount()));
                } else {
                    reportBuilder.append(String.format("%d. N/A\n", i + 1));
                }
            }
            reportBuilder.append("\n");
        }

        for (Product prod : products.values()) {
            reportBuilder.append(String.format("%s Total Revenue = %d\n", prod.getName(),
                    prod.getTotalRevenue()));
        }

        reportBuilder.append(String.format("Total profit = %d\n", totalProfit));

        System.out.println(reportBuilder.toString());
        return reportBuilder.toString();
    }

    private static class Department {
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
            categories.sort((a, b) -> Long.compare(b.getAmount(), a.getAmount()));
            return categories;
        }
    }

    private static class Product {
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

    private static class Category {
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
}