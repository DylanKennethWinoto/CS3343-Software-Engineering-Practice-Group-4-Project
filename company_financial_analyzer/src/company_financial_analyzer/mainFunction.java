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

            FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
            // show the file information if the user file is in correct type.
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {//check if the file is in the correct format
      
            	String timePeriod = null;
            	do { 
            		FinancialReportGenerator.showinput();
                    timePeriod = scanner.nextLine();
            } while (!reportGenerator.setTargetDates(timePeriod));
  

                String line;
                List<Long> expenses = new ArrayList<>();

               
                while ((line = br.readLine()) != null) {//br read line is to get the information line by line. Check if all content is readed 
                    String[] parts = line.split("\\|");
                    System.out.println("Read line: " + line); // 调试输出? for user check

                    // 检查条目格式
                    if (parts.length != 4) {//check if the content of one line have 4 item
                        System.err.println("Invalid line format: " + line);
                        continue;
                    }

                    String date = parts[0];
                    // if time matched, then add the expenses to the list
                    if (reportGenerator.isDateInTarget(date)) {
                        reportGenerator.addEntry(line);
                        if (!parts[1].startsWith("Product")) {
                            expenses.add(Long.parseLong(parts[3].trim()));//.trim is for deleting the space before and after the string
                        }
                    }
                }

                reportGenerator.generateReport(timePeriod);

                // 仅在输入年份时进行预测
                if (timePeriod.matches("\\d{4}")) {//if user only input the year then do the prediction
                    reportGenerator.weightedAveragePredict(expenses);
                }

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
