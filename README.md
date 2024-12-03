Group 4 - Company Financial Analyzer

Short Description:
This program is an application that can automatically track an entire company's department expenses and product revenues in a given time frame. Users can either choose a monthly report, a quarter report, an annual report, or really just about any period that the user likes as long as there is data during that period.

Program Installation:
1. Install the latest Java SE Runtime Environment (http://www.oracle.com/technetwork/java/javase/downloads/index.html). (Ignore this step if you already have this installed)
2. Double Click the exe file.

Project Usage:
1. Upon launching, the program will prompt for user login credentials.
2. User Authentication. The application features a login system. Users must enter a valid username and password to access the financial analysis features.
New users can sign up if they don’t already have an account.
3. After opening the program, the message will send the following message: "Welcome to Company's Financial Analyzer, please input your text report: "

Inputting the correct format:
- Make Sure the report is in .txt file
- Make Sure the report follows the exact formatting categories (Date (DD/MM/YYYY)|Department Name/Product Name|Category|Total Expense), an example is provided below:

24/May/2024|Marketing Department|Online Advertisement|1000000
24/May/2024|Accounting|Software License Renewal|1000000
25/May/2024|Technology Department|Server Maintenance|1000000				
26/May/2024|Marketing Department|Salary|100000	
26/May/2024|Product X|Revenue*|10000000
27/May/2024|Marketing Department|Online Advertisement|2000000

*Revenue Category is a unique keyword that should only be used in Products and not Departments.

Our system can also detect various mistakes that will help you fix the input as quickly as possible. Here are a few mistakes that our program can detect:
-Wrong date input (System will give the message: "DD-MM-YYYY is not a valid date")
-Putting "Revenue" as a category in the Department sector (System will give the message: "Revenue is not a valid category for department")
-Putting anything that is not "Revenue" as a category in the Product sector (System will give the message: "[Category] is not a valid category for department")

2. After successfully inputting the text file, the message will send the following message: "Input successful, please select your time period: "
-When selecting a time period that is in YYYY format, a prediction regarding the next year cost will be made.
-If the input format is not YYYY format, a report without prediction will be made.
-Using the example from above,

Please enter the path to your report.txt file:
src/data/data_for_readme.txt
MM/YYYY (e.g., Jan/2024)
Q1/YYYY (e.g., Q1/2024)
YYYY (e.g., 2024)
MM/YYYY-MM/YYYY (e.g., Jan/2024-Mar/2024)
please select your time period:
2024
2024 Financial Report

Marketing Department Total Expense = 3100000 (60.8%)
Marketing Department Top 3 Categories:
1. Online Advertisement - 3000000
2. Salary - 100000
3. N/A

Accounting Total Expense = 1000000 (19.6%)
Accounting Top 3 Categories:
1. Software License Renewal - 1000000
2. N/A
3. N/A

Technology Department Total Expense = 1000000 (19.6%)
Technology Department Top 3 Categories:
1. Server Maintenance - 1000000
2. N/A
3. N/A

Product X Total Revenue = 10000000
Total profit = 4900000

Calculating weighted average prediction...

Predicted future expense for the year: 12240000.00

Would you like to generate another financial report (Y/N)?
Y
Please enter the path to your report.txt file:
src/data/data_for_readme.txt
MM/YYYY (e.g., Jan/2024)
Q1/YYYY (e.g., Q1/2024)
YYYY (e.g., 2024)
MM/YYYY-MM/YYYY (e.g., Jan/2024-Mar/2024)
please select your time period:
May/2024
May/2024 Financial Report

Marketing Department Total Expense = 3100000 (60.8%)
Marketing Department Top 3 Categories:
1. Online Advertisement - 3000000
2. Salary - 100000
3. N/A

Accounting Total Expense = 1000000 (19.6%)
Accounting Top 3 Categories:
1. Software License Renewal - 1000000
2. N/A
3. N/A

Technology Department Total Expense = 1000000 (19.6%)
Technology Department Top 3 Categories:
1. Server Maintenance - 1000000
2. N/A
3. N/A

Product X Total Revenue = 10000000
Total profit = 4900000

Would you like to generate another financial report (Y/N)?
N
Thank you for using Company's Financial Analyzer, Goodbye.

**If Total Expense is greater than Total Revenue, the line will change to Total Loss = XXXXX
4. If the user chooses Y, the program will go back to step 2. If the user choose N, The program will display the following message: "Thank you for using Company's Financial Analyzer, Goodbye."

