package company_financial_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class mainFunction {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            System.out.println("Please enter the path to your report.txt file:");
            String filePath = scanner.nextLine();

            FinancialReportGenerator reportGenerator = new FinancialReportGenerator();

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                System.out.println("Input successful, please select your time period:");
                System.out.println("MM/YYYY (e.g., Jan/2024)");
                System.out.println("Q1/YYYY (e.g., Q1/2024)");
                System.out.println("YYYY (e.g., 2024)");
                System.out.println("MM/YYYY-MM/YYYY (e.g., Jan/2024-Mar/2024)");
                String timePeriod = scanner.nextLine();
                reportGenerator.setTargetDates(timePeriod);

                String line;
                List<Long> expenses = new ArrayList<>();

                while ((line = br.readLine()) != null) {
                    reportGenerator.addEntry(line);
                    String[] parts = line.split("\\|");
                    if (!parts[1].startsWith("Product")) {
                        expenses.add(Long.parseLong(parts[3].trim()));
                    }
                }

                reportGenerator.generateReport(timePeriod);
                reportGenerator.weightedAveragePredict(expenses);

            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }

            System.out.println("Would you like to generate another financial report (Y/N)?");
            userInput = scanner.nextLine().trim();

        } while (userInput.equalsIgnoreCase("Y"));

        System.out.println("Thank you for using Company's Financial Analyzer, Goodbye.");
        scanner.close();
    }
}