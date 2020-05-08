/**
 * 
 * This file is going to consist of all the TicTacToe content, except
 * it will auto connect to online server instead of allowing local play
 * 
 * 
 */

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

import java.util.Optional;
import java.util.Random;

import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeOnline extends JPanel
{
    public static JLabel playersTurn;

    public static JFrame frame;

    public static String[] GamePositions = {//x and y pos on game board
        "100, 105", "300, 105", "500, 105", 
        "100, 295", "300, 295", "500, 295", 
        "100, 480", "300, 480", "500, 480"
    };

    public static int victoryPos;

    public static String Winner = "null";

    public TicTacToeOnline(){
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel gameBoard = new JPanel();
        gameBoard.setPreferredSize(new Dimension(600, 580));
        gameBoard.setOpaque(false);//set all the panels to transparent so they have no background

        JPanel controller = new JPanel();
        controller.setPreferredSize(new Dimension(200, 580));
        controller.setOpaque(false);

        playersTurn = new JLabel();
        playersTurn.setText("   It is X's turn.   ");

        controller.add(playersTurn);

        mainPanel.add(gameBoard);
        mainPanel.add(controller);

        mainPanel.setOpaque(false);

        add(mainPanel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(600, 0, 600, 600);//dividing line between panels

        //Below draws the gameboard
        g.setColor(Color.BLACK);
        /* Horizontal lines */
        g.drawLine(20, 180, 580, 180);
        g.drawLine(20, 380, 580, 380);
        /* Vertical lines*/
        g.drawLine(200, 20, 200, 560);
        g.drawLine(400, 20, 400, 560);

        for(int i = 0; i < 9; i++){
            if(ReadWriteOnline.GameState[i] == "X"){
                DrawX(g, i, false);
            }else if(ReadWriteOnline.GameState[i] == "O"){
                DrawO(g, i, false);
            }
        }

        if(ReadWriteOnline.victory == true && ReadWriteOnline.showWinner == true){
            String[] winnerLocations = (Integer.toString(victoryPos)).split("");
            for(int i = 0; i < 3; i++){
                if(Winner == "X"){
                    DrawX(g, Integer.parseInt(winnerLocations[i]), true);
                }else{
                    DrawO(g, Integer.parseInt(winnerLocations[i]), true);
                }
            }
        }
        System.err.println("Paint Component Called!");
    }

    public static void DrawX(Graphics g, int position, boolean victory){
        if(victory == true){
            g.setColor(Color.RED);
        }
        int xoffset = 62, yoffset = 60;
        String[] location = GamePositions[position].split(", "); //location[0] for x, location[1] for y

        g.drawLine(Integer.parseInt(location[0]) - xoffset, (Integer.parseInt(location[1]) - 10) - yoffset, 
        Integer.parseInt(location[0]) + xoffset, (Integer.parseInt(location[1]) - 10) + yoffset);

        g.drawLine(Integer.parseInt(location[0]) + xoffset, (Integer.parseInt(location[1]) - 10) - yoffset, 
        Integer.parseInt(location[0]) - xoffset, (Integer.parseInt(location[1]) - 10) + yoffset);
    }

    public static void DrawO(Graphics g, int position, boolean victory){
        if(victory == true){
            g.setColor(Color.RED);
        }
        int size = 125;
        int xoffset = 62, yoffset = 75;//the circle is not drawn around the center point, it is the position of the top left square.
        String[] location = GamePositions[position].split(", "); //location[0] for x, location[1] for y
        g.drawOval(Integer.parseInt(location[0]) - xoffset, Integer.parseInt(location[1]) - yoffset, size, size);
    }

    public static void main(String[] args)
    {
        frame = new JFrame("Tic Tac Toe (Online Version)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX() + ", " + e.getY());
                userClick(e.getX(), e.getY());
                frame.repaint();
            }
        });

        frame.getContentPane().add(new TicTacToeOnline());

        frame.pack();
        frame.setVisible(true);

        try {
            int port = 1804;
            Socket socket = new Socket("localhost", port);

            System.err.println("Connected to localhost on port " + port);

            new ReadWriteOnline(socket.getInputStream(), System.out).start();
            new ReadWriteOnline(System.in, socket.getOutputStream()).start();

        }catch (Exception e) {
            e.printStackTrace();
            System.err.println("\nUsage: java TicTacToeOnline.java");
        }
    }

    public static void userClick(int posX, int posY)
    {
        if(ReadWriteOnline.yourTurn)
        {
            for(int i = 0; i < 9; i++)
            {
                String[] location = GamePositions[i].split(", ");
                if(posX > Integer.parseInt(location[0]) - 90 && posX < Integer.parseInt(location[0]) + 90 
                    && posY > Integer.parseInt(location[1]) - 90 && posY < Integer.parseInt(location[1]) + 90)
                {
                    MakeMove(i);
                }
            }
        }
    }

    public static void MakeMove(int position)
    {
        if(ReadWriteOnline.GameState[position].equals("null"))
        {
            String send = Integer.toString(position);
            ReadWriteOnline.output.println(send);
        }
    }
}

class ReadWriteOnline extends Thread{

    private BufferedReader input;//input used to recieve from the server
    public static PrintWriter output;//output used to send to the server

    public static String[] GameState = {
        "null", "null", "null", 
        "null", "null", "null", 
        "null", "null", "null"
    }; 

    public static String token = "null";
    public static String token2 = "null";

    public static boolean yourTurn = false;
    public static boolean victory = false;
    public static boolean showWinner = false;
    public static int player;

    public ReadWriteOnline(InputStream input, OutputStream output) {
        this.input = new BufferedReader(new InputStreamReader(input));
        ReadWriteOnline.output = new PrintWriter(output, true);
    }

    public void run(){
        try{
            String line;
            int count = 0;
            while ((line = input.readLine()) != null) {
                if(!victory){
                    if(line.equals("Player1")){
                        System.err.println("You are Player1!");
                        player = 1;

                        token = "X";
                        token2 = "O";
                        yourTurn = true;
                        TicTacToeOnline.playersTurn.setText("It is your turn.");
                    }else if(line.equals("Player2")){
                        System.err.println("You are Player2!");
                        player = 2;

                        token = "O";
                        token2 = "X";
                        yourTurn = false;
                        TicTacToeOnline.playersTurn.setText("It is the opponents turn.");
                    }else if(line.equals("Victory")){
                        victory = true;
                    }else{
                        if(yourTurn){
                            GameState[Integer.parseInt(line)] = token;
                            yourTurn = false;
                            TicTacToeOnline.playersTurn.setText("It is the opponents turn.");
                        }else{
                            GameState[Integer.parseInt(line)] = token2;
                            yourTurn = true;
                            TicTacToeOnline.playersTurn.setText("It is your turn.");
                        }
                        TicTacToeOnline.frame.repaint();
                    }
                }else{
                    if(count == 0){
                        TicTacToeOnline.victoryPos = Integer.parseInt(line);
                        count++;
                    }else if(count == 1){
                        if(line.equals("X")){
                            TicTacToeOnline.Winner = "X";
                            if(player == 1){
                                TicTacToeOnline.playersTurn.setText("You win!");
                            }else{
                                TicTacToeOnline.playersTurn.setText("You lose");
                            }
                        }else{
                            TicTacToeOnline.Winner = "O";
                            if(player == 2){
                                TicTacToeOnline.playersTurn.setText("You win!");
                            }else{
                                TicTacToeOnline.playersTurn.setText("You lose");
                            }
                        }
                        count++;
                        showWinner = true;
                        TicTacToeOnline.frame.repaint();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}