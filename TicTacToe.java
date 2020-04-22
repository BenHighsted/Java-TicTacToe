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
        //With this i can say that GameState[0] == GameState[4] == GameState[8] is a game over (3 in a row)

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
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
 
        JLabel label = new JLabel("Test Label!");
        frame.getContentPane().add(label);
 
        //frame.pack();
        frame.setVisible(true);
    }

    /** Checks the game state to see if a victory condition has been met */
    public static void checkWinConditions(String[] GameState){
        /** Rows */
        if(GameState[0] == GameState[1] && GameState[1] == GameState[2]){//Top row

        }
        if(GameState[3] == GameState[4] && GameState[4] == GameState[5]){//Middle row

        }
        if(GameState[6] == GameState[7] && GameState[7] == GameState[8]){//Bottom row

        }
        /** Columns */
        if(GameState[0] == GameState[3] && GameState[3] == GameState[6]){//Left column

        }
        if(GameState[1] == GameState[4] && GameState[4] == GameState[7]){//Middle column

        }
        if(GameState[2] == GameState[5] && GameState[5] == GameState[8]){//Right column

        }
        /** Diagonal */
        if(GameState[0] == GameState[4] && GameState[4] == GameState[8]){//Diagonal (right)

        }
        if(GameState[2] == GameState[4] && GameState[4] == GameState[6]){//Diagonal (left)

        }
        
    }

}