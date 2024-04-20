import java.io.*;
import java.util.*;

public class Game1 {
    static Scanner scanner = new Scanner(System.in);
    static List<String> elements;

    public static void main(String[] args) {
        String[] levels = {"Easy", "Medium", "Hard"};
//        category name and file name must be same, can be into different case
        String[] categories = {"Fruits", "Veggies", "Animals"};

        int selectedLevel = displayMenu("Select Level:", levels);
        System.out.println("Selected Level: " + levels[selectedLevel]);

        int selectedCategory = displayMenu("Select Category:", categories);
        System.out.println("Selected Category: " + categories[selectedCategory]);

//        this will fetch data from the user choose category
        String fileName = categories[selectedCategory].toLowerCase() + ".txt";
        elements = readWordsFromFile(fileName, levels[selectedLevel]);
        int highScore = Integer.parseInt(elements.get(0));
        List<String> elementsToGuess = new ArrayList<>(elements.subList(1, elements.size()));
        System.out.println(highScore + " " + elementsToGuess);
        int score = mainGame(elementsToGuess);
        System.out.println("Your score: " + score);
        if (score > highScore) {
            System.out.println("You made new high score🥳");
            System.out.println("Past highScore was " + highScore);
            writeHighScore(fileName, score, levels[selectedLevel]);
        }
    }

    //    this method is to print initial instructions and displaying the level/category
    private static int displayMenu(String prompt, String[] options) {

        while (true) {
            System.out.println(prompt);

            for (int i = 0; i < options.length; i++) {
                System.out.println("    " + (i + 1) + ". " + options[i]);
            }

            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= options.length) {
                    return choice - 1;  // Adjust to 0-based index
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + options.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    //    this method is to read the file of the category which has been chosen by user
    private static List<String> readWordsFromFile(String fileName, String selectedLevel) {
        List<String> wordList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
//            wordList.add(reader.readLine());
            while (!Objects.equals(line = reader.readLine(), "complete")) {
                if (line.startsWith("Level- ")) {
                    String[] parts = line.split("Level- ");
                    String levelName = parts[1].trim();
                    System.out.println(levelName);
                    if (Objects.equals(levelName, selectedLevel)) {
                        System.out.println("level matched");
                        wordList.add(reader.readLine());
                        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                            wordList.add(line.trim());
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            System.out.println(e);
        }

        return wordList;
    }

    private static int mainGame(List<String> elements) {
        Collections.shuffle(elements);
        int totalGuesses = 3;
        int score = 0;
        for (String element : elements) {
            String elementToGuess = generateDashedString(element);
            System.out.printf("%-20s %03d%n", elementToGuess, score);
            int guessRemaining = totalGuesses - 1;
            for (int i = 1; i <= totalGuesses; i++) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase(element)) {
                    System.out.println("You guessed it correctly!");
                    score = score + guessRemaining + 1;
                    break;
                } else {
                    if (guessRemaining != 0) {
                        System.out.println("You have " + guessRemaining + " chances to guess ");
                        System.out.println(elementToGuess);
                        guessRemaining--;
                    } else {
                        System.out.println("You could not guess " + element + " correctly");
                        return score;
                    }
                }
            }
        }
        System.out.println("Congratulations!! You completed all words of this category");
        return score;
    }

    private static String generateDashedString(String original) {
        char[] chars = original.toCharArray();
        int length = chars.length;

//        because some random value would generate whole word as dashed or
//        every time half-length of the word will not be challenging to user
        int dashes = Math.min((length / 2), (int) (Math.random() * 10) + 1);

        // Replace random characters with dashes
        for (int i = 0; i < dashes; i++) {
            int randomIndex = (int) (Math.random() * length);
//          if place where we are going to place ' - ' is already having dash than change the value of i
            if (chars[randomIndex] == '-') {
                i--;
            }
            chars[randomIndex] = '-';
        }

        return new String(chars);
    }

    private static void writeHighScore(String fileName, int score, String selectedLevel) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;
            while (!Objects.equals(line = raf.readLine(), "complete")) {
                if (line.startsWith("Level- ")) {
                    String[] parts = line.split("Level- ");
                    String levelName = parts[1].trim();
                    System.out.println(levelName);
                    if (Objects.equals(levelName, selectedLevel)) {
                        System.out.println("level matched");
                        raf.writeBytes(Integer.toString(score));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            System.out.println(e);
        }

    }

}
