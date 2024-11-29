package financial_analyzer_test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

import company_financial_analyzer.mainFunction;

public class Test7_for_mainFuntion {
	
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

	
    @Test
    public void everything_is_right() throws IOException {
    	
        // 模拟用户输入
        String simulatedInput = "src/data/data2.txt\nJan/2024-Jun/2024\nN\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        

        // 捕获 System.out 输出
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        PrintStream printStream = new PrintStream(outContent);
        
        System.setOut(printStream);

        


        // 调用 main 方法
        mainFunction.main(new String[]{});
        
        System.setOut(originalOut);
        
        String output = outContent.toString();
        System.out.println("Captured Output: " + output);  // 打印捕获的输出以调试


        // 检查输出内容是否符合预期
        assertTrue(output.contains("Marketing Department Total Expense = 3600000 (65.5%)"));
        assertTrue(output.contains("Technology Department Total Expense = 1200000 (21.8%)"));
        assertTrue(output.contains("Accounting Total Expense = 700000 (12.7%)"));
        assertTrue(output.contains("Total profit = 19500000"));


        // 关闭 PrintStream，避免资源泄露
        printStream.close();
    }
    
    @Test
    public void everything_is_right_loop() throws IOException {
    	
        // 模拟用户输入
        String simulatedInput = "src/data/data1.txt\nMay/2024\nY\nsrc/data/data1.txt\nMay/2024\nN\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        

        // 捕获 System.out 输出
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        PrintStream printStream = new PrintStream(outContent);
        
        System.setOut(printStream);

        


        // 调用 main 方法
        mainFunction.main(new String[]{});
        
        System.setOut(originalOut);
        
        String output = outContent.toString();
        System.out.println("Captured Output: " + output);  // 打印捕获的输出以调试


        // 检查输出内容是否符合预期
        assertTrue(output.contains("Marketing Department Total Expense = 3100000 (60.8%)"));
        assertTrue(output.contains("Technology Department Total Expense = 1000000 (19.6%)"));
        assertTrue(output.contains("Accounting Total Expense = 1000000 (19.6%)"));
        assertTrue(output.contains("Total profit = 4900000"));


        // 关闭 PrintStream，避免资源泄露
        printStream.close();
    }
    
    @Test
    public void wrong_filepath() throws IOException {
    	
        // 模拟用户输入
        String simulatedInput = "src/wrong_path.txt\nMay/2024\nN\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        

        // 捕获 System.out 输出
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        PrintStream printStream = new PrintStream(outContent);
        
        System.setOut(printStream);

        


        // 调用 main 方法
        mainFunction.main(new String[]{});
        
        System.setOut(originalOut);
        
        String output = outContent.toString();
        System.out.println("Captured Output: " + output);  // 打印捕获的输出以调试


        // 检查输出内容是否符合预期
        assertTrue(output.contains("An error occurred while reading the file."));



        // 关闭 PrintStream，避免资源泄露
        printStream.close();
    }

}