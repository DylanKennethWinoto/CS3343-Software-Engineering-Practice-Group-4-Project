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
            String line;
            boolean dataFound = false;

            while ((line = br.readLine()) != null) {
                dataFound = true;
                reportGenerator.addEntry(line);
            }

            if (dataFound) {
                System.out.println("Input successful, please select your time period (e.g., May/2024):");
                String timePeriod = scanner.nextLine();
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
