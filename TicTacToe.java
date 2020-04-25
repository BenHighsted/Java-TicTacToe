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
        "100, 105", "300, 105", "500, 105", 
        "100, 295", "300, 295", "500, 295", 
        "100, 480", "300, 480", "500, 480"
    };

    public static String[] VictoryPositions = {
        "012", "345", "678", "036", "147", "258", "048", "246"
    };

    public static int Move = 0;
    public static boolean playerOneTurn = true, victory = false;

    public TicTacToe(){
        JPanel panel = new JPanel();
        panel.setSize(600, 600);

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

        for(int i = 0; i < 9; i++){
            if(GameState[i] == "X"){
                DrawX(g, i);
            }else if(GameState[i] == "O"){
                DrawO(g, i);
            }
        }

        if(victory == true){

        }
    }

    public static void DrawX(Graphics g, int position){
        int xoffset = 62, yoffset = 60;
        String[] location = GamePositions[position].split(", "); //location[0] for x, location[1] for y

        g.drawLine(Integer.parseInt(location[0]) - xoffset, (Integer.parseInt(location[1]) - 10) - yoffset, 
        Integer.parseInt(location[0]) + xoffset, (Integer.parseInt(location[1]) - 10) + yoffset);

        g.drawLine(Integer.parseInt(location[0]) + xoffset, (Integer.parseInt(location[1]) - 10) - yoffset, 
        Integer.parseInt(location[0]) - xoffset, (Integer.parseInt(location[1]) - 10) + yoffset);
    }

    public static void DrawO(Graphics g, int position){
        int size = 125;
        int xoffset = 62, yoffset = 75;//the circle is not drawn around the center point, it is the position of the top left square.
        String[] location = GamePositions[position].split(", "); //location[0] for x, location[1] for y
        g.drawOval(Integer.parseInt(location[0]) - xoffset, Integer.parseInt(location[1]) - yoffset, size, size);
    }

    public static void main(String[] args){
        newGame();//once you have a GUI make a button 'New Game' that calls this.
    }

    public static void newGame(){
        //boolean playerOneTurn = firstMove(); for now its just going to be local (one pc) so this isnt used, but in the future it will be.

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

    public static void userClick(int x, int y){
        for(int i = 0; i < GamePositions.length; i++){
            String[] location = GamePositions[i].split(", ");
            if(GameState[i] == "null" && victory == false){
                if(x > Integer.parseInt(location[0]) - 100 && x < Integer.parseInt(location[0]) + 100 
                && y > Integer.parseInt(location[1]) - 100 && y < Integer.parseInt(location[1]) + 100){
                    if(playerOneTurn == true){
                        GameState[i] = "X";
                        playerOneTurn = false;
                        Move++;
                    }else{
                        GameState[i] = "O";
                        playerOneTurn = true;
                        Move++;
                    }
                    checkWinConditions(GameState);
                }
            }
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
                if(Move <= 9){
                    userClick(e.getX(), e.getY());
                }

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
                victory = true;
            }
        }
        if(GameState[3] == GameState[4] && GameState[4] == GameState[5]){//Middle row
            if(GameState[3] != "null"){
                victory = true;
            }
        }
        if(GameState[6] == GameState[7] && GameState[7] == GameState[8]){//Bottom row
            if(GameState[6] != "null"){
                victory = true;
            }
        }
        /** Columns */
        if(GameState[0] == GameState[3] && GameState[3] == GameState[6]){//Left column
            if(GameState[0] != "null"){
                victory = true;
            }
        }
        if(GameState[1] == GameState[4] && GameState[4] == GameState[7]){//Middle column
            if(GameState[1] != "null"){
                victory = true;
            }
        }
        if(GameState[2] == GameState[5] && GameState[5] == GameState[8]){//Right column
            if(GameState[2] != "null"){
                victory = true;
            }
        }
        /** Diagonal */
        if(GameState[0] == GameState[4] && GameState[4] == GameState[8]){//Diagonal (right)
            if(GameState[0] != "null"){
                victory = true;
            }
        }
        if(GameState[2] == GameState[4] && GameState[4] == GameState[6]){//Diagonal (left)
            if(GameState[2] != "null"){
                victory = true;
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