import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeServer{

    private static List<ClientHandler> clients = new LinkedList<ClientHandler>();
    public static int clientCounter = 0;
    public static int port = 1804;

    public static String[] GameState = {
        "null", "null", "null", 
        "null", "null", "null", 
        "null", "null", "null"
    };
    
    public static String[] VictoryPositions = {
        "012", "345", "678", "036", "147", "258", "048", "246"
    };

    public static int Move = 0;
    public static boolean victory = false;

    public static boolean playerOneTurn = true;//controls who the server is listening to (player one is true)

    public static int winningNumber;
    public static String Winner = "null";

    public static void main(String[] args){
        try{
            new TicTacToeServer().startServer(port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startServer(int port) throws Exception{

        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Tic Tac Toe Server started");

        while (true){
            if(clientCounter < 3){
                ClientHandler ch = new ClientHandler(serverSocket.accept());
                System.err.println("Accepted connection from " + ch);

                synchronized (clients){
                    clients.add(ch);
                }

                ch.start();
                clientCounter++;
            }
        }
    }

    public static void sendAll(String line, ClientHandler sender){
        System.err.println("Sending '" + line + "' to: " + clients);
        synchronized (clients){
            for (ClientHandler cl : clients){
                if(playerOneTurn == true){
                    cl.send(line);
                }else{
                    cl.send(line);
                }
            }
        }
        System.err.println(sender + " just played their turn");
    }

    public static void updateGameState(int position){
        if(GameState[position] == "null"){
            if(playerOneTurn == true){
                GameState[position] = "X";
                playerOneTurn = false;
            }else{
                GameState[position] = "O";
                playerOneTurn = true;
            }
        }

        /* prints overall gamestate */
        System.out.println();
        for(int i = 0; i < GameState.length; i++){
            System.err.print(GameState[i] + " ");
            if(i == 2 || i == 5){
                System.err.println();
            }
        }
        System.out.println("\n");

        checkWinConditions(GameState);

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

            if(victory){
                sendAll("Victory", clients.get(0));
                sendAll(VictoryPositions[winningNumber], clients.get(0));
                sendAll(Winner, clients.get(0));
            }

        }

    public static class ClientHandler extends Thread{

        private BufferedReader input;
        private PrintWriter output;
        private String id;

        private static int count = 0;

        public ClientHandler(Socket socket) throws Exception{
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            count++;
            id = "Player " + count;
        }

        public void send(String line){
            output.println(line);
        }

        public String toString(){
            return id;
        }

        public void run(){
            try{
                if(clients.get(0) == this){
                    send("Player1");
                }else if(clients.get(1) == this){
                    send("Player2");
                }
 
                String line;
                while ((line = input.readLine()) != null) {
                    if(!victory){
                        if(playerOneTurn == true){
                            if(this.equals(clients.get(0)) ){
                                updateGameState(Integer.parseInt(line));
                                sendAll(line, clients.get(0));
                            }
                        }else{
                            if(this.equals(clients.get(1)) ){
                                updateGameState(Integer.parseInt(line));
                                sendAll(line, clients.get(1));
                            }
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                synchronized (clients) {
                    clients.remove(this);
                }
                System.err.println(this + " closed connection!");
            }
        }
    }
}