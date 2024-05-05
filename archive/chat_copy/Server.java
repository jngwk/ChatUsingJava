package chat_copy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // server socket
    private ServerSocket serverSocket;

    // constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void serverStart(){
        try{
            System.out.println("Waiting for others to join...");
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New Friend Connected!");
                // client manager
                ClientManager clientManager = new ClientManager(socket);

                // start thread
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        }catch(IOException e){
            e.setStackTrace(null);
        }
    }

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(9999);
            Server server = new Server(serverSocket);
            server.serverStart();
        }catch(IOException e){
            e.getStackTrace();
        }

    }
}
