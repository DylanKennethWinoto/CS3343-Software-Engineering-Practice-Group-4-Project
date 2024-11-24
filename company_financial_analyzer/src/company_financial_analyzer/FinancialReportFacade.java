package company_financial_analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FinancialReportFacade {
    private FinancialReportGenerator reportGenerator;

    public FinancialReportFacade() {
        this.reportGenerator = new FinancialReportGenerator();
    }

    public boolean setTargetDates(String timePeriod) {
        return reportGenerator.setTargetDates(timePeriod);
    }

    public void processReport(String filePath, String timePeriod) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Long> expenses = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                String date = parts[0];
                if (reportGenerator.isDateInTarget(date)) {
                    reportGenerator.addEntry(line);
                    if (!parts[1].startsWith("Product")) {
                        expenses.add(Long.parseLong(parts[3].trim()));
                    }
                }
            }

            reportGenerator.generateReport(timePeriod);
            if (reportGenerator.getTargetDates().size() == 1 && reportGenerator.getTargetDates().get(0).matches("\\d{4}")) {
                reportGenerator.weightedAveragePredict(expenses);
            }
        }
    }
}