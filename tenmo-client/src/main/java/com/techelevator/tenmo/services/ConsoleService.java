package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.UserCredentials;


import java.math.BigDecimal;
import java.util.Scanner;


public class ConsoleService {
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String ANSI_BRIGHT_WHITE  = "\u001B[97m";
    public static final String WHITE_BOLD = "\033[1;37m";
    public static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";




    private final Scanner scanner = new Scanner(System.in);
    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(PURPLE_BRIGHT + prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        //System.out.println(AnsiOutput.encode(AnsiBackground.MAGENTA));
        System.out.println(PURPLE_BRIGHT + "⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇");
        System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "* Welcome to TEnmo! *");
        System.out.println(PURPLE_BRIGHT + "⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇");
    }

    public void printLoginMenu() {
        System.out.println(PURPLE_BRIGHT);
        System.out.println(PURPLE_BRIGHT + "1: Register");
        System.out.println(PURPLE_BRIGHT + "2: Login");
        System.out.println(PURPLE_BRIGHT + "0: Exit");
        System.out.println(PURPLE_BRIGHT);
    }

    public void printMainMenu() {
        System.out.println(PURPLE_BRIGHT );
        System.out.println(PURPLE_BRIGHT + "1: View your current balance");
        System.out.println(PURPLE_BRIGHT + "2: View your past transfers");
        System.out.println(PURPLE_BRIGHT + "3: View your pending requests");
        System.out.println(PURPLE_BRIGHT + "4: Send money");
        System.out.println(PURPLE_BRIGHT + "5: Request money");
        System.out.println(PURPLE_BRIGHT + "0: Exit");
        System.out.println(PURPLE_BRIGHT);
    }

    public void printPendingMenu(){
        System.out.println(PURPLE_BRIGHT);
        System.out.println(PURPLE_BRIGHT + "1: Approve");
        System.out.println(PURPLE_BRIGHT + "2: Reject");
        System.out.println(PURPLE_BRIGHT + "0: Don't approve or reject");
        bar();
        System.out.println(PURPLE_BRIGHT);
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString(PURPLE_BRIGHT + "Username: ");
        String password = promptForString(PURPLE_BRIGHT + "Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(PURPLE_BRIGHT + prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(PURPLE_BRIGHT + prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(PURPLE_BRIGHT + "Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(PURPLE_BRIGHT + prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println(PURPLE_BRIGHT + "\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void bar(){
        System.out.println(PURPLE_BRIGHT + "⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇⊆⊇");
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}