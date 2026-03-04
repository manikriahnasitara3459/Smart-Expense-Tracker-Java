package com.expense.app;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

public class ExpenseService {
	static boolean saveExpense(String date, String category, double amount) {

        String sql = "INSERT INTO expenses (date, category, amount) VALUES (?, ?, ?)";

      try (Connection con = DButil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {


    	  java.sql.Date sqldate = java.sql.Date.valueOf(date);
  	
  
  ps.setDate(1, sqldate);
  ps.setString(2, category);
  ps.setDouble(3, amount);

  ps.executeUpdate();
  return true;

 } catch (Exception e) {
  e.printStackTrace();
  return false;
}
}

private static void viewByDate(String date) {
  String sql = "SELECT date, category, amount FROM expenses WHERE date = ?";
  double total = 0;

  try (Connection con = DButil.getConnection();
       PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, date);
      ResultSet rs = ps.executeQuery();

      System.out.println("\nDate         Category       Amount");
      System.out.println("-----------------------------------");

      boolean found = false;
      while (rs.next()) {
          found = true;
          System.out.printf("%-12s %-14s %.2f\n",
                  rs.getString("date"),
                  rs.getString("category"),
                  rs.getDouble("amount"));

          total += rs.getDouble("amount");
      }

      if (!found) {
          System.out.println("No expenses found for this date.");
      } else {
          System.out.println("-----------------------------------");
          System.out.println("Total Spent: " + total);
      }

  } catch (Exception e) {
      e.printStackTrace();
  }
}




     static ArrayList<Expense> getAllExpenses() {
  ArrayList<Expense> list = new ArrayList<>();
  String sql = "SELECT date, category, amount FROM expenses";

  try (Connection con = DButil.getConnection();
       Statement stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
          list.add(new Expense(
                  rs.getString("date"),
                  rs.getString("category"),
                  rs.getDouble("amount")
          ));
      }
  } catch (Exception e) {
      e.printStackTrace();
  }
  return list;
}

public static void main(String[] args) {
  Scanner sc = new Scanner(System.in);
  ArrayList<Expense> expenses = getAllExpenses();

  
  String[] categories = {
                      "Food",
                      "Transport",
                      "Rent",
                      "Shopping",
                      "Groceries",
                      "Movie",
                      "Medical",
                      "Internet",
                      "Travel"
  };

  // ArrayList <Expense> expenses  = new ArrayList<> ();

  int choice;
  do{
      System.out.println("\n---Smart Expense Tracker---");
      System.out.println("1.Add your Expense ");
      System.out.println("2.View All Expenses");
      System.out.println("3.Total Expense ");
      System.out.println("4.Highest Expense ");
      System.out.println("5.Exit");
      System.out.println("6. Category-wise Report");
      System.out.println("7. View expenses by Date");
//      System.out.println("8. View expenses by Month");
      System.out.println("Enter your choice: ");

      choice =sc.nextInt();

      switch (choice){
           case 1:
               sc.nextLine(); // clear buffer

              System.out.print("Enter date (yyyy-mm-dd): ");
              String date = sc.nextLine();

              System.out.println("Select Category:");
              for (int i = 0; i < categories.length; i++) {
                   System.out.println((i + 1) + ". " + categories[i]);
               }

      System.out.print("Enter category number: ");
      int catChoice = sc.nextInt();

      if (catChoice < 1 || catChoice > categories.length) {
          System.out.println("❌ Invalid category selected!");
          break;
      }

      String category = categories[catChoice - 1];

      System.out.print("Enter amount: ");
      double amount = sc.nextDouble();
      // saveExpense(date,category,amount);

      boolean saved = saveExpense(date,category,amount);

      if(saved){
          System.out.println("✅ Expense added successfully!");
      }
      else{
          System.out.println("❌ Failed to save expense!");
      }

      break;
      
  case 2:
  	 expenses = getAllExpenses();
     
       if (expenses.isEmpty()) {
          System.out.println("No expenses to found.");
      } else {
          for (Expense e : expenses) {
      System.out.println(e.date + " | " + e.category + " | ₹" + e.amount);
     }
    }
  break;

  
  case 3:
      expenses=getAllExpenses();
      
      double total = 0;
      for (Expense e : expenses) {
          total += e.amount;
      }
      System.out.println("💰 Total Expense: ₹" + total);
      break;

  case 4:
      expenses=getAllExpenses();
      if (expenses.isEmpty()) {
          System.out.println("No expenses available.");
      } else {
          Expense highest = expenses.get(0);
          for (Expense e : expenses) {
              if (e.amount > highest.amount) {
                  highest = e;
              }
          }
          System.out.println("📈 Highest Expense:");
          System.out.println(highest.date + " | " + highest.category + " | ₹" + highest.amount);
      }
      break;

  case 5:
      System.out.println("👋 Exiting... Thank you!");
      break;
  case 6:
      expenses=getAllExpenses();
      
      if (expenses.isEmpty()) {
      		System.out.println("No expenses available.");
      		break;
      }
  
   HashMap<String, Double> map = new HashMap<>();

   	for (Expense e : expenses) {

   			map.put(
   					e.category,
   				map.getOrDefault(e.category, 0.0) + e.amount
   			);
   	}

		System.out.println("--- Category-wise Expense Report ---");
			for (String cat :map.keySet()) {
					System.out.println(cat + " : ₹" + map.get(cat));
			}
			break;
			
  case 7:
  	sc.nextLine();
      System.out.print("Enter date (dd-mm-yyyy): ");
      String searchDate = sc.nextLine();
      viewByDate(searchDate);
      break;

          default:
              System.out.println("❌ Invalid choice");
      }
 }

   while (choice != 5);

  sc.close();


   }
}
