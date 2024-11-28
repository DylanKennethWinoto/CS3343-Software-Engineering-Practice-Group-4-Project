package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.Category;
import company_financial_analyzer.Department;
import company_financial_analyzer.FinancialReportGenerator;
import company_financial_analyzer.Product;

import java.io.*;
import java.text.ParseException;
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
    
    //department test starts
    
    @Test()
    public void testEmptyDepartmentName() {
    	try {
    		new Department(""); // 应抛出异常
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Department name cannot be null or empty.", e.getMessage());
    	}
    }

    // 测试类别名称为 null
    @Test()
    public void testNullDepartmentName() {
    	try {
            new Department(null); // 应抛出异常
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Department name cannot be null or empty.", e.getMessage());
    	}
    }

    // 测试负金额
    @Test()
    public void testDepartmentNegativeAmount() {
    	try {
            Department D = new Department("test");
            D.addExpense("cat", -100);
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Expense amount cannot be negative.", e.getMessage());
    	}
    }

    @Test
    public void testDepartmentInvalidDate() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err; // 保存原来的 System.err

        try {
            System.setErr(new PrintStream(errContent));

            Department department = new Department("test");
            // 使用无效的日期格式
            department.addExpense("1125/J222an/20234", 1000); // 注意年份部分

            // Assert: Check if the error message was printed correctly
            String output = errContent.toString().trim();
            System.out.println("Captured output: " + output); // 添加调试输出
            assertTrue(output.contains("Invalid date format: 1125/J222an/20234"));

        } finally {
            // Restore original System.err
            System.setErr(originalErr);
        }
    }


    
    @Test
    public void testGetTopCategories() {
    	Department D = new Department("test");
        D.addExpense("15/Jan/2024", 1000);
        D.addExpense("16/Jan/2024", 2000);

        List<Category> topCategories = D.getTopCategories();
        assertEquals(3000, D.getYearlyExpense(2024)); // 确保 2024 年的费用为 1000
        assertEquals(3000, D.getTotalExpense()); // 确保总费用为 1000
        assertEquals(2, topCategories.size()); // 应该有两个类别
        assertEquals("16/Jan/2024", topCategories.get(0).getName()); // 应该按金额排序
        assertEquals(2000, topCategories.get(0).getAmount());
    }

    //department test ends
    
    //category test starts

    // 测试类别名称为空
    @Test()
    public void testEmptyCategoryName() {
    	try {
    		new Category("", 100); // 应抛出异常
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Category name cannot be null or empty.", e.getMessage());
    	}
    }

    // 测试类别名称为 null
    @Test()
    public void testNullCategoryName() {
    	try {
            new Category(null, 100); // 应抛出异常
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Category name cannot be null or empty.", e.getMessage());
    	}
    }

    // 测试负金额
    @Test()
    public void testNegativeAmount() {
    	try {
            new Category("Category A", -100); // 应抛出异常
    		fail("Expected IllegalArgumentException not thrown");
    	} catch (IllegalArgumentException e){
    		assertEquals("Amount cannot be negative.", e.getMessage());
    	}
    }

    // 测试正金额
    @Test
    public void testPositiveAmount() {
        Category category = new Category("Category B", 500);
        assertEquals("Category B", category.getName());
        assertEquals(500, category.getAmount());
    }
    
    //category test ends
    
    //product test starts
    
    
    // 测试产品名称为空
    @Test
    public void testProductEmptyProductName() {
        try {
            new Product(""); // 应抛出异常
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product name cannot be null or empty.", e.getMessage());
        }
    }

    // 测试产品名称为 null
    @Test
    public void testNullProductName() {
        try {
            new Product(null); // 应抛出异常
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product name cannot be null or empty.", e.getMessage());
        }
    }

    // 测试负收入
    @Test
    public void testNegativeRevenue() {
        try {
        	Product product = new Product("test");
            product.addRevenue(-100); // 应抛出异常
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Revenue amount cannot be negative.", e.getMessage());
        }
    }

    // 测试零收入
    @Test
    public void testZeroRevenue() {
    	Product product = new Product("test");
        product.addRevenue(0); // 应成功执行
        assertEquals(0, product.getTotalRevenue()); // 总收入应为 0
    }

    // 测试正收入
    @Test
    public void testPositiveRevenue() {
    	Product product = new Product("test");
        product.addRevenue(500); // 应成功执行
        assertEquals(500, product.getTotalRevenue()); // 总收入应为 500
    }
    
    //product test ends

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
    
    @Test
    public void test_getMonthFromExpenseDate() throws IOException {

        try {
        	FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        	reportGenerator.test_getMonthFromExpenseDate("XXX");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid date format: XXX", e.getMessage());
        }
    }
    
    @Test
    public void invalid_date_of_expense() throws IOException {

        try {
        	FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        	reportGenerator.test_textToMonth("XXX");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid month: XXX", e.getMessage());
        }
    }
    
    @Test
    public void test_calculateWeightedAverage() throws IOException {

        try {
        	FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        	List<Long> emptyList = new ArrayList<>();
        	reportGenerator.test_calculateWeightedAverage(emptyList);
        } catch (IllegalArgumentException e) {
            assertEquals("No expenses to calculate average.", e.getMessage());
        }
    }
    
    
    @Test
    public void invalid_format_of_data() throws IOException {
        String testFilePath = "test_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("24/May/2024|Marketing Department|Online Advertisement|1000000|sth\n");
        }

        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        
        // Redirect System.err to capture error output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err; // 保存原来的 System.err

        try {
            System.setErr(new PrintStream(errContent));

            try (BufferedReader br = new BufferedReader(new FileReader(testFilePath))) {
                String line;
                List<Long> expenses = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    reportGenerator.addEntry(line); // 这里触发可能的错误
                    String[] parts = line.split("\\|");
                    if (!parts[1].startsWith("Product")) {
                        expenses.add(Long.parseLong(parts[3].trim()));
                    }
                }
            } catch (IllegalArgumentException e) {
                // 这里可以处理其他异常，但主要是捕获错误信息
                assertEquals("Department name cannot be null or empty.", e.getMessage());
            }

            // Assert: Check if the error message was printed correctly
            String output = errContent.toString().trim();
            assertTrue(output.contains("Invalid line format: 24/May/2024|Marketing Department|Online Advertisement|1000000|sth"));

        } finally {
            // Restore original System.err
            System.setErr(originalErr);
        }
    }
    
    // 测试从文件读取数据（模拟文件）
    @Test
    public void invalid_number_to_trigger_parseAmount() throws IOException {
        String testFilePath = "test_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("24/May/2024|Marketing Department|Online Advertisement|invalidnumber\n");
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
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid amount: invalidnumber", e.getMessage());
        }
    }
    
}