package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportFacade;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Test6_for_facade {

    @Test
    public void testSetTargetDates() {
        // Arrange
        FinancialReportFacade facade = new FinancialReportFacade();
        String timePeriod = "2024"; // 测试年份

        // Act
        boolean result = facade.setTargetDates(timePeriod);

        // Assert
        assertTrue("Expected setTargetDates to return true for valid timePeriod", result);
    }

    @Test
    public void testProcessReport_ValidInput() throws IOException {
        // Arrange
        FinancialReportFacade facade = new FinancialReportFacade();
        String timePeriod = "Jan/2024"; // 设置目标日期
        facade.setTargetDates(timePeriod);

        // 创建一个临时文件来存储测试数据
        String testFilePath = "test_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            // 写入符合格式的测试数据
            writer.write("01/Feb/2024|Product A|Sales|2000\n");
            writer.write("01/Jan/2024|Marketing Department|Online Advertisement|1000\n");
            writer.write("01/Mar/2024|HR Department|Salary|1500\n");
            writer.write("InvalidLineFormat\n"); // 不符合格式的行
        }

        // Act
        facade.processReport(testFilePath, timePeriod);

        // Assert
        // 这里可以根据你的实现，检查 reportGenerator 的状态或生成的报告内容
        // 例如，你可以检查总收入、总支出等
    }

    @Test
    public void testProcessReport_InvalidLine() throws IOException {
        // Arrange
        FinancialReportFacade facade = new FinancialReportFacade();
        String timePeriod = "2024"; // 设置目标日期
        facade.setTargetDates(timePeriod);

        // 创建一个临时文件来存储测试数据
        String testFilePath = "test_invalid_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            // 写入不符合格式的测试数据
            writer.write("InvalidLineFormat\n");
        }

        // Act
        facade.processReport(testFilePath, timePeriod);

        // Assert
        // 检查是否能处理无效行，可能需要检查控制台输出或其他状态
    }
    
    @Test
    public void testProcessReport_with_product() throws IOException {
        // Arrange
        FinancialReportFacade facade = new FinancialReportFacade();
        String timePeriod = "2024"; // 设置目标日期
        facade.setTargetDates(timePeriod);
        String testFilePath = "test_with_product_report.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("01/Jan/2024|Product A|Online Advertisement|1000\n");
            writer.write("01/Feb/2024|Sales Department|Sales|2000\n");
            writer.write("01/Mar/2024|HR Department|Salary|1500\n");
        }

        facade.processReport(testFilePath, timePeriod);
    }
}