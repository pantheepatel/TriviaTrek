import java.io.*;
import java.util.*;

public class User {
    private String username;
    private String password;
    private int highScore;

    public User(String username, String password,int highScore) {
        this.username = username;
        this.password = password;
        this.highScore = highScore;
    }

    // Getters and setters for username, password, and highScore
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHighScore(){
        return highScore;
    }

    public int getHighScore(String fileName) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // Read the file and update the high score for the specified user
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return Integer.parseInt(parts[2]);
                }
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        return highScore;
    }

    public boolean setHighScore(int highScore, String FileName) {
        this.highScore = highScore;
        return updateHighScore(FileName, this.username, this.highScore);
    }

    // Other methods as needed (e.g., methods for updating user information)

    public boolean saveToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(username + ":" + password + ":" + highScore);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateHighScore(String fileName, String username, int newHighScore) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // Read the file and update the high score for the specified user
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    parts[2] = String.valueOf(newHighScore); // Update the high score
                    line = String.join(":", parts);
                    found = true;
                }
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // Write the modified content back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return found; // Return true if the user was found and high score updated
    }

}
