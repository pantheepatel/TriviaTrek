import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class TriviaTrek {

    public static final Scanner sc = new Scanner(System.in);
    private static User currentUser;
    private static List<User> users = new ArrayList<>();
    public static final String fileName = "data/users.txt";
    public static boolean loggedin = false;
    public static int choice = 0;
    public static int highScore = 0;
    static List<String> elements;

    public static void main(String[] args) {

        List<User> userList = loadUserDataFromFile(fileName);

        System.out.println("\nWelcome to TriviaTrek Game\n");

        step_1();

    }

    private static void step_1() {
        while (!loggedin) {
            System.out.print(
                    "Please choose accordingly \n" +
                            "\t1. Register \n " +
                            "\t2. Login \n " +
                            "\t3. Exit \n" +
                            "\t\tPlease enter your choice here : ");
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> System.out.println("Thank you");
                default -> System.out.println("Please choose accordingly");
            }
        }
        step_2();
    }

    private static void step_2() {
        System.out.println("\nHere are some rules of this game\n");
        WordList step3 = new WordList();
        step3.step_3(currentUser, elements);
    }


    private static List<User> loadUserDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line to get username and password (assuming format is username:password)
                String[] parts = line.split(":");
                String username = parts[0];
                String password = parts[1];
                int highScore = Integer.parseInt(parts[2]);

                // Create a new User object and add it to the list
                User user = new User(username, password);
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static void registerUser() {
        sc.nextLine(); // Consume newline character
        System.out.print("Enter a username: ");
        String username = sc.nextLine();
        // Check if username is already taken
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists. Please choose a different one.");
                return;
            }
        }
        String password;
        do {
            System.out.println("Enter a password (Least 7 characters, including uppercase, lowercase, digit, and special character): ");
            password = sc.nextLine();
        } while (!isValidPassword(password));

        // Create and add new user
        User newUser = new User(username, password);
        users.add(newUser);
        boolean success = newUser.saveToFile(fileName);
        if (success) {
            System.out.println("User registered successfully!");
            loginUser(username, password);
        } else {
            System.out.println("Can not register at this moment, Please try again later!");
        }

    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        boolean isValid = m.matches();
        if (!isValid) {
            System.out.println("Please enter correct password pattern");
        }
        return isValid;
    }

    private static void loginUser() {
        sc.nextLine(); // Consume newline character
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
//        String password = sc.nextLine();
        String password = Arrays.toString(System.console().readPassword());
//        String password = Arrays.toString(System.console().readPassword("[%s]","Password : "));
        loginUser(username, password);
    }

    private static void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Logging...");
                // Check if password matches
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    System.out.println("Login successful. Welcome, " + username + "!");
                    highScore = user.getHighScore(fileName);
                    loggedin = true;
                    return;
                } else {
                    System.out.println("Incorrect password. Please try again.");
                    return;
                }
            }
        }
        System.out.println("User not found. Please register or try again.");
    }
}
