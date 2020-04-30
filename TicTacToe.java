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

import java.util.Optional;
import java.util.Random;

import java.io.*;
import java.net.*;
import java.util.*;//multiplayer network imports

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

    public static String Winner = "null";
    public static int Move = 0, winningNumber = 0;
    public static boolean playerOneTurn = true, victory = false;
    public static JLabel playersTurn;

    public static boolean onlinePlay = true;
    public static boolean running = true;
    public static int port = 7770;

    public static void main(String[] args){
        newGame();//At the moment starts a game on launch. Can change this later.
    }

    public TicTacToe(){
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel gameBoard = new JPanel();
        gameBoard.setPreferredSize(new Dimension(600, 580));
        gameBoard.setOpaque(false);//set all the panels to transparent so they have no background

        JPanel controller = new JPanel();
        controller.setPreferredSize(new Dimension(200, 580));
        controller.setOpaque(false);

        JButton newGame = new JButton();
        newGame.setText("New Game");
        controller.add(newGame);

        newGame.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e){ 
                resetValues();
                newGame();
            } 
        });

        JButton host = new JButton();
        host.setText("     Host     ");
        controller.add(host);

        JButton connect = new JButton();
        connect.setText("     Join     ");
        controller.add(connect);

        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                onlinePlay = true;
                connectThread.start();
                host.setVisible(false);
            }
        });

        host.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                onlinePlay = true;
                hostThread.start();
                connect.setVisible(false);
            }
        });

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
        g.drawLine(600, 0, 600, 600);

        drawGameBoard(g);
    }

    public void drawGameBoard(Graphics g){
        g.setColor(Color.BLACK);

        /* Horizontal lines */
        g.drawLine(20, 180, 580, 180);
        g.drawLine(20, 380, 580, 380);
        /* Vertical lines*/
        g.drawLine(200, 20, 200, 560);
        g.drawLine(400, 20, 400, 560);

        for(int i = 0; i < 9; i++){
            if(GameState[i] == "X"){
                DrawX(g, i, false);
            }else if(GameState[i] == "O"){
                DrawO(g, i, false);
            }
        }

        if(victory == true){
            String[] winnerLocations = VictoryPositions[winningNumber].split("");
            for(int i = 0; i < winnerLocations.length; i++){
                if(Winner == "X"){
                    DrawX(g, Integer.parseInt(winnerLocations[i]), true);
                }else{
                    DrawO(g, Integer.parseInt(winnerLocations[i]), true);
                }
            }
            playersTurn.setText("   " + Winner + " wins!   ");
        }
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

    public static void newGame(){
        //boolean playerOneTurn = firstMove(); for now its just going to be local (one pc) so this isnt used, but in the future it will be.
        resetValues();
        setUpGUI();
    }

    public static void resetValues(){
        for(int i = 0; i < GameState.length; i++){
            GameState[i] = "null";
        }
        Winner = "null";
        winningNumber = 0;
        victory = false;
        Move = 0;
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
                if(x > Integer.parseInt(location[0]) - 90 && x < Integer.parseInt(location[0]) + 90 
                && y > Integer.parseInt(location[1]) - 90 && y < Integer.parseInt(location[1]) + 90){
                    if(playerOneTurn == true){
                        GameState[i] = "X";
                        playerOneTurn = false;
                        Move++;
                        playersTurn.setText("   It is O's turn.   ");
                    }else{
                        GameState[i] = "O";
                        playerOneTurn = true;
                        Move++;
                        playersTurn.setText("   It is X's turn.   ");
                    }
                    checkWinConditions(GameState);
                }
            }
        }
    }

    public static void setUpGUI() {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(800, 600);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println(e.getX() + ", " + e.getY());
                if(Move <= 9){
                    userClick(e.getX(), e.getY());
                }
                frame.repaint();
            }
        });

        frame.getContentPane().add(new TicTacToe());

        frame.pack();
        frame.setVisible(true);
    }

    /** Checks the game state to see if a victory condition has been met */
    public static void checkWinConditions(String[] GameState){
        /** Rows */
        if(GameState[0] == GameState[1] && GameState[1] == GameState[2]){//Top row
            if(GameState[0] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 0;
            }else if(GameState[0] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 0;
            }
        }
        if(GameState[3] == GameState[4] && GameState[4] == GameState[5]){//Middle row
            if(GameState[3] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 1;
            }else if(GameState[3] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 1;
            }
        }
        if(GameState[6] == GameState[7] && GameState[7] == GameState[8]){//Bottom row
            if(GameState[6] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 2;
            }else if(GameState[6] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 2;
            }
        }
        /** Columns */
        if(GameState[0] == GameState[3] && GameState[3] == GameState[6]){//Left column
            if(GameState[0] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 3;
            }else if(GameState[0] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 3;
            }
        }
        if(GameState[1] == GameState[4] && GameState[4] == GameState[7]){//Middle column
            if(GameState[1] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 4;
            }else if(GameState[1] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 4;
            }
        }
        if(GameState[2] == GameState[5] && GameState[5] == GameState[8]){//Right column
            if(GameState[2] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 5;
            }else if(GameState[2] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 5;
            }
        }
        /** Diagonal */
        if(GameState[0] == GameState[4] && GameState[4] == GameState[8]){//Diagonal (right)
            if(GameState[0] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 6;
            }else if(GameState[0] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 6;
            }
        }if(GameState[2] == GameState[4] && GameState[4] == GameState[6]){//Diagonal (left)
            if(GameState[2] == "X"){
                victory = true;
                Winner = "X";
                winningNumber = 7;
            }else if(GameState[2] == "O"){
                victory = true;
                Winner = "O";
                winningNumber = 7;
            }
        }

        if(Move == 9 && victory == false){
            playersTurn.setText("   Its a draw!   ");
        }

        /* 
        Uncomment if you wish to print out the current state of the game when this method is called.
        
        for(int i = 0; i < GameState.length; i++){
            System.out.print(GameState[i] + " ");
            if(i == 2 || i == 5){
                System.out.println();
            }
        }
        System.out.println();
        */
        
    }


    /** Methods related to connection */
    static Thread hostThread = new Thread(){
        Socket socket = null;
        public void run(){
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                System.err.println("Waiting for someone to connect");
                socket = serverSocket.accept();
                System.err.println("Accepted connection on port " + port);
                TCPConnect connect = new TCPConnect(socket);
                connect.startReceiving();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("\nUsage: java TCPExample <port> [host]");
                running = false;
            }
        }
    };

    static Thread connectThread = new Thread(){
        public void run(){
            Socket socket = null;
            try{
                socket = new Socket("localhost", port);
                System.err.println("Connected to" + "localhost " +  "on port " + port);
                TCPConnect connect = new TCPConnect(socket);
                connect.startSending();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("\nUsage: java TCPExample <port> [host]");
                running = false;
            }
        }
    };
}

class TCPConnect{

    private PrintWriter output;
    private BufferedReader input;

    public TCPConnect(Socket socket) throws Exception {
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void startReceiving() throws Exception {
        while(running){
            String line;
            System.err.println(">>THEM:");
            if ((line = input.readLine()) != null) {
                System.out.println(line);
                updateGameState(line);
            }
            Scanner stdin = new Scanner(System.in);
            System.err.println(">>YOU:");
            if (stdin.hasNextLine()){
                String input = stdin.nextLine();
                updateGameState(input);
                output.println(input);
            }
        }
    }

    public void startSending() throws Exception {
        while(running){
            Scanner stdin = new Scanner(System.in);
            System.err.println(">>YOU:");
            if (stdin.hasNextLine()) {
                String input = stdin.nextLine();
                updateGameState(input);
                output.println(input);
            }
            String line;
            System.err.println(">>THEM:");
            if ((line = input.readLine()) != null) {
                System.out.println(line);
                updateGameState(line);
            }
        }
    }

    public void updateGameState(String newChange){
        if(playerOneTurn == true){
            GameState[Integer.parseInt(newChange)] = "X";
            playerOneTurn = false;
        }else{
            GameState[Integer.parseInt(newChange)] = "O";
            playerOneTurn = true;
        }

        
        System.out.println();
        for(int i = 0; i < 9; i++){
            System.out.print(GameState[i] + " ");
            if(i == 2 || i == 5){
                System.out.println();
            }
        }
        System.out.println();

    }

}