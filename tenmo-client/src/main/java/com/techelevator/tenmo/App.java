package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;
public class App {
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final TenmoService tenmoService = new TenmoService();
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String ANSI_BRIGHT_WHITE  = "\u001B[97m";
    public static final String WHITE_BOLD = "\033[1;37m";
    public static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection(PURPLE_BRIGHT + "Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println(PURPLE_BRIGHT + "Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println(PURPLE_BRIGHT + "Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println(PURPLE_BRIGHT + "Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        tenmoService.setAuthToken(currentUser.getToken());

    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection(PURPLE_BRIGHT + "Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println(PURPLE_BRIGHT + "Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //After user logs in balance will show using the getAccount balance method from tenmoService class
	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println(PURPLE_BRIGHT + "Your current balance is: $" + tenmoService.getAccountBalance());

    }

    // shows list of past transfers made by user
	private void viewTransferHistory() {
        boolean running = true;
        while (running) {
            Transfer[] listOfTransfers = tenmoService.getAllTransfers();
            consoleService.bar();
            System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "    *Transfer History*");
            System.out.println(PURPLE_BRIGHT + "ID \t\tFrom/To \tAmount");
            consoleService.bar();
            long currentAccountId = tenmoService.getAccountById(currentUser.getUser().getId()).getAccountId();
            for (Transfer transfer : listOfTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                String usernameFrom = tenmoService.username(transfer.getAccountFrom());
                if (transfer.getAccountFrom() == currentAccountId) {
                    System.out.println(PURPLE_BRIGHT + transfer.getTransferId() + "\tTo: " + usernameTo + "\t" + transfer.getAmount());
                } else if (transfer.getAccountTo() == currentAccountId) {
                    System.out.println(PURPLE_BRIGHT + transfer.getTransferId() + "\tFrom: " + usernameFrom + "\t" + transfer.getAmount());
                }

            }
            //once transfers are listed, this allows you to enter transfer Id and see all details of the transfer and how it processed
            consoleService.bar();
            int input = consoleService.promptForInt(PURPLE_BRIGHT + "\nPlease enter transfer ID to view (0 to cancel): ");
            if (input == 0) {
                running = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println(PURPLE_BRIGHT + "\nInvalid Selection. Try Again.");
                    consoleService.pause();
                } else {
                    consoleService.bar();
                    System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "   *Transfer Details*");
                    consoleService.bar();
                    System.out.println(PURPLE_BRIGHT + "Status: " + transfer.getTransferStatusDesc());
                    System.out.println(PURPLE_BRIGHT + "Amount: $" + transfer.getAmount());
                    System.out.println(PURPLE_BRIGHT + "ID:" + transfer.getTransferId());
                    System.out.println(PURPLE_BRIGHT + "From: " + tenmoService.username(transfer.getAccountFrom()));
                    System.out.println(PURPLE_BRIGHT + "To: " + tenmoService.username(transfer.getAccountTo()));
                    System.out.println(PURPLE_BRIGHT + "Type: " + transfer.getTransferTypeDesc());
                    consoleService.bar();

                    consoleService.pause();

                }

            }
        }
    }

    //shows transfer requests
	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        boolean running = true;
        while (running) {
            consoleService.bar();
            System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "  *Pending Transfers*");
            System.out.println(PURPLE_BRIGHT + "ID \t\tTo \t\t\tAmount");
            consoleService.bar();

            Transfer[] pendingTransfers = tenmoService.getAllPendingTransfers();
            for (Transfer transfer : pendingTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                System.out.println(PURPLE_BRIGHT + transfer.getTransferId() + "\t" + usernameTo + "\t" + transfer.getAmount());

            }
            consoleService.bar();

            long input = consoleService.promptForInt(PURPLE_BRIGHT + "\nPlease enter transfer ID to approve/reject (0 to cancel): ");
            if (input == 0) {
                running = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println(PURPLE_BRIGHT + "\nInvalid Selection. Try Again.");
                    consoleService.pause();
                } else {
                    viewPendingOptions(input);
                }
            }
        }

	}
    // shows pending transfers with their transfer status
    private void viewPendingOptions(long id) {
        Transfer transfer = tenmoService.getTransferById(id);
        BigDecimal amount = transfer.getAmount();
        long accountToId = transfer.getAccountTo();
        long userToId = tenmoService.getUserIdByAccountId(accountToId);
        System.out.println();
        consoleService.bar();
        consoleService.bar();
        System.out.println(PURPLE_BRIGHT + "ID:" + transfer.getTransferId());
        System.out.println(PURPLE_BRIGHT + "From: " + tenmoService.username(transfer.getAccountFrom()));
        System.out.println(PURPLE_BRIGHT + "To: " + tenmoService.username(transfer.getAccountTo()));
        System.out.println(PURPLE_BRIGHT + "Type: " + transfer.getTransferTypeDesc());
        System.out.println(PURPLE_BRIGHT + "Status: " + transfer.getTransferStatusDesc());
        System.out.println(PURPLE_BRIGHT + "Amount: $" + transfer.getAmount());
        consoleService.bar();
        consoleService.bar();
        boolean running = true;
        while (running) {
            consoleService.printPendingMenu();
            int input = consoleService.promptForInt(PURPLE_BRIGHT + "Choose an option: ");
            if (input == 1) {
                if (tenmoService.acceptRequest(id, userToId, amount)) {
                    System.out.println(PURPLE_BRIGHT + "Request Accepted.");
                    running = false;
                } else {
                    System.out.println(PURPLE_BRIGHT + "Request Denied. You lack the required funds.");
                    running = false;
                }
            }
            if (input == 2) {
                if (tenmoService.rejectRequest(id, userToId, amount)) {
                    System.out.println(PURPLE_BRIGHT + "Request Denied.");
                    running = false;
                }
            } else if (input == 0) {
                running = false;
            }
        }
    }
   // lists users to choose from in order to send funds
    private void sendBucks() {
		// TODO Auto-generated method stub

        boolean running = true;
        Transfer transfer = null;
        while (running) {
            System.out.println(PURPLE_BRIGHT + "Users ID\t" + "Name");
            consoleService.bar();
            User[] listUsers = tenmoService.getAllUsers();
            for (User user : listUsers) {
                System.out.println(PURPLE_BRIGHT + user.getId() + "\t\t" + user.getUsername());
            }
            consoleService.bar();

            int id = consoleService.promptForInt(PURPLE_BRIGHT + "Enter the ID of user you are sending to (0 to cancel): ");
            if (id == 0) {
                running = false;
            }
            // if amount is invalid of userId is invalid, will receive error message
            else if (tenmoService.getAccountById(id) == null) {
                System.out.println(PURPLE_BRIGHT + "\nInvalid Selection. Try again.");
                consoleService.pause();
                // if user inputs valid info, transfer will be successful
            } else {
                BigDecimal amount = consoleService.promptForBigDecimal(PURPLE_BRIGHT + "Enter amount: ");
                transfer = tenmoService.makeTransfer(id, amount);
                consoleService.bar();
                System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "  *Money Sent!*");
                consoleService.bar();
                System.out.println(PURPLE_BRIGHT + transfer);
                consoleService.bar();
                running = false;
            }
        }
    }
    // Lists users to choose from in order to request cash money. All money request go into a pending status until the other user approves
    // or denies the amount requested! Once request is made it instantly shows pending request details.
	private void requestBucks() {
        // TODO Auto-generated method stub
        boolean running = true;
        Transfer transfer = null;
        while (running) {
            System.out.println(PURPLE_BRIGHT + "Users ID\t" + "Name");
            consoleService.bar();
            User[] listUsers = tenmoService.getAllUsers();
            for (User user : listUsers) {
                System.out.println(PURPLE_BRIGHT + user.getId() + "\t\t" + user.getUsername());
            }
            consoleService.bar();


            int id = consoleService.promptForInt(PURPLE_BRIGHT + "Enter the ID of user you are requesting from (0 to cancel): ");
            if (id == 0) {
                running = false;
            }
            else if (tenmoService.getAccountById(id) == null) {
                System.out.println(PURPLE_BRIGHT + "\nInvalid Selection. Try again.");
                consoleService.pause();
            } else {
                BigDecimal amount = consoleService.promptForBigDecimal(PURPLE_BRIGHT + "Enter amount: ");
                transfer = tenmoService.makeRequest(id, amount);
                consoleService.bar();
                System.out.println(ANSI_BRIGHT_BG_PURPLE + WHITE_BOLD + ANSI_BRIGHT_WHITE + "  *Request Complete!*");
                consoleService.bar();
                System.out.println(PURPLE_BRIGHT + transfer);
                running = false;
            }
        }
    }

   }
