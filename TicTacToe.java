/**
 * 
 * Tic Tac Toe Project by Ben Highsted. Started 20/04/2020.
 * 
 * Plan is to make a Tic Tac Toe game that is multiplayer over the internet and local.
 * Also would like to make an option to verse A.I.
 * 
 */

 //The next step for this project is to setup the graphics.
 //For a start, the best way is to make the program launch and the game starts (so all that is shown is the game board)
 //Then you play a game, and the game ends.
 //You can worry about making it look nice, adding menus, and everything else once the base game is completed.

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class TicTacToe extends JPanel{

    public TicTacToe(){
        JPanel panel = new JPanel();
        panel.setSize(620, 620);
        //JLabel label = new JLabel("Test Label!");
        //panel.add(label);
        add(panel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGameBoard(g);
    }

    public void drawGameBoard(Graphics g){
        /* Horizontal lines */
        g.drawLine(20, 180, 580, 180);
        g.drawLine(20, 380, 580, 380);
        /* Vertical lines*/
        g.drawLine(200, 20, 200, 560);
        g.drawLine(400, 20, 400, 560);
    }

    public static void main(String[] args){
        newGame();//once you have a GUI make a button 'New Game' that calls this.
    }

    public static void newGame(){
        int Move = 0;

        String[] GameState = {
        "X", "X", "X", 
        "null", "null", "null", 
        "null", "null", "null"}; //Thinking i'll use this array to hold the state of the game.
        //With this i can say that GameState[0] == GameState[4] == GameState[8] is a game over (3 in a row)
        
        boolean playerOneTurn = firstMove();

        //This is here currently just to test all the states. Will need to be called after every move when the GUI is set up.
        checkWinConditions(GameState);

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
        frame.setSize(600, 600);
 
        //JLabel label = new JLabel("Test Label!");
        frame.getContentPane().add(new TicTacToe());
 
        //frame.pack();
        frame.setVisible(true);
    }

    /** Checks the game state to see if a victory condition has been met */
    public static void checkWinConditions(String[] GameState){
        /** Rows */
        if(GameState[0] == GameState[1] && GameState[1] == GameState[2]){//Top row
            if(GameState[0] != "null"){
                //do action
                //I will need to do this or these statements will get picked up from the starting state.
                System.out.println("Test");
            }
        }
        if(GameState[3] == GameState[4] && GameState[4] == GameState[5]){//Middle row
            if(GameState[3] != "null"){
                
            }
        }
        if(GameState[6] == GameState[7] && GameState[7] == GameState[8]){//Bottom row
            if(GameState[6] != "null"){
                
            }
        }
        /** Columns */
        if(GameState[0] == GameState[3] && GameState[3] == GameState[6]){//Left column
            if(GameState[0] != "null"){
                
            }
        }
        if(GameState[1] == GameState[4] && GameState[4] == GameState[7]){//Middle column
            if(GameState[1] != "null"){
                
            }
        }
        if(GameState[2] == GameState[5] && GameState[5] == GameState[8]){//Right column
            if(GameState[2] != "null"){
                
            }
        }
        /** Diagonal */
        if(GameState[0] == GameState[4] && GameState[4] == GameState[8]){//Diagonal (right)
            if(GameState[0] != "null"){
                
            }
        }
        if(GameState[2] == GameState[4] && GameState[4] == GameState[6]){//Diagonal (left)
            if(GameState[2] != "null"){
                
            }
        }

        /* 
        Uncomment if you wish to print out the current state of the game when this method is called.

        for(int i = 0; i < GameState.length; i++){
            System.out.print(GameState[i] + " ");
            if(i == 2 || i == 5){
                System.out.println();
            }
        }
        */
        
    }
}