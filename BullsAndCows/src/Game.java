import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {

    //Graphical Objects
    private JPanel pnlStart;
    private JTextField txtNumberPlayer;
    private JButton btnStart;
    private JTextField txtNumberPredict;
    private JButton btnPredict;
    private JLabel lblEnterPlayerNumber;
    private JLabel lblPredictComputerNumber;
    private JLabel lblCheat;
    private JLabel lblMoves;
    private JTextArea txtAreaPlayerMoves;
    private JTextArea txtAreaComputerMoves;
    private JButton btnNewGame;
    private JLabel lblPlayer;
    private JLabel lblComputer;
    private JLabel lblScorePlayer;
    private JLabel lblScoreComputer;

    //Backend variables
    private static JFrame GAME = new JFrame("Bulls and Cows");
    private static int playerVictoryCount;
    private static int computerVictoryCount;
    private static String[] computerBullsPlacementArray = {null, null, null, null};
    private int moves = 0;
    private String computerNumber;
    private String computerPredictionNumber = "";
    private String playerNumber = "";



    public Game() {
        btnStart.addActionListener(e -> {

            playerNumber = txtNumberPlayer.getText();

            if (playerNumber.length() < 4 || Integer.parseInt(playerNumber) > 9999 || !isValid(playerNumber) || Integer.parseInt(playerNumber) < 1000) {

                invalidNumberError();

            } else {

                //Computer generates it's initial number !
                computerNumber = computerGuessAndNumberGenerator();

                // Player and computer score
                lblScorePlayer.setText("Player: " + playerVictoryCount + "");
                lblScoreComputer.setText("Computer: " + computerVictoryCount + "");


                lblEnterPlayerNumber.setEnabled(false);
                lblPredictComputerNumber.setEnabled(true);
                lblCheat.setVisible(true);

                txtNumberPlayer.setEditable(false);
                txtNumberPredict.setEnabled(true);
                txtAreaComputerMoves.setEnabled(true);
                txtAreaPlayerMoves.setEnabled(true);

                btnStart.setEnabled(false);
                btnPredict.setEnabled(true);
                btnNewGame.setEnabled(false);

            }

        });
        btnPredict.addActionListener(e -> {

            String playerPredictionNumber = txtNumberPredict.getText();
            // First we check if the number is a valid one
            if (playerPredictionNumber.length() > 4 || playerPredictionNumber.length() < 4 || !isValid(playerPredictionNumber)) {
                invalidNumberError();
            } else {

                //Increase moves count and set a value for the computer's prediction
                moves++;
                computerPredictionNumber = computerGuessAndNumberGenerator() + "";

                //Sets the fields with the moves counter and number guessing history
                String playerMoves = "On move " + moves + " number: \"" + playerPredictionNumber
                        + "\" has: "
                        + countingBulls(playerPredictionNumber, computerNumber)
                        + " bulls and " + countingCows(playerPredictionNumber, computerNumber)
                        + " cows";


                String computerMoves = "On move " + moves + " number: \"" + computerPredictionNumber
                        + "\" has: "
                        + countingBulls(computerPredictionNumber, playerNumber)
                        + " bulls and " + countingCows(computerPredictionNumber, playerNumber)
                        + " cows";

                //Win conditions and cows and bulls printing to user
                if (countingBulls(playerPredictionNumber, computerNumber) == 4 && countingBulls(computerPredictionNumber, playerNumber) == 4) {
                    JFrame drawMessage = new JFrame("Draw");
                    JOptionPane.showMessageDialog(drawMessage, "This ends in a Draw !", "DrawMessage", JOptionPane.INFORMATION_MESSAGE);
                    txtNumberPlayer.setText("");
                }
                if (countingBulls(playerPredictionNumber, computerNumber) == 4) {
                    playerVictoryCount++;
                    txtAreaPlayerMoves.append("Player won on the " + moves + " move with number: " + computerNumber);
                    btnNewGame.setVisible(true);
                    btnNewGame.setEnabled(true);
                } else {
                    txtAreaPlayerMoves.append(playerMoves + "\n");
                }

                if (countingBulls(computerPredictionNumber, playerNumber) == 4) {
                    computerVictoryCount++;
                    txtAreaComputerMoves.append("Computer won on the " + moves + " move with number: " + playerNumber);
                    btnNewGame.setVisible(true);
                    btnNewGame.setEnabled(true);
                } else {

                    txtAreaComputerMoves.append(computerMoves + "\n");

                }

                //Here we save the score to a file ,which is instantiated every time we restart the Application
                writeScoreToFile(playerVictoryCount, computerVictoryCount);
                txtNumberPredict.setText("");

            }

        });

        btnNewGame.addActionListener(e -> newGame(pnlStart));
        //The button, which is used only for debugging and stupid people !
        lblCheat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                lblCheat.setText(computerNumber);
            }
        });
    }

    //Separate methods for counting bulls and cows , so we can see the code clearer and be more specific in future reimplementation of the Application

    //Cows countering method
    private Integer countingCows(String guessingNumber, String guessedNumber) {
        int cows = 0;
        for (int i = 0; i < 4; i++) {

            if (guessedNumber.contains(guessingNumber.charAt(i) + "") && guessedNumber.charAt(i) != guessingNumber.charAt(i)) {
                cows++;
            }
        }
        return cows;

    }

    //Bulls counting method
    private Integer countingBulls(String guessingNumber, String guessedNumber) {
        int bulls = 0;

        for (int i = 0; i < 4; i++) {
            if (guessedNumber.charAt(i) == guessingNumber.charAt(i)) {
                bulls++;
                computerBullsPlacementArray[i] = guessedNumber.charAt(i) + "";
                if (bulls == 4) {
                    btnNewGame.setEnabled(true);
                    btnPredict.setEnabled(false);
                    txtNumberPredict.setEnabled(false);
                }
            }
        }
        return bulls;
    }

    //A random 4 digit number generator - The biggest bullshit of this project, this tested our mental health and nerves throughout the whole project
    private String computerGuessAndNumberGenerator() {
        int guessedCounter = 0;
        List<Integer> numberList = IntStream.range(1,10).boxed().collect(Collectors.toList());
        StringBuilder randomString = new StringBuilder();
        Random rand = new Random();


        for (int i = 0; i < computerBullsPlacementArray.length; i++) {
            if (computerBullsPlacementArray[i] != null) {
                guessedCounter++;
            }
        }
        if (guessedCounter == 0) {
            for (int i = 0; i < 4; i++) {
                randomStringBuilder(rand, randomString, numberList);
            }
            return randomString.toString();
            // A certain problem occurred when we perfected the guesses of the computer - it started guessing the number in 4-7 moves
            //TODO: ELSE IF GuessCounter = 3 - fix bad performance

        } else {
            for (int i = 0; i < 4; i++) {
                if (computerBullsPlacementArray[i] == null) {
                    Random rand2 = new Random();
                    int p = rand2.nextInt(numberList.size());
                    randomString.append(numberList.get(p));
                    numberList.remove(p);

                    //difficulty adjustments for hardcore/soft players

//                    if (moves == 5) {
//                        randomString = new StringBuilder(playerNumber);
//                    }

                } else {
                    randomString.append(computerBullsPlacementArray[i]);
                }

            }
            return randomString.toString();
        }
    }

    //Had to make a separate method ,so that we have no code recurrences
    private StringBuilder randomStringBuilder(Random random, StringBuilder stringBuilder, List<Integer> arrayList) {
        int t = random.nextInt(arrayList.size());
        stringBuilder.append(arrayList.get(t));
        arrayList.remove(t);

        return stringBuilder;
    }

    //Number validation method
    // TODO Maybe we can rewrite this algorithm but it certainly looks good for now

    private boolean isValid(String num) {

        int a = Integer.parseInt(num.charAt(0) + "");
        int b = Integer.parseInt(num.charAt(1) + "");
        int c = Integer.parseInt(num.charAt(2) + "");
        int d = Integer.parseInt(num.charAt(3) + "");

        return (a - b != 0) && (a - c != 0) && (a - d != 0) && (b - c != 0) && (b - d != 0) && (c - d != 0);

    }

    // Better off within a method ,because we have 3 or more uses of this code
    private void invalidNumberError() {
        JFrame frame = new JFrame("message");
        JOptionPane.showMessageDialog(frame, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
        txtNumberPlayer.setText("");
    }

    //Writes both scores to a file , so that we can reuse them when new game is initialised
    private static void writeScoreToFile(Integer pScore, Integer compScore) {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("dataBase.txt"), StandardCharsets.UTF_8))) {
            writer.write(String.valueOf(pScore));
            writer.newLine();
            writer.write(String.valueOf(compScore));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Reads score form the file and sets values to the scores
    private void readScoreFromFile() {
        try (final LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("dataBase.txt"))) {
            String line;

            while ((line = lineNumberReader.readLine()) != null) {
                lblScorePlayer.setText(line);
                lblScoreComputer.setText(lineNumberReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Method for making a new game
    private void newGame(JPanel panel) {
        // gets variables for scores from file

        GAME.dispose();
        GAME.setContentPane(new Game().pnlStart);
        GAME.setSize(900, 700);
        GAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAME.setVisible(true);
        computerBullsPlacementArray = new String[]{null, null, null, null};
        readScoreFromFile();
    }


    // Application main method
    public static void main(String[] args) {

        writeScoreToFile(playerVictoryCount, computerVictoryCount);

        GAME.setContentPane(new Game().pnlStart);
        GAME.setSize(900, 700);
        GAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAME.setVisible(true);
    }
}