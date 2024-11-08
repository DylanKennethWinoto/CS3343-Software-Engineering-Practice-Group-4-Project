package company_financial_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainFunction {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String userInput;
            do {
                String filePath = promptFilePath(scanner);
                
                FinancialReportGenerator reportGenerator = new FinancialReportGenerator();

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String timePeriod = promptTimePeriod(scanner);
                    reportGenerator.setTargetDates(timePeriod);

                    List<Long> expenses = processFile(br, reportGenerator);
                    
                    reportGenerator.generateReport(timePeriod);
                    reportGenerator.weightedAveragePredict(expenses);

                } catch (IOException e) {
                    System.out.println("An error occurred while reading the file: " + e.getMessage());
                }

                userInput = promptRepeat(scanner);

            } while (userInput.equalsIgnoreCase("Y"));

            System.out.println("Thank you for using Company's Financial Analyzer, Goodbye.");
        }
    }

    private static String promptFilePath(Scanner scanner) {
        System.out.println("Please enter the path to your report.txt file:");
        return scanner.nextLine().trim();
    }

    private static String promptTimePeriod(Scanner scanner) {
        System.out.println("Input successful, please select your time period:");
        System.out.println("MM/YYYY (e.g., Jan/2024)");
        System.out.println("Q1/YYYY (e.g., Q1/2024)");
        System.out.println("YYYY (e.g., 2024)");
        System.out.println("MM/YYYY-MM/YYYY (e.g., Jan/2024-Mar/2024)");
        return scanner.nextLine().trim();
    }

    private static List<Long> processFile(BufferedReader br, FinancialReportGenerator reportGenerator) throws IOException {
        List<Long> expenses = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            reportGenerator.addEntry(line);
            String[] parts = line.split("\\|");
            if (!parts[1].startsWith("Product")) {
                try {
                    expenses.add(Long.parseLong(parts[3].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid expense format: " + parts[3].trim());
                }
            }
        }
        return expenses;
    }

    private static String promptRepeat(Scanner scanner) {
        System.out.println("Would you like to generate another financial report (Y/N)?");
        return scanner.nextLine().trim();
    }
}