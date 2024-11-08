package company_financial_analyzer;

import java.util.*;
import java.util.stream.Collectors;

public class FinancialReportGenerator {
    private final Map<String, Department> departments = new HashMap<>();
    private final Map<String, Product> products = new HashMap<>();
    private long totalRevenue = 0;
    private long totalExpenses = 0;
    private final List<String> targetDates = new ArrayList<>();

    private final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                     "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public void setTargetDates(String timePeriod) {
        targetDates.clear();
        if (timePeriod.matches("\\d{4}")) {
            addMonthsForYear(Integer.parseInt(timePeriod));
        } else if (timePeriod.matches("(Q[1-4])/\\d{4}")) {
            addMonthsForQuarter(timePeriod);
        } else if (timePeriod.matches("\\w{3}/\\d{4}")) {
            targetDates.add(timePeriod);
        } else if (timePeriod.matches("\\w{3}/\\d{4}-\\w{3}/\\d{4}")) {
            addMonthsForRange(timePeriod);
        } else {
            throw new IllegalArgumentException("Invalid time period format.");
        }
    }

    private void addMonthsForYear(int year) {
        for (int month = 1; month <= 12; month++) {
            targetDates.add(String.format("%s/%d", monthToText(month), year));
        }
    }

    private void addMonthsForQuarter(String timePeriod) {
        String[] parts = timePeriod.split("/");
        int startMonth = (Integer.parseInt(parts[0].substring(1)) - 1) * 3 + 1;
        int year = Integer.parseInt(parts[1]);
        for (int month = startMonth; month < startMonth + 3; month++) {
            targetDates.add(String.format("%s/%d", monthToText(month), year));
        }
    }

    private void addMonthsForRange(String timePeriod) {
        String[] parts = timePeriod.split("-");
        String[] startParts = parts[0].split("/");
        String[] endParts = parts[1].split("/");

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
    }

    private String monthToText(int month) {
        return (month >= 1 && month <= 12) ? months[month - 1] : null;
    }

    private int textToMonth(String month) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(month)) {
                return i + 1;
            }
        }
        throw new IllegalArgumentException("Invalid month: " + month);
    }

    public void addEntry(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            System.err.println("Invalid line format: " + line);
            return;
        }

        String date = parts[0];
        String name = parts[1];
        String category = parts[2];
        long amount = parseAmount(parts[3]);

        if (isDateInTarget(date)) {
            processEntry(name, category, amount);
        } else {
            System.out.println("Skipping line (date mismatch): " + line);
        }
    }

    private long parseAmount(String amountStr) {
        try {
            return Long.parseLong(amountStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + amountStr, e);
        }
    }

    private boolean isDateInTarget(String date) {
        return targetDates.stream().anyMatch(date::contains);
    }

    private void processEntry(String name, String category, long amount) {
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

        double prediction = calculateWeightedAverage(expenses);
        System.out.printf("Predicted future expense for the year: %.2f\n", prediction * 12);
    }

    private double calculateWeightedAverage(List<Long> expenses) {
        int n = expenses.size();
        double weightedSum = 0;
        int totalWeight = 0;

        for (int i = 0; i < n; i++) {
            int weight = i + 1;
            weightedSum += expenses.get(i) * weight;
            totalWeight += weight;
        }
        return weightedSum / totalWeight;
    }

    public String generateReport(String timePeriod) {
        StringBuilder reportBuilder = new StringBuilder();
        long totalProfit = totalRevenue - totalExpenses;

        reportBuilder.append(timePeriod).append(" Financial Report\n\n");

        generateDepartmentReport(reportBuilder);
        generateProductReport(reportBuilder);

        reportBuilder.append(String.format("Total profit = %d\n", totalProfit));

        System.out.println(reportBuilder.toString());
        return reportBuilder.toString();
    }

    private void generateDepartmentReport(StringBuilder reportBuilder) {
        Map<String, Double> departmentPercentages = departments.values().stream()
                .collect(Collectors.toMap(
                        Department::getName,
                        dept -> (dept.getTotalExpense() * 100.0) / totalExpenses
                ));

        List<Map.Entry<String, Double>> sortedDepartments = departmentPercentages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

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
    }

    private void generateProductReport(StringBuilder reportBuilder) {
        for (Product prod : products.values()) {
            reportBuilder.append(String.format("%s Total Revenue = %d\n", prod.getName(),
                    prod.getTotalRevenue()));
        }
    }
}