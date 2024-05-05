package book_store;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try {
            System.out.println("사용자를 기다리고 있습니다...");
            Socket socket = serverSocket.accept();
            System.out.println("사용자와 연결됐습니다.");
            ClientManager clientManager = new ClientManager(socket);
            Thread thread = new Thread(clientManager);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(9999);
            Server server = new Server(serverSocket);
            server.startServer();
        }catch(IOException e){

        }

    }
}
