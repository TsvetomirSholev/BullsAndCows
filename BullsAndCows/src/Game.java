import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
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


    String numberC;
    int moves=0;
    String s="Player: ";
    String predictC="";
    String compMoves="Computer: ";
    String numP="";
    int winsPl=0;
    int winsC=0;

    public Game() {
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblScoreComputer.setText("Computer: "+winsC+"");
                lblScorePlayer.setText("Player: "+winsPl+"");
               // int num=Integer.parseInt(txtNumP.getText());
                numP=txtNumP.getText();

                int n=Integer.parseInt(numP);
                if(n<1000 || n>9999 || !isValid(numP)){
                    JFrame frame=new JFrame("message");
                    JOptionPane.showMessageDialog(frame,"Invalid number","Error",JOptionPane.ERROR_MESSAGE);
                    txtNumP.setText("");

                }else{
                    numberC=number();
                    lblcheat.setVisible(true);
                    lblPredicP.setEnabled(true);
                    txtNumPredict.setEnabled(true);
                    btnPredict.setEnabled(true);
                    txtAreaCompMoves.setEnabled(true);
                    txtAreaMoves.setEnabled(true);
                    btnStart.setEnabled(false);
                    lblNumP.setEnabled(false);
                    txtNumP.setEditable(false);
                }

            }

        });
        btnPredict.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moves++;
                String numPredictP=txtNumPredict.getText();
                if(numPredictP.length()>4 || numPredictP.length()<4){
                    JFrame frame=new JFrame("message");
                    JOptionPane.showMessageDialog(frame,"Invalid number","Error",JOptionPane.ERROR_MESSAGE);
                    txtNumP.setText("");
                }else{
                    s =s+ moves+". "+ bullOrCow(numPredictP, numberC);
                    if(btnNewGame.isVisible()){
                        winsPl++;
                    }
                    txtAreaMoves.setText(s);
                    predictC=number()+"";
                    //#TODO optimize computer predictions
                    compMoves=compMoves+moves+". "+bullOrCow(predictC,numP);
                    if(btnNewGame.isVisible()){{
                        winsC++;
                    }}
                    txtAreaCompMoves.setText(compMoves);
                    txtNumPredict.setText("");
                }

            }
        });

        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //#TODO new game
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

    public static void main(String[] args) {
        JFrame game=new JFrame("Bulls and Cows");
        game.setContentPane(new Game().pnlStart);
        game.setSize(700,700);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setVisible(true);
    }
    public String bullOrCow(String a,String b){
        int bulls=0;
        int cows=0;
        for (int i = 0; i <4 ; i++) {
            if(b.contains(a.charAt(i)+"") && b.charAt(i)!=a.charAt(i)){
                cows++;

            }
            if(b.charAt(i)==a.charAt(i)){
                bulls++;
                if(bulls==4){
                    btnNewGame.setVisible(true);
                    return a+" Congrats you win on "+moves+" move!!!";

                }
            }
        }
        String t=(a+" " +bulls+ " bulls "+cows+" cows\n");
        return t;
    }
    public String number(){
        ArrayList<Integer> numb=new ArrayList<>();
        numb.add(1);
        numb.add(2);
        numb.add(3);
        numb.add(4);
        numb.add(5);
        numb.add(6);
        numb.add(7);
        numb.add(8);
        numb.add(9);
        String s="";
        for (int i = 0; i <4 ; i++) {
            Random rand=new Random();
            int n=rand.nextInt(numb.size());
            s+=numb.get(n)+"";
            numb.remove(n);
        }
        return s;
    }
    public boolean isValid(String num){

            int a=Integer.parseInt(num.charAt(0)+"");
            int b=Integer.parseInt(num.charAt(1)+"");
            int c=Integer.parseInt(num.charAt(2)+"");
            int d=Integer.parseInt(num.charAt(3)+"");
            if((a-b==0)||(a-c==0)||(a-d==0)||(b-c==0)||(b-d==0)||(c-d==0)){
                return false;
            }
        return true;
    }
}
