package company_financial_analyzer;
import java.util.*;

public class FinancialReportGenerator {
    private Map<String, Department> departments = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private long totalRevenue = 0;
    private long totalExpenses = 0;
    private List<String> targetDates = new ArrayList<>();

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
            System.out.println("Quarter: " + quarter + ", Year: " + year); // 调试输出
            System.out.println("Start Month: " + startMonth); // 调试输出
            for (int month = startMonth; month < startMonth + 3; month++) {
                targetDates.add(String.format("%s/%d", monthToText(month), Integer.parseInt(year)));
                System.out.println("Adding target date: " + String.format("%s/%d", monthToText(month), Integer.parseInt(year))); // 调试输出
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
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "Jan");
        monthMap.put(2, "Feb");
        monthMap.put(3, "Mar");
        monthMap.put(4, "Apr");
        monthMap.put(5, "May");
        monthMap.put(6, "Jun");
        monthMap.put(7, "Jul");
        monthMap.put(8, "Aug");
        monthMap.put(9, "Sep");
        monthMap.put(10, "Oct");
        monthMap.put(11, "Nov");
        monthMap.put(12, "Dec");
        return monthMap.get(month);
    }

    private int textToMonth(String month) {
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("Jan", 1);
        monthMap.put("Feb", 2);
        monthMap.put("Mar", 3);
        monthMap.put("Apr", 4);
        monthMap.put("May", 5);
        monthMap.put("Jun", 6);
        monthMap.put("Jul", 7);
        monthMap.put("Aug", 8);
        monthMap.put("Sep", 9);
        monthMap.put("Oct", 10);
        monthMap.put("Nov", 11);
        monthMap.put("Dec", 12);
        return monthMap.get(month);
    }

    public void addEntry(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            System.out.println("Invalid line format: " + line); // 调试输出
            return;
        }

        String date = parts[0];
        String name = parts[1];
        String category = parts[2];
        long amount = Long.parseLong(parts[3]);

        // 只处理指定时间段的数据
        boolean dateMatches = targetDates.stream().anyMatch(targetDate -> date.contains(targetDate));
        if (!dateMatches) {
            System.out.println("Skipping line (date mismatch): " + line); // 调试输出
            return;
        }

        System.out.println("Processing line: " + line); // 调试输出

        if (category.equals("Revenue*")) {
            totalRevenue += amount;
            products.computeIfAbsent(name, k -> new Product(name)).addRevenue(amount);
        } else {
            totalExpenses += amount;
            departments.computeIfAbsent(name, k -> new Department(name)).addExpense(category, amount);
        }
    }

    public void generateReport(String timePeriod) {
        System.out.println(timePeriod + " Financial Report\n");

        for (Department dept : departments.values()) {
            System.out.printf("%s Total Expense = %d (%.1f%%)\n", dept.getName(), dept.getTotalExpense(),
                    (dept.getTotalExpense() * 100.0) / totalExpenses);
            System.out.printf("%s Top 3 Categories:\n", dept.getName());
            List<Category> topCategories = dept.getTopCategories();

            for (int i = 0; i < 3; i++) {
                if (i < topCategories.size()) {
                    System.out.printf("%d. %s - %d\n", i + 1, topCategories.get(i).getName(),
                            topCategories.get(i).getAmount());
                } else {
                    System.out.printf("%d. N/A\n", i + 1);
                }
            }
            System.out.println();
        }

        for (Product prod : products.values()) {
            System.out.printf("%s Total Revenue = %d\n", prod.getName(), prod.getTotalRevenue());
        }

        long totalProfit = totalRevenue - totalExpenses;
        System.out.printf("Total profit = %d\n", totalProfit);
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
            categories.sort((a, b) -> Long.compare(b.getAmount(), a.getAmount())); // Sort descending
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
