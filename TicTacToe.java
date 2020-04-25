/**
 * 
 * Tic Tac Toe Project by Ben Highsted. Started 20/04/2020.
 * 
 * Plan is to make a Tic Tac Toe game that is multiplayer over the internet and local.
 * Also would like to make an option to verse A.I.
 * 
 */

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class TicTacToe extends JPanel{

    public static String[] GameState = {
        "null", "null", "null", 
        "null", "null", "null", 
        "null", "null", "null"
    }; 

    public static String[] GamePositions = {
        "100, 100", "300, 100", "500, 100", 
        "100, 300", "300, 300", "500, 300", 
        "100, 500", "300, 500", "500, 500"
    };

    public static int Move = 0;

    public TicTacToe(){
        JPanel panel = new JPanel();
        panel.setSize(600, 600);

        add(panel);
    }

    /** 
        This method is called at the start of the program, then anytime the window is resized or interacted with.
        I will need to take that into consideration when planning how to draw the moves.
        At the moment i'm thinking that i'll draw the GameState[] array onto the board, so even if its redrawn
        There isnt a problem (i.e dont draw anything in the square if its null, draw X if X ... so on.)
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGameBoard(g);
        //x offset = 62
        //y offset = 80
        /*g.drawOval(238, 220, 125, 125);
        g.drawOval(50, 30, 125, 125);*/
    }

    public void drawGameBoard(Graphics g){
        /* Horizontal lines */
        g.drawLine(20, 180, 580, 180);
        g.drawLine(20, 380, 580, 380);
        /* Vertical lines*/
        g.drawLine(200, 20, 200, 560);
        g.drawLine(400, 20, 400, 560);

        for(int i = 0; i < 9; i++){
            if(GameState[i] == "X"){
                DrawX(g, i);
            }else if(GameState[i] == "O"){
                DrawO(g, i);
            }
        }

    }

    // NOW THINKING ILL USE ACTUAL IMAGES RATHER THAN DRAWING IF POSSIBLE.
    public static void DrawX(Graphics g, int position){
        int size = 125;
        int xoffset = 62, yoffset = 80;
        String[] location = GamePositions[position].split(", "); //location[0] for x, location[1] for y
        g.drawOval(Integer.parseInt(location[0]) - xoffset, Integer.parseInt(location[1]) - yoffset, size, size);

    }

    public static void DrawO(Graphics g, int position){
        
    }

    public static void main(String[] args){
        newGame();//once you have a GUI make a button 'New Game' that calls this.
    }

    public static void newGame(){
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

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX() + ", " + e.getY());
                GameState[Move] = "X";
                Move++;
                frame.repaint();
            }
        });

        frame.getContentPane().add(new TicTacToe());
 
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