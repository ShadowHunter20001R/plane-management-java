import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneManagement {
    private static final int ROWS_A_D = 2; // Rows A and D have 14 seats each
    private static final int ROWS_B_C = 2; // Rows B and C have 12 seats each
    private static final int SEATS_PER_ROW_A_D = 14;
    private static final int SEATS_PER_ROW_B_C = 14;
    private static final int TOTAL_ROWS = ROWS_A_D + ROWS_B_C; // Total number of rows in the plane
    private static final int TOTAL_COLS = Math.max(SEATS_PER_ROW_A_D, SEATS_PER_ROW_B_C); // Total number of columns in
    // the plane, determined by
    // the row with the maximum
    // seats
    private static final int[][] seats = new int[TOTAL_ROWS][TOTAL_COLS]; // Array representing the plane's seating
    // arrangement, initialized with zeros
    // indicating available seats
    private static double total_sales_amount = 0; // Total sales amount, initially set to zero
    private static final Ticket[][] sold_tickets = new Ticket[ROWS_A_D + ROWS_B_C][SEATS_PER_ROW_A_D]; // Array to store
                                                                                                       // sold tickets

    // Method to initialize the seating arrangement of the plane
    private static void initialize_seats() {
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLS; j++) {
                seats[i][j] = 0; // 0 indicates available seat
            }
        }
    }

    private static char validate_row_letter(Scanner scanner) {
        char row_letter = ' ';
        boolean valid_input = false;
        do {
            System.out.print("Enter row letter (A-D): ");
            try {
                String input = scanner.next().toUpperCase();
                if (input.length() == 1 && input.charAt(0) >= 'A' && input.charAt(0) <= 'D') {
                    row_letter = input.charAt(0);
                    valid_input = true;
                } else {
                    System.out.println("Invalid row letter. Please enter a letter between A and D.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid row letter (A-D).");
                scanner.next(); // Clear the invalid input from the scanner
            }
        } while (!valid_input);
        return row_letter;
    }

    private static int validate_seat_number(Scanner scanner, char row_letter) {
        int seat_number = 0;
        boolean valid_input = false;

        // Determine the maximum seat number based on the row letter
        int max_seat_number = (row_letter == 'B' || row_letter == 'C') ? 12 : 14;

        // Input validation loop for seat number
        do {
            System.out.print("Enter seat number (1 - " + max_seat_number + "): ");
            try {
                seat_number = scanner.nextInt();
                if (seat_number >= 1 && seat_number <= max_seat_number) {
                    valid_input = true;
                } else {
                    System.out
                            .println("Invalid seat number. Please enter a number between 1 and " + max_seat_number
                                    + ".");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Clear the invalid input from the scanner
            }
        } while (!valid_input);

        return seat_number;
    }

    private static int[] calculate_seat_indices(char row_letter, int seat_number) {
        int row_index = row_letter - 'A';
        int seat_index;
        int max_seats_per_row;

        seat_index = seat_number - 1;
        if (row_index == 0 || row_index == 3) {
            max_seats_per_row = SEATS_PER_ROW_A_D;
        } else {
            max_seats_per_row = SEATS_PER_ROW_B_C;
        }

        return new int[] { row_index, seat_index, max_seats_per_row };
    }

    // Method to handle buying a seat
    private static void buy_seat(Scanner scanner) {
        char row_letter = validate_row_letter(scanner);
        int seat_number = validate_seat_number(scanner, row_letter);

        // Call the method to calculate seat indices
        int[] indices = calculate_seat_indices(row_letter, seat_number);
        int row_index = indices[0];
        int seat_index = indices[1];
        int max_seats_per_row = indices[2];

        // Check if the calculated row index and seat index are within valid ranges
        if (row_index >= 0 && row_index < TOTAL_ROWS && seat_index >= 0 && seat_index < max_seats_per_row) {
            if (seats[row_index][seat_index] == 0) {
                // Determine ticket price based on row and seat location
                double price;
                if (seat_index < 5) {
                    price = 200;
                } else if (seat_index < 9) {
                    price = 150;
                } else {
                    price = 180;
                }

                System.out.print("Enter name: ");
                String name = scanner.next();

                System.out.print("Enter surname: ");
                String surname = scanner.next();

                System.out.print("Enter email: ");
                String email = scanner.next();

                Person person = new Person(name, surname, email);

                Ticket ticket = new Ticket(String.valueOf(row_letter), seat_number, price, person);

                // Mark seat as sold
                seats[row_index][seat_index] = 1;

                // Store the ticket
                sold_tickets[row_index][seat_index] = ticket;

                total_sales_amount += price;

                System.out.println("Seat purchased successfully.");
                ticket.save();
            } else {
                System.out.println("Seat is already sold. Please select another seat.");
            }
        } else {
            System.out.println("Invalid row or seat number. Please try again.");
        }
    }

    // Method to handle canceling a seat
    private static void cancel_seat(Scanner scanner) {
        char row_letter = validate_row_letter(scanner);
        int seat_number = validate_seat_number(scanner, row_letter);

        // Call the method to calculate seat indices
        int[] indices = calculate_seat_indices(row_letter, seat_number);
        int row_index = indices[0];
        int seat_index = indices[1];
        int max_seats_per_row = indices[2];

        // Check if the calculated row index and seat index are within valid ranges
        if (row_index >= 0 && row_index < ROWS_A_D + ROWS_B_C && seat_index >= 0 && seat_index < max_seats_per_row) {
            Ticket ticket = sold_tickets[row_index][seat_index];
            if (ticket != null) {
                double price = ticket.getPrice();
                sold_tickets[row_index][seat_index] = null;
                total_sales_amount -= price;

                // Mark the seat as available again in the seating plan
                seats[row_index][seat_index] = 0;

                // Delete the associated ticket text file
                String fileName = row_letter + String.valueOf(seat_number) + ".txt";
                File file = new File(fileName);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("Ticket file deleted successfully.");
                    } else {
                        System.out.println("Failed to delete ticket file.");
                    }
                } else {
                    System.out.println("Ticket file does not exist.");
                }

                System.out.println("Seat cancelled successfully.");
            } else {
                System.out.println("Seat is already available. Nothing to cancel.");
            }
        } else {
            System.out.println("Invalid row or seat number. Please try again.");
        }
    }

    // Method to find the first seat available
    private static void find_first_available() {
        boolean seat_found = false;

        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    char row_letter = (char) ('A' + i);
                    int seat_number = j + 1;
                    System.out.println("First available seat found at row " + row_letter + ", seat " + seat_number);
                    seat_found = true;
                    break;
                }
            }
            if (seat_found) {
                break;
            }
        }

        if (!seat_found) {
            System.out.println("No available seats found.");
        }
    }

    // Method to handle showing of the seating plan
    private static void show_seating_plan() {
        System.out.print("    ");
        System.out.println("1  2  3  4  5  6  7  8  9  10 11 12 13 14");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < ROWS_A_D + ROWS_B_C; i++) {
            System.out.print((char) ('A' + i) + " | ");
            for (int j = 0; j < SEATS_PER_ROW_A_D; j++) {
                if (i == 1 && (j == 12 || j == 13) || (i == 2 && (j == 12 || j == 13))) {
                    System.out.print("  "); // Skip printing seat numbers for these seats
                } else {
                    if (seats[i][j] == 1) {
                        System.out.print("X  "); // Sold seat
                    } else {
                        System.out.print("O  "); // Available seat
                    }
                }
            }
            System.out.println();
        }
    }

    // Method to handle printing of tickets info
    private static void print_tickets_info() {
        boolean found_tickets = false; // Flag to check if any tickets are found

        double total_sales = 0; // Variable to hold total sales amount

        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (sold_tickets[i][j] != null) {
                    Ticket ticket = sold_tickets[i][j];
                    ticket.printInfo();
                    total_sales += ticket.getPrice(); // Accumulate total sales
                    found_tickets = true; // Set flag to true if at least one ticket is found
                }
            }
        }

        // Print total sales amount only if at least one ticket is found
        if (found_tickets) {
            System.out.println("Total Sales: £" + total_sales);
        } else {
            System.out.println("No tickets have been sold yet.");
        }
    }

    // Method to find if the ticket is available or not
    private static void search_ticket(Scanner scanner) {
        char row_letter = validate_row_letter(scanner);
        int seat_number = validate_seat_number(scanner, row_letter);

        // Call the method to calculate seat indices
        int[] indices = calculate_seat_indices(row_letter, seat_number);
        int row_index = indices[0];
        int seat_index = indices[1];
        int max_seats_per_row = indices[2];

        if (row_index >= 0 && row_index < TOTAL_ROWS && seat_index >= 0 && seat_index < max_seats_per_row) {
            if (seats[row_index][seat_index] == 0) {
                System.out.println("This seat is available.");
            } else {
                System.out.println("This seat is already sold.");
            }
        } else {
            System.out.println("Invalid row or seat number. Please try again.");
        }
    }

    // Main method
    public static void main(String[] args) {
        initialize_seats();

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWelcome to the Plane Management application\n");

        int option;

        // Print Menu options
        do {
            System.out.println("********************************************************");
            System.out.println("*                     MENU OPTIONS                     *");
            System.out.println("********************************************************");
            System.out.println("\t1) Buy a seat");
            System.out.println("\t2) Cancel a seat");
            System.out.println("\t3) Find first available seat");
            System.out.println("\t4) Show seating plan");
            System.out.println("\t5) Print tickets information and total sales");
            System.out.println("\t6) Search ticket");
            System.out.println("\t0) Quit");
            System.out.println("********************************************************");
            System.out.print("Please select an option: ");

            try {
                option = scanner.nextInt(); // Try to read an integer
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Clear the invalid input from the scanner
                option = -1; // Set option to an invalid value to prompt the user again
                continue; // Skip the rest of the loop and prompt the user again
            }

            switch (option) {
                case 0:
                    System.out.println("Exiting...");
                    break;
                case 1:
                    buy_seat(scanner);
                    break;
                case 2:
                    cancel_seat(scanner);
                    break;
                case 3:
                    find_first_available();
                    break;
                case 4:
                    show_seating_plan();
                    break;
                case 5:
                    print_tickets_info();
                    break;
                case 6:
                    search_ticket(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }
}