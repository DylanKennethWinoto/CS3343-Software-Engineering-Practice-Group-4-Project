package company_financial_analyzer;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class FinancialAnalyzerGUI extends JFrame {
    private static List<User> users = new ArrayList<>();
    private JTextField filePathField;
    private JTextArea resultArea;
    private JComboBox<String> timePeriodType;
    private JTextField customTimePeriod;
    private JButton analyzeButton;
    private FinancialReportGenerator reportGenerator;
    private static final String USER_DATA_FILE = "users.dat";
    private static final Color THEME_COLOR = new Color(51, 153, 255);
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 12);

    public FinancialAnalyzerGUI() {
        reportGenerator = new FinancialReportGenerator();
        initializeUI();
        setupTheme();
    }

    private void setupTheme() {
        try {
            UIManager.put("Button.background", THEME_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Panel.background", new Color(240, 240, 240));
            UIManager.put("Label.font", MAIN_FONT);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setTitle("Company Financial Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Add main components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JPanel filePanel = createFilePanel();
        JPanel timePeriodPanel = createTimePeriodPanel();
        createResultArea();

        mainPanel.add(filePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(timePeriodPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Create control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        analyzeButton = createStyledButton("Generate Report");
        analyzeButton.addActionListener(e -> generateReport());
        controlPanel.add(analyzeButton);

        // Add all components to frame
        add(mainPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Set frame properties
        pack();
        setSize(900, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(THEME_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private JPanel createFilePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("File Selection"));

        filePathField = new JTextField(40);
        filePathField.setFont(MAIN_FONT);
        
        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // Add TXT file filter
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return "Text Files (*.txt)";
                }
            });
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(new JLabel("File Path:"));
        panel.add(filePathField);
        panel.add(browseButton);

        return panel;
    }

    private JPanel createTimePeriodPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Time Period Selection"));

        String[] periods = {"Select Type", "Year", "Quarter", "Month", "Custom Range"};
        timePeriodType = new JComboBox<>(periods);
        customTimePeriod = new JTextField(15);
        customTimePeriod.setFont(MAIN_FONT);

        timePeriodType.addActionListener(e -> updateTimePeriodField());

        panel.add(new JLabel("Time Period:"));
        panel.add(timePeriodType);
        panel.add(customTimePeriod);

        return panel;
    }

    private void updateTimePeriodField() {
        String selected = (String) timePeriodType.getSelectedItem();
        customTimePeriod.setEnabled(!"Select Type".equals(selected));
        
        switch (selected) {
            case "Year":
                customTimePeriod.setToolTipText("Enter year (e.g., 2024)");
                break;
            case "Quarter":
                customTimePeriod.setToolTipText("Enter quarter (e.g., Q1/2024)");
                break;
            case "Month":
                customTimePeriod.setToolTipText("Enter month (e.g., Jan/2024)");
                break;
            case "Custom Range":
                customTimePeriod.setToolTipText("Enter range (e.g., Jan/2024-Mar/2024)");
                break;
            default:
                customTimePeriod.setToolTipText("");
                break;
        }
    }

    private void createResultArea() {
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setBackground(new Color(252, 252, 252));
    }

    private void generateReport() {
        String filePath = filePathField.getText().trim();
        String timePeriod = customTimePeriod.getText().trim();

        if (filePath.isEmpty() || timePeriod.isEmpty()) {
            showError("Please select a file and enter a time period");
            return;
        }

        // Validate file extension
        if (!filePath.toLowerCase().endsWith(".txt")) {
            showError("Please select a valid .txt file");
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            showError("Selected file does not exist");
            return;
        }

        try {
            processReport(filePath, timePeriod);
        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            showError("Error generating report: " + e.getMessage());
        }
    }

    private void processReport(String filePath, String timePeriod) throws IOException {
        resultArea.setText("");
        if (!reportGenerator.setTargetDates(timePeriod)) {
            showError("Invalid time period format");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<Long> expenses = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                processLine(line, expenses);
            }

            String report = reportGenerator.generateReport(timePeriod);
            resultArea.append(report);

            if (timePeriod.matches("\\d{4}")) {
                reportGenerator.weightedAveragePredict(expenses);
            }
        }
    }

    private void processLine(String line, List<Long> expenses) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            resultArea.append("Invalid line format: " + line + "\n");
            return;
        }

        String date = parts[0];
        if (reportGenerator.isDateInTarget(date)) {
            reportGenerator.addEntry(line);
            if (!parts[1].startsWith("Product")) {
                expenses.add(Long.parseLong(parts[3].trim()));
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(THEME_COLOR, 2));

        JLabel title = new JLabel("Financial Analyzer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(THEME_COLOR);
        content.add(title, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(THEME_COLOR);
        content.add(progressBar, BorderLayout.SOUTH);

        splash.setContentPane(content);
        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        Timer timer = new Timer(3000, e -> {
            splash.dispose();
            showLoginDialog();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private static void showLoginDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Login");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        loginButton.setBackground(THEME_COLOR);
        loginButton.setForeground(Color.WHITE);
        signUpButton.setBackground(THEME_COLOR);
        signUpButton.setForeground(Color.WHITE);

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        loginButton.addActionListener(e -> handleLogin(dialog, usernameField.getText(),
                new String(passwordField.getPassword())));
        signUpButton.addActionListener(e -> {
            dialog.dispose();
            showSignUpDialog();
        });

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void handleLogin(JDialog dialog, String username, String password) {
        if (isValidLogin(username, password)) {
            dialog.dispose();
            SwingUtilities.invokeLater(() -> new FinancialAnalyzerGUI().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(dialog, "Invalid credentials.", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showSignUpDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Sign Up");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField newUsernameField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();

        inputPanel.add(new JLabel("New Username:"));
        inputPanel.add(newUsernameField);
        inputPanel.add(new JLabel("New Password:"));
        inputPanel.add(newPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton signUpButton = new JButton("Create Account");
        JButton cancelButton = new JButton("Cancel");

        signUpButton.setBackground(THEME_COLOR);
        signUpButton.setForeground(Color.WHITE);
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);

        signUpButton.addActionListener(e -> handleSignUp(dialog, 
            newUsernameField.getText(), new String(newPasswordField.getPassword())));
        cancelButton.addActionListener(e -> {
            dialog.dispose();
            showLoginDialog();
        });

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void handleSignUp(JDialog dialog, String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Username and password cannot be empty.", 
                "Sign Up Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(dialog,
                "Username already exists. Please choose a different one.",
                "Sign Up Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        users.add(new User(username, password));
        saveUsers();
        
        JOptionPane.showMessageDialog(dialog,
            "Sign up successful! You can now log in.",
            "Sign Up",
            JOptionPane.INFORMATION_MESSAGE);
        
        dialog.dispose();
        showLoginDialog();
    }

    private static boolean isValidLogin(String username, String password) {
        return users.stream()
            .anyMatch(user -> user.getUsername().equals(username) 
                && user.getPassword().equals(password));
    }

    private static boolean isUsernameTaken(String username) {
        return users.stream()
            .anyMatch(user -> user.getUsername().equals(username));
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadUsers() {
        File userFile = new File(USER_DATA_FILE);
        if (userFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(userFile))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123"));
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        loadUsers();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveUsers();
        }));

        SwingUtilities.invokeLater(() -> {
            try {
                showSplashScreen();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
