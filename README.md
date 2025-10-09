<p align="center">
  <img src="src/main/resources/assets/wallieLogo.png" alt="Wallie Logo" width="400">
</p>

# Wallie

**Wallie** is an AI-powered **budget calculator and expense manager** designed to help you understand, optimize, and take control of your finances - effortlessly.

Whether you're tracking daily spending, planning future savings, or adjusting to life changes, Wallie uses artificial intelligence to make budgeting smarter, not harder.

---

## ✨ Features

- 🤖 **AI-Powered Budget Insights** - Let Wallie analyze your habits and suggest realistic budgets.
- 📊 **Automatic Categorization** - Wallie sorts your expenses intelligently with the use of Ai.
- 🔐 **Privacy-First** - Your data stays local; no cloud syncing or tracking.

---
! IMPORTANT !

For now wallie works locally and every data is being stored in your machine so in order for the app to work you need to follow the steps below and you're good to go!

Step 1:
Download and install Intellij IDEA Community Edition https://www.jetbrains.com/idea/download/?section=windows

Step 2:
Download and install mySQL (choose the MSI Installer) following the wizard's steps. At some point you will need to create a password that will be needed for a following step https://dev.mysql.com/downloads/mysql/

Step 3:
Open MySQL Workbench 8.0 CE, open the Local Instance MySQL## and provide the password that you created in the step before. Next you need to copy the script below and paste it inside the tab that opened. Run it using the "thunder icon". Finally right click and refresh at the left part of the screen in order for the database to appear.

CREATE DATABASE IF NOT EXISTS wallie;

USE wallie;

CREATE TABLE IF NOT EXISTS Users(
    userId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    preferredCurrency INT NOT NULL,
    profileImg INT NOT NULL
);

CREATE TABLE IF NOT EXISTS BudgetCategories(
    categoryId INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO BudgetCategories (categoryId, name) VALUES
(1, 'Health'),
(2, 'Home'),
(3, 'Leisure'),
(4, 'Shopping'),
(5, 'Transport'),
(6, 'Other');

CREATE TABLE IF NOT EXISTS Budgets(
    budgetId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    totalAmount DOUBLE NOT NULL DEFAULT 0,
    totalAmountSpent DOUBLE NOT NULL DEFAULT 0,
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BudgetCategoryAmounts(
    budgetId INT NOT NULL,
    categoryId INT NOT NULL,
    amount DOUBLE NOT NULL DEFAULT 0,
    `limit` DOUBLE NOT NULL,
    PRIMARY KEY (budgetId, categoryId),
    FOREIGN KEY (budgetId) REFERENCES Budgets(budgetId) ON DELETE CASCADE,
    FOREIGN KEY (categoryId) REFERENCES BudgetCategories(categoryId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PaymentHistory(
    paymentId INT AUTO_INCREMENT PRIMARY KEY,
    budgetId INT NOT NULL,
    userId INT NOT NULL,
    categoryId INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    paymentDate DATE NOT NULL,
    FOREIGN KEY (budgetId) REFERENCES Budgets(budgetId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE,
    FOREIGN KEY (categoryId) REFERENCES BudgetCategories(categoryId) ON DELETE CASCADE
); 

Step 4:
Open the file .env and fill in the field DB_PASSWORD with the password you created at step 3. Next you will need to drag the file inside the project.

Step:
Open the project in Intellij, press on the settings icon in top right corner, and head for Project Structure > Project and in the field SDK open the list and press Download JDK. Then choose JDK corretto-23 Amazon Corretto 23.0.2 and press apply. Next head for Modules > Dependancies and press the + button right down from the Module SDK. Then press JARs or Directories and choose one of the file inside the lib file provided and repeat this process for every file in the lib.
