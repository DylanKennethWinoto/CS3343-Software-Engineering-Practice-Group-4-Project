package company_financial_analyzer;
import java.util.*;

public class FinancialReportGenerator {
    private Map<String, Department> departments = new HashMap<>();
    private long totalRevenue = 0;
    private long totalExpenses = 0;

    public void addEntry(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4)
            return;

        String date = parts[0];
        String departmentName = parts[1];
        String category = parts[2];
        long amount = Long.parseLong(parts[3]);

        // Handle revenue and expenses
        if (category.equals("Revenue*")) {
            totalRevenue += amount;
            departments.computeIfAbsent(departmentName, k -> new Department(departmentName)).addRevenue(amount);
        } else {
            totalExpenses += amount;
            departments.computeIfAbsent(departmentName, k -> new Department(departmentName)).addExpense(category,
                    amount);
        }
    }

    public void generateReport(String monthYear) {
        System.out.println(monthYear + " Financial Report\n");

        for (Department dept : departments.values()) {
            dept.calculateTotal();
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

        // 只输出产品的收入
        long productXRevenue = departments.getOrDefault("Product X", new Department("Product X")).getTotalRevenue();
        System.out.printf("Product X Total Revenue = %d\n", productXRevenue);

        long totalProfit = totalRevenue - totalExpenses;
        System.out.printf("Total profit = %d\n", totalProfit);
    }

    private static class Department {
        private String name;
        private long totalExpense = 0;
        private long totalRevenue = 0;
        private Map<String, Long> categoryExpenses = new HashMap<>();

        public Department(String name) {
            this.name = name;
        }

        public void addExpense(String category, long amount) {
            totalExpense += amount;
            categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0L) + amount);
        }

        public void addRevenue(long amount) {
            totalRevenue += amount;
        }

        public void calculateTotal() {
            // Additional calculations can be done here if needed
        }

        public String getName() {
            return name;
        }

        public long getTotalExpense() {
            return totalExpense;
        }

        public long getTotalRevenue() {
            return totalRevenue;
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
