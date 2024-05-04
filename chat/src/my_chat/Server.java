package my_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket socketOfServer;

    public Server(ServerSocket socketOfServer){
        this.socketOfServer = socketOfServer;
    }

    public void startServer(){
        try{
            System.out.println("Waiting for users to join...");
            // use main thread to connect clients
            while(!socketOfServer.isClosed()){
                Socket socket = socketOfServer.accept();
                System.out.println("New user has joined!");
                // start thread to handle messages
                new Thread(new ClientHandler(socket)).start();
            }
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    public static void main(String[] args){
        try {
            ServerSocket socketOfServer = new ServerSocket(9999);
            Server server = new Server(socketOfServer);
            server.startServer();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
