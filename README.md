# Plane Management Application

This Java application allows users to manage seating arrangements and ticket sales for a plane. Users can buy seats, cancel seats, find the first available seat, show the seating plan, print ticket information, and search for the availability of a specific seat.

## Features

- **Buy a Seat**: Users can purchase available seats by providing their name, surname, and email address.
- **Cancel a Seat**: Allows users to cancel a previously purchased seat, freeing it up for others to buy.
- **Find First Available Seat**: Find the first available seat on the plane and display its location.
- **Show Seating Plan**: Displays the current seating arrangement of the plane, indicating available and sold seats.
- **Print Tickets Information**: Prints information about all sold tickets, including total sales amount.
- **Search Ticket**: Allows users to check if a specific seat is available or already sold.

## Usage

1. **Compile**: Compile the Java files using a Java compiler.
   ```
   javac PlaneManagement.java Person.java Ticket.java
   ```

2. **Run**: Run the compiled `PlaneManagement` class.
   ```
   java PlaneManagement
   ```

3. **Follow the Menu Options**: Once the application starts, follow the on-screen menu options to perform various actions like buying a seat, canceling a seat, etc.

## Classes

### 1. PlaneManagement.java

- This class is the main driver for the application.
- It handles user inputs and interacts with other classes to perform actions.

### 2. Person.java

- Represents a person who buys a ticket.
- Contains attributes such as name, surname, and email address.
- Provides methods to retrieve and print personal information.

### 3. Ticket.java

- Represents a ticket purchased by a person for a specific seat.
- Contains attributes such as row, seat number, price, and associated person.
- Provides methods to retrieve ticket information and save ticket details to a file.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
