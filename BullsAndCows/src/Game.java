import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static JFrame GAME = new JFrame("Bulls and Cows");
    private JPanel pnlStart;
    private JTextField txtNumP;
    private JButton btnStart;
    private JTextField txtNumPredict;
    private JButton btnPredict;
    private JLabel lblNumP;
    private JLabel lblPredicP;
    private JLabel lblcheat;
    private JLabel lblMoves;
    private JTextArea txtAreaMoves;
    private JTextArea txtAreaCompMoves;
    private JButton btnNewGame;
    private JLabel lblPlayer;
    private JLabel lblComputer;
    private JLabel lblScorePlayer;
    private JLabel lblScoreComputer;


    private String numberC;
    private int moves = 0;
    private String predictC = "";
    private String numP = "";
    private int winsPl = 0;
    private int winsC = 0;


    public Game() {
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // int num=Integer.parseInt(txtNumP.getText());
                numP = txtNumP.getText();
//                int[] nArray = new int[4];
//                List<Integer> myList = Arrays.asList(nArray[0],nArray[1],nArray[2],nArray[3]);

                if (numP.length() < 4 || Integer.parseInt(numP) > 9999 || !isValid(numP) || Integer.parseInt(numP) < 1000) {
                    invalidNumberError();

                } else {
                    lblScoreComputer.setText("Computer: " + winsC + "");
                    lblScorePlayer.setText("Player: " + winsPl + "");
                    numberC = number();
                    lblcheat.setVisible(true);
                    lblPredicP.setEnabled(true);
                    txtNumPredict.setEnabled(true);
                    btnPredict.setEnabled(true);
                    txtAreaCompMoves.setEnabled(true);
                    txtAreaMoves.setEnabled(true);
                    btnStart.setEnabled(false);
                    lblNumP.setEnabled(false);
                    txtNumP.setEditable(false);
                    btnNewGame.setEnabled(false);
                }

            }

        });
        btnPredict.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numPredictP = txtNumPredict.getText();
                if (numPredictP.length() > 4 || numPredictP.length() < 4 || !isValid(numPredictP)) {
                    invalidNumberError();
                } else {
                    moves++;
                    String playerMoves = "Player: ";
                    String compMoves = "Computer: ";
                    playerMoves = playerMoves + moves + ". " + numPredictP + " " + countingBulls(numPredictP, numberC) + " bulls " + countingCows(numPredictP, numberC) + " cows";
                    if (countingBulls(numPredictP, numberC) == 4) {
                        winsPl++;
                    }
                    txtAreaMoves.append(playerMoves + "\n");
                    predictC = number() + "";
                    //#TODO optimize computer predictions
                    compMoves = compMoves + moves + ". " + predictC + " " + countingBulls(predictC, numP) + " bulls " + countingCows(predictC, numP) + " cows";
                    if (countingBulls(predictC, numP) == 4) {
                        {
                            winsC++;
                        }
                    }
                    txtAreaCompMoves.append(compMoves + "\n");
                    txtNumPredict.setText("");
                }

            }
        });

        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //#TODO new GAME
                newGame(pnlStart);
//                pnlStart.removeAll();
//                pnlStart.revalidate();
//                pnlStart.repaint();
            }
        });
        lblcheat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                lblcheat.setText(numberC);
            }
        });
    }
    //TODO Method for New GAME
    //TODO Store the Variables in a Class and push them to the Application on every reinitialisation


    //TODO BULLS AND COWS COUNTING LOGIC MUST BE OPTIMISED

//    public String bullOrCow(String a, String b) {
//        int cows = 0;
//        int bulls = 0;
//
//        for (int i = 0; i < 4; i++) {
//
//            if (b.contains(a.charAt(i) + "") && b.charAt(i) != a.charAt(i)) {
//                cows++;
//
//            } else if (b.charAt(i) == a.charAt(i)) {
//                bulls++;
//                if (bulls == 4) {
////                    btnNewGame.setEnabled(true);
//                    btnPredict.setEnabled(false);
//                    txtNumPredict.setEnabled(false);
//                    return a + " Congrats you won on the " + moves + " move!!!";
//                }
//            }
//        }
//        String t = (a + " " + bulls + " bulls " + cows + " cows\n");
//        return t;
//    }

    public Integer countingCows(String a, String b) {
        int cows = 0;
        for (int i = 0; i < 4; i++) {

            if (b.contains(a.charAt(i) + "") && b.charAt(i) != a.charAt(i)) {
                cows++;
            }
        }
        return cows;

    }

    public Integer countingBulls(String a, String b) {
        int bulls = 0;
        for (int i = 0; i < 4; i++) {
            if (b.charAt(i) == a.charAt(i)) {
                bulls++;
                if (bulls == 4) {
                    btnNewGame.setEnabled(true);
                    btnPredict.setEnabled(false);
                    txtNumPredict.setEnabled(false);
                }
            }
        }
        return bulls;
    }

    public String number() {
        ArrayList<Integer> numb = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            numb.add(i);
        }
        String s = "";
        for (int i = 0; i < 4; i++) {
            Random rand = new Random();
            int n = rand.nextInt(numb.size());
            s += numb.get(n) + "";
            numb.remove(n);
        }
        return s;
    }

    public boolean isValid(String num) {

        int a = Integer.parseInt(num.charAt(0) + "");
        int b = Integer.parseInt(num.charAt(1) + "");
        int c = Integer.parseInt(num.charAt(2) + "");
        int d = Integer.parseInt(num.charAt(3) + "");
        if ((a - b == 0) || (a - c == 0) || (a - d == 0) || (b - c == 0) || (b - d == 0) || (c - d == 0)) {
            return false;
        }
        return true;
    }

    public void invalidNumberError() {
        JFrame frame = new JFrame("message");
        JOptionPane.showMessageDialog(frame, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
        txtNumP.setText("");
    }


    public void newGame(JPanel panel) {
        GAME.dispose();
        GAME.setContentPane(new Game().pnlStart);
        GAME.setSize(900, 700);
        GAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAME.setVisible(true);
    }


    public static void main(String[] args) {
        File dataBase = new File("database.txt");

        GAME.setContentPane(new Game().pnlStart);
        GAME.setSize(900, 700);
        GAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GAME.setVisible(true);
    }
}
