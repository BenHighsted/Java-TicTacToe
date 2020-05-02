import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeServer{

    private static List<ClientHandler> clients = new LinkedList<ClientHandler>();
    public static int clientCounter = 0;

    public static String[] GameState = {
        "null", "null", "null", 
        "null", "null", "null", 
        "null", "null", "null"
    }; 

    public static void main(String[] args){
        try{
            new TicTacToeServer().startServer(Integer.parseInt(args[0]));
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("\nUsage: java TicTacToeServer <port>");
        }
    }

    public void startServer(int port) throws Exception{

        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Tic Tac Toe Server started");

        while (true){
            if(clientCounter < 2){
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
                cl.send(sender + ": " + line);
            }
        }
    }

    public static void updateGameState(int position){
        GameState[position] = "X";
        //Will need if statement to know if it needs to be X or O, for now just X for testing.

        //prints overall gamestate
        for(int i = 0; i < GameState.length; i++){
            System.err.print(GameState[i] + " ");
            if(i == 2 || i == 5){
                System.err.println();
            }
        }
        System.out.println();
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
                send("You are " + this + ".");
                String line;
                while ((line = input.readLine()) != null) {
                    //sendAll(line, this);
                    updateGameState(Integer.parseInt(line));
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