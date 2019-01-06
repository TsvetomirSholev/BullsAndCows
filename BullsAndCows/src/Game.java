import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static JFrame GAME = new JFrame("Bulls and Cows");
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


    private String computerNumber;
    private int moves = 0;
    private String computerPredictionNumber = "";
    private String playerNumber = "";
    private static int playerWonCount;
    private static int computerWonCount;


    public Game() {
        btnStart.addActionListener(e -> {

            playerNumber = txtNumberPlayer.getText();

            if (playerNumber.length() < 4 || Integer.parseInt(playerNumber) > 9999 || !isValid(playerNumber) || Integer.parseInt(playerNumber) < 1000) {

                invalidNumberError();

            } else {

                //Computer generates it's number
                computerNumber = number();

                // Player and computer score
                lblScorePlayer.setText("Player: " + playerWonCount + "");
                lblScoreComputer.setText("Computer: " + computerWonCount + "");


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

            if (playerPredictionNumber.length() > 4 || playerPredictionNumber.length() < 4 || !isValid(playerPredictionNumber)) {
                invalidNumberError();
            } else {

                //TODO optimize computer predictions
                //Increase moves count and set a value for the computer's prediction
                moves++;
                computerPredictionNumber = number() + "";

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
                    playerWonCount++;
                    txtAreaPlayerMoves.append("Player won on the " + moves + " move with number: " + computerNumber);
                    btnNewGame.setVisible(true);
                    btnNewGame.setEnabled(true);
                } else {
                    txtAreaPlayerMoves.append(playerMoves + "\n");
                }

                if (countingBulls(computerPredictionNumber, playerNumber) == 4) {
                    computerWonCount++;
                    txtAreaComputerMoves.append("Computer won on the " + moves + " move with number: " + playerNumber);
                    btnNewGame.setVisible(true);
                    btnNewGame.setEnabled(true);
                } else {

                    txtAreaComputerMoves.append(computerMoves + "\n");

                }
                writeScoreToFile(playerWonCount, computerWonCount);
                txtNumberPredict.setText("");

            }

        });

        btnNewGame.addActionListener(e -> newGame(pnlStart));
        lblCheat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                lblCheat.setText(computerNumber);
            }
        });
    }

    //TODO Optimise computer guesses

    //Cows counter
    private Integer countingCows(String a, String b) {
        int cows = 0;
        for (int i = 0; i < 4; i++) {

            if (b.contains(a.charAt(i) + "") && b.charAt(i) != a.charAt(i)) {
                cows++;
            }
        }
        return cows;

    }

    //Bulls counter
    private Integer countingBulls(String a, String b) {
        int bulls = 0;
        for (int i = 0; i < 4; i++) {
            if (b.charAt(i) == a.charAt(i)) {
                bulls++;
                if (bulls == 4) {
                    btnNewGame.setEnabled(true);
                    btnPredict.setEnabled(false);
                    txtNumberPredict.setEnabled(false);
                }
            }
        }
        return bulls;
    }

    //A random 4 digit number generator
    private String number() {
        ArrayList<Integer> numb = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            numb.add(i);
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            Random rand = new Random();
            int n = rand.nextInt(numb.size());
            s.append(numb.get(n));
            numb.remove(n);
        }
        return s.toString();
    }

    //Number validation method
    // TODO Maybe we can rewrite this algorithm
    private boolean isValid(String num) {

        int a = Integer.parseInt(num.charAt(0) + "");
        int b = Integer.parseInt(num.charAt(1) + "");
        int c = Integer.parseInt(num.charAt(2) + "");
        int d = Integer.parseInt(num.charAt(3) + "");

        return (a - b != 0) && (a - c != 0) && (a - d != 0) && (b - c != 0) && (b - d != 0) && (c - d != 0);

    }

    // Better off within a method ,because we have 3 or more uses if this code
    private void invalidNumberError() {
        JFrame frame = new JFrame("message");
        JOptionPane.showMessageDialog(frame, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
        txtNumberPlayer.setText("");
    }

    //Writes both scores to a file
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

    // Reads score form the file and sets its values
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
        readScoreFromFile();
    }


    // Application main method
    public static void main(String[] args) {

        writeScoreToFile(playerWonCount, computerWonCount);

        GAME.setContentPane(new Game().pnlStart);
        GAME.setSize(900, 700);
        GAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAME.setVisible(true);
    }
}