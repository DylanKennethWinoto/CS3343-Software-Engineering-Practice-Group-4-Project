package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class Test4_for_testing_predict {
    @Test
    public void testWeightedAveragePredict_WithExpenses() {
        // 创建一个输出流，用于捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // 重定向 System.out
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // 创建 FinancialReportGenerator 实例并调用方法
        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        reportGenerator.weightedAveragePredict(Arrays.asList(100L, 200L, 300L)); // 传入一些开销数据

        // 恢复 System.out
        System.setOut(originalOut);

        // 获取输出内容
        String output = outputStream.toString().trim();

        // 计算预期预测值
        double expectedPrediction = (100 * 1 + 200 * 2 + 300 * 3) / 6.0; // 权重是 1, 2, 3，总和是 6
        String expectedOutput = String.format("Predicted future expense for the year: %.2f", expectedPrediction);

        // 验证输出内容
        assertEquals(expectedOutput, output);
    }
}