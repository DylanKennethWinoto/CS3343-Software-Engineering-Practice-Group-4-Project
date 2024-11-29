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
        String userInput = null;

        do {
            System.out.println("Please enter the path to your report.txt file:");
            String filePath = scanner.nextLine();

            FinancialReportFacade reportFacade = new FinancialReportFacade();
            String timePeriod;
            
            do {
                FinancialReportGenerator.showinput();
                timePeriod = scanner.nextLine();
            } while (!reportFacade.setTargetDates(timePeriod));
 
            try {
                reportFacade.processReport(filePath, timePeriod);
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
