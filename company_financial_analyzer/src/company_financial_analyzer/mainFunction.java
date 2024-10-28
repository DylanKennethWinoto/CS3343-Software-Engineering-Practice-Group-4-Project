package company_financial_analyzer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class mainFunction {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            boolean dataFound = false;

            while ((line = br.readLine()) != null) {
                dataFound = true;
                reportGenerator.addEntry(line);
            }

            if (dataFound) {
                reportGenerator.generateReport(timePeriod);
            } else {
                System.out.println("No data found in the file.");
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        scanner.close();
    }
}
