package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportGenerator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class test2 {
	
	@Test
	public void testGenerateReportForJanuary() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    reportGenerator.setTargetDates("Jan/2024");
	    
	    String filePath = "src/data/data2.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line); // 假设你有一个方法来处理每一行数据
            }
        }
	    String report = reportGenerator.generateReport("Jan/2024");
	    String expectedReport = "Jan/2024 Financial Report\n"
	            + "\n"
	            + "Marketing Department Total Expense = 1200000 (80.0%)\n"
	            + "Marketing Department Top 3 Categories:\n"
	            + "1. Online Advertisement - 1200000\n"
	            + "2. N/A\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Accounting Total Expense = 300000 (20.0%)\n"
	            + "Accounting Top 3 Categories:\n"
	            + "1. Office Supplies - 300000\n"
	            + "2. N/A\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Total profit = -1500000"
	            + "\n";

	    assertEquals(expectedReport, report);
	}
	
	@Test
	public void testGenerateReportForQ1() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    reportGenerator.setTargetDates("Q1/2024");
	    
	    String filePath = "src/data/data2.txt"; 
	    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line); // 假设你有一个方法来处理每一行数据
            }
        }
        
	    String report = reportGenerator.generateReport("Q1/2024");
	    String expectedReport = "Q1/2024 Financial Report\n"
	            + "\n"
	            + "Marketing Department Total Expense = 1350000 (62.8%)\n"
	            + "Marketing Department Top 3 Categories:\n"
	            + "1. Online Advertisement - 1200000\n"
	            + "2. Salary - 150000\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Technology Department Total Expense = 500000 (23.3%)\n"
	            + "Technology Department Top 3 Categories:\n"
	            + "1. Server Upgrade - 500000\n"
	            + "2. N/A\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Accounting Total Expense = 300000 (14.0%)\n"
	            + "Accounting Top 3 Categories:\n"
	            + "1. Office Supplies - 300000\n"
	            + "2. N/A\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Total profit = -2150000"
	            + "\n";

	    assertEquals(expectedReport, report);
	}
	
	@Test
	public void testGenerateReportFor2024() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    reportGenerator.setTargetDates("2024");
	    
	    String filePath = "src/data/data2.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line); // 假设你有一个方法来处理每一行数据
            }
        }
	    String report = reportGenerator.generateReport("2024");
	    String expectedReport = "2024 Financial Report\n"
	    		+ "\n"
	    		+ "Marketing Department Total Expense = 4500000 (42.1%)\n"
	    		+ "Marketing Department Top 3 Categories:\n"
	    		+ "1. Online Advertisement - 3700000\n"
	    		+ "2. Salary - 800000\n"
	    		+ "3. N/A\n"
	    		+ "\n"
	    		+ "Accounting Total Expense = 3300000 (30.8%)\n"
	    		+ "Accounting Top 3 Categories:\n"
	    		+ "1. Year-End Bonuses - 2000000\n"
	    		+ "2. Audit Fees - 600000\n"
	    		+ "3. Consulting Fees - 400000\n"
	    		+ "\n"
	    		+ "Technology Department Total Expense = 2900000 (27.1%)\n"
	    		+ "Technology Department Top 3 Categories:\n"
	    		+ "1. Hardware Purchase - 900000\n"
	    		+ "2. Cloud Services - 800000\n"
	    		+ "3. Website Maintenance - 700000\n"
	    		+ "\n"
	    		+ "Product A Total Revenue = 25000000\n"
	    		+ "Product B Total Revenue = 30000000\n"
	    		+ "Product C Total Revenue = 15000000\n"
	    		+ "Product D Total Revenue = 50000000\n"
	    		+ "Total profit = 109300000"
	    		+ "\n";

	    assertEquals(expectedReport, report);
	}
	
	@Test
	public void testGenerateReportForPeriod() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    reportGenerator.setTargetDates("Jan/2024-Jun/2024");
	    
	    String filePath = "src/data/data2.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line); // 假设你有一个方法来处理每一行数据
            }
        }
	    String report = reportGenerator.generateReport("Jan/2024-Jun/2024");
	    String expectedReport = "Jan/2024-Jun/2024 Financial Report\n"
	            + "\n"
	            + "Marketing Department Total Expense = 3600000 (65.5%)\n"
	            + "Marketing Department Top 3 Categories:\n"
	            + "1. Online Advertisement - 3200000\n"
	            + "2. Salary - 400000\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Technology Department Total Expense = 1200000 (21.8%)\n"
	            + "Technology Department Top 3 Categories:\n"
	            + "1. Website Maintenance - 700000\n"
	            + "2. Server Upgrade - 500000\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Accounting Total Expense = 700000 (12.7%)\n"
	            + "Accounting Top 3 Categories:\n"
	            + "1. Consulting Fees - 400000\n"
	            + "2. Office Supplies - 300000\n"
	            + "3. N/A\n"
	            + "\n"
	            + "Product A Total Revenue = 25000000\n"
	            + "Total profit = 19500000"
	            + "\n";
	    assertEquals(expectedReport, report);
	}
}