public class Person {
    private String name;
    private String surname;
    private String email;

    // Constructor to initialize Person object with name, surname, and email
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // Getter methods for retrieving person attributes
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    // Method to print information about the person
    public void printInfo() {
        System.out.println("Name: " + name + " " + surname);
        System.out.println("Email: " + email + "\n");
    }
}
