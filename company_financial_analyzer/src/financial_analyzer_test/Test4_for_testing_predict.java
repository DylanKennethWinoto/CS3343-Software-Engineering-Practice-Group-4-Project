package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class Test4_for_testing_predict {

    @Test
    public void testWeightedAveragePredict_NoExpenses() {
        // 创建一个输出流，用于捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        reportGenerator.weightedAveragePredict(Arrays.asList()); // 传入空列表

        // 恢复 System.out
        System.setOut(originalOut);

        // 获取输出内容
        String output = outputStream.toString().trim();
        assertEquals("No expense data available for prediction.", output);
    }

    @Test
    public void testWeightedAveragePredict_WithExpenses() {
        // 创建一个输出流，用于捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        
        // 模拟输入，添加日期和开销
        reportGenerator.setTargetDates("May/2024");
        reportGenerator.addEntry("01/May/2024|Marketing Department|Advertisement|1000");
        reportGenerator.addEntry("15/May/2024|Marketing Department|Salary|2000");
        reportGenerator.addEntry("20/May/2024|Marketing Department|Office Supplies|3000");

        // 调用预测方法
        reportGenerator.weightedAveragePredict(Arrays.asList(1000L, 2000L, 3000L));

        // 恢复 System.out
        System.setOut(originalOut);

        // 获取输出内容
        String output = outputStream.toString().trim();

        // 计算预期预测值
        double expectedAverage = (1000 * 1 + 2000 * 2 + 3000 * 3) / (1 + 2 + 3); // 权重是1, 2, 3
        String expectedOutput = String.format("Predicted future expense for the year: %.2f", expectedAverage * 12);

        // 验证输出内容
        assertTrue(output.contains("Calculating weighted average prediction..."));
        assertTrue(output.contains("Expense 1: 1000"));
        assertTrue(output.contains("Expense 2: 2000"));
        assertTrue(output.contains("Expense 3: 3000"));
    }
}