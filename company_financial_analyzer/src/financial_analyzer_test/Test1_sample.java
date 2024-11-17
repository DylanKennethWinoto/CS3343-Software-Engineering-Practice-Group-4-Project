package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportGenerator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test1_sample {

    @Test
    public void testGenerateReportFromFile() throws IOException {
        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();

        // 设置时间段
        reportGenerator.setTargetDates("May/2024");

        // 从文件加载测试数据
        String filePath = "src/data/data1.txt"; // 确保这个路径正确
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line); // 假设你有一个方法来处理每一行数据
            }
        }

        // 生成报告并返回字符串
        String report = reportGenerator.generateReport("May/2024");

        // 预期报告内容
        String expectedReport = "May/2024 Financial Report\n"
        		+ "\n"
        		+ "Marketing Department Total Expense = 3100000 (60.8%)\n"
        		+ "Marketing Department Top 3 Categories:\n"
        		+ "1. Online Advertisement - 3000000\n"
        		+ "2. Salary - 100000\n"
        		+ "3. N/A\n"
        		+ "\n"
        		+ "Accounting Total Expense = 1000000 (19.6%)\n"
        		+ "Accounting Top 3 Categories:\n"
        		+ "1. Software License Renewal - 1000000\n"
        		+ "2. N/A\n"
        		+ "3. N/A\n"
        		+ "\n"
        		+ "Technology Department Total Expense = 1000000 (19.6%)\n"
        		+ "Technology Department Top 3 Categories:\n"
        		+ "1. Server Maintenance - 1000000\n"
        		+ "2. N/A\n"
        		+ "3. N/A\n"
        		+ "\n"
        		+ "Product X Total Revenue = 10000000\n"
        		+ "Total profit = 4900000\n"
        		;

        // 验证生成的报告是否与预期相同
        assertEquals(expectedReport, report);
    }
}