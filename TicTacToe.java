/**
 * 
 * Tic Tac Toe Project by Ben Highsted. Started 20/04/2020.
 * 
 * Plan is to make a Tic Tac Toe game that is multiplayer over the internet and local.
 * Also would like to make an option to verse A.I.
 * 
 */

import javax.swing.*;
import java.util.Random;

public class TicTacToe{
    public static void main(String[] args){
        newGame();//once you have a GUI make a button 'New Game' that calls this.
    }

    public static void newGame(){
        int Move = 0;

        String[] GameState = {
        "null", "null", "null", 
        "null", "null", "null", 
        "null", "null", "null"}; //Thinking i'll use this array to hold the state of the game.

        boolean playerOneTurn = firstMove();

        setUpGUI();//once all the game variables are set up, we initiate the GUI.
    }

    public static boolean firstMove(){
        //Code to determine who goes first (0 for X, 1 for O)
        Random rand = new Random();
        int Roll = rand.nextInt(1);
        
        if(Roll == 0){
            return true;
        }else{
            return false;
        }
    }

    public static void setUpGUI() {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);
 
        frame.pack();
        frame.setVisible(true);
    }

}