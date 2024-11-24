package company_financial_analyzer;

import java.util.*;
import java.util.stream.Collectors;

public class FinancialReportGenerator {
	private final Map<String, Department> departments = new HashMap<>();
	private final Map<String, Product> products = new HashMap<>();
	private long totalRevenue = 0;
	private long totalExpenses = 0;
	private final List<String> targetDates = new ArrayList<>();
	private final List<Long> expenses = new ArrayList<>();
	private final List<String> expenseDates = new ArrayList<>();

	private final String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
			"Dec" };

	public boolean setTargetDates(String timePeriod) {//time period is the target time that want to check
		targetDates.clear();
		if (timePeriod.matches("\\d{4}")) {//such 2024
			addMonthsForYear(Integer.parseInt(timePeriod));
			return true;
		} else if (timePeriod.matches("(Q[1-4])/\\d{4}")) {//such Q1/2024 
			addMonthsForQuarter(timePeriod);
			return true;
		} else if (timePeriod.matches("\\w{3}/\\d{4}")&& check_month(timePeriod)) {//such Jan /2024
			targetDates.add(timePeriod);
			return true;
		} else if (timePeriod.matches("\\w{3}/\\d{4}-\\w{3}/\\d{4}")) {//such Jan /2024-Mar/2024
			addMonthsForRange(timePeriod);
			return true;
		} else {
			System.out.println("Invalid time period format.please retry to select time period.");
			return false;//report error
		}
	}

	private void addMonthsForYear(int year) {//for case 2024
		for (int month = 1; month <= 12; month++) {
			targetDates.add(String.format("%s/%d", monthToText(month), year));
		}
	}

	private void addMonthsForQuarter(String timePeriod) {//for case Q1/2024
		String[] parts = timePeriod.split("/");
		int startMonth = (Integer.parseInt(parts[0].substring(1)) - 1) * 3 + 1;
		int year = Integer.parseInt(parts[1]);
		for (int month = startMonth; month < startMonth + 3; month++) {
			targetDates.add(String.format("%s/%d", monthToText(month), year));
		}
	}
	private boolean check_month(String timePeriod) {
		for (int i = 0; i < months.length; i++) {
            if (timePeriod. substring(0,2).equals(months[i])) {
                return true;
            }
		}    
        return false;
	}
	private void addMonthsForRange(String timePeriod) {//for case Jan/2024-Mar/2024
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

	private String monthToText(int month) {//convert the number to text
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

		// 处理日期和金额
		if (isDateInTarget(date)) {
			processEntry(name, category, amount);
			if (!name.startsWith("Product")) {
				expenses.add(amount);
				expenseDates.add(date);
				int month = getMonthFromExpenseDate(date);
				System.out.printf("Added expense: %d on %s (Month: %d)\n", amount, date, month);
			}
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

	public boolean isDateInTarget(String date) {
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

		System.out.println("Calculating weighted average prediction...");
		for (int i = 0; i < expenses.size(); i++) {
			System.out.printf("Expense %d: %d\n", i + 1, expenses.get(i));
		}

		double prediction = calculateWeightedAverage(expenses);
		System.out.printf("Predicted future expense for the year: %.2f\n", prediction * 12);
	}

	private int getMonthFromExpenseDate(String date) {
		String[] parts = date.split("/");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Invalid date format: " + date);
		}
		String monthText = parts[1]; // 获取文本月份部分
		return textToMonth(monthText); // 转换为数字（1-12）
	}

	private double calculateWeightedAverage(List<Long> expenses) {
		if (expenses.isEmpty()) {
			throw new IllegalArgumentException("No expenses to calculate average.");
		}

		// 获取所有不同的月份
		Set<Integer> uniqueMonths = new HashSet<>();
		for (String date : expenseDates) {
			uniqueMonths.add(getMonthFromExpenseDate(date));
		}

		// 将月份转换为列表并排序
		List<Integer> sortedMonths = new ArrayList<>(uniqueMonths);
		Collections.sort(sortedMonths);

		// 创建月份到权重的映射，较晚的月份权重更高
		Map<Integer, Integer> monthWeights = new HashMap<>();
		for (int i = 0; i < sortedMonths.size(); i++) {
			monthWeights.put(sortedMonths.get(i), i + 1); // 较晚的月份权重更高
		}

		double weightedSum = 0;
		int totalWeight = 0;

		// 计算加权和
		for (int i = 0; i < expenses.size(); i++) {
			String date = expenseDates.get(i);
			int month = getMonthFromExpenseDate(date);
			int weight = monthWeights.get(month); // 获取该月份的权重

			weightedSum += expenses.get(i) * weight;
			totalWeight += weight;

			System.out.printf("Calculating for expense: %d (Month: %d, Weight: %d)\n", expenses.get(i), month, weight);
		}

		double average = weightedSum / totalWeight;
		System.out.printf("Total weighted sum: %.2f, Total weight: %d, Weighted average: %.2f\n", weightedSum,
				totalWeight, average);
		return average;
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
		Map<String, Double> departmentPercentages = departments.values().stream().collect(
				Collectors.toMap(Department::getName, dept -> (dept.getTotalExpense() * 100.0) / totalExpenses));

		List<Map.Entry<String, Double>> sortedDepartments = departmentPercentages.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

		for (Map.Entry<String, Double> entry : sortedDepartments) {
			String deptName = entry.getKey();
			double percentage = entry.getValue();
			Department dept = departments.get(deptName);

			reportBuilder.append(String.format("%s Total Expense = %d (%.1f%%)\n", dept.getName(),
					dept.getTotalExpense(), percentage));
			reportBuilder.append(String.format("%s Top 3 Categories:\n", dept.getName()));

			List<Category> topCategories = dept.getTopCategories();
			for (int i = 0; i < 3; i++) {
				if (i < topCategories.size()) {
					reportBuilder.append(String.format("%d. %s - %d\n", i + 1, topCategories.get(i).getName(),
							topCategories.get(i).getAmount()));
				} else {
					reportBuilder.append(String.format("%d. N/A\n", i + 1));
				}
			}
			reportBuilder.append("\n");
		}
	}
	public static void showinput () {
		System.out.println("Input successful.");
        System.out.println("MM/YYYY (e.g., Jan/2024)");
        System.out.println("Q1/YYYY (e.g., Q1/2024)");
        System.out.println("YYYY (e.g., 2024)");
        System.out.println("MM/YYYY-MM/YYYY (e.g., Jan/2024-Mar/2024)");
        System.out.println("please select your time period:");
	}

	private void generateProductReport(StringBuilder reportBuilder) {
		for (Product prod : products.values()) {
			reportBuilder.append(String.format("%s Total Revenue = %d\n", prod.getName(), prod.getTotalRevenue()));
		}
	}
}
