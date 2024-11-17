package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.Category;
import company_financial_analyzer.Department;
import company_financial_analyzer.FinancialReportGenerator;
import company_financial_analyzer.Product;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Test5_for_testing_exceptions {

    // 测试文件路径无效
    @Test
    public void testInvalidFilePath() {
        String invalidFilePath = "invalid/path/to/report.txt";
        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        try {
            BufferedReader br = new BufferedReader(new FileReader(invalidFilePath));
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("invalid\\path\\to\\report.txt (系统找不到指定的路径。)", e.getMessage());
        }
    }

    // 测试产品名称为空
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyProductName() {
        new Product(""); // 应抛出异常
    }

    // 测试部门名称为空
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDepartmentName() {
        new Department(""); // 应抛出异常
    }

    // 测试负开销
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpense() {
        Department department = new Department("Marketing");
        department.addExpense("Advertising", -1000); // 应抛出异常
    }

    // 测试类别名称为空
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCategoryName() {
        new Category("", 100); // 应抛出异常
    }

    // 测试负收入
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRevenue() {
        Product product = new Product("Product A");
        product.addRevenue(-500); // 应抛出异常
    }

    // 测试从文件读取数据（模拟文件）
    @Test
    public void testReadDataFromFile() throws IOException {
        String testFilePath = "test_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("24/May/2024|Marketing Department|Online Advertisement|1000000\n");
            writer.write("24/May/2024||Software License Renewal|1000000\n"); // 部门名称为空
        }

        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        try (BufferedReader br = new BufferedReader(new FileReader(testFilePath))) {
            String line;
            List<Long> expenses = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                reportGenerator.addEntry(line);
                String[] parts = line.split("\\|");
                if (!parts[1].startsWith("Product")) {
                    expenses.add(Long.parseLong(parts[3].trim()));
                }
            }
            // 可以在这里检查 expenses 是否正确
        } catch (IllegalArgumentException e) {
            assertEquals("Department name cannot be null or empty.", e.getMessage());
        }
    }
}