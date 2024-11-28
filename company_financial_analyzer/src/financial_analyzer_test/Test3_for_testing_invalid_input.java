package financial_analyzer_test;

import static org.junit.Assert.*;
import org.junit.Test;

import company_financial_analyzer.FinancialReportGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class Test3_for_testing_invalid_input {
	
	@Test()
	public void ExceptionTest1() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("sth");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid time period format.";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
	@Test()
	public void ExceptionTest2() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("JAN");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid time period format.";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
	@Test()
	public void ExceptionTest3() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("0000");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid time period format.";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
	@Test()
	public void ExceptionTest4() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("Q5/2024");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid time period format.";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
	@Test()
	public void ExceptionTest5() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("XXX/2024");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid month: XXX";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
	@Test()
	public void ExceptionTest6() throws IOException {
	    FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
	    try {
	    	reportGenerator.setTargetDates("XXX/2024-XXX/2024");
	    } catch (IllegalArgumentException e) {
		    String expectedReport = "Invalid month: XXX";
		    assertEquals(expectedReport, e.getMessage());
	    }
	}
	
    @Test
    public void testWeightedAveragePredict_NoExpenses() {
        // 创建一个输出流，用于捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // 重定向 System.out
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // 创建 FinancialReportGenerator 实例并调用方法
        FinancialReportGenerator reportGenerator = new FinancialReportGenerator();
        reportGenerator.weightedAveragePredict(Arrays.asList()); // 传入空列表

        // 恢复 System.out
        System.setOut(originalOut);

        // 获取输出内容
        String output = outputStream.toString().trim();

        // 验证输出内容
        assertEquals("No expense data available for prediction.", output);
    }

}