package chat_copy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable{
    // array of clientManagers to manage
    public static ArrayList<ClientManager> clientManagers = new ArrayList<ClientManager>();
    public Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String name;
    
    public ClientManager(Socket socket) {
        try{
            // get info
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.name = br.readLine();
            
            // add to clientManagers
            clientManagers.add(this);
            broadcastMessage(" has joined the chat!");

        }catch(IOException e){}
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = br.readLine();
                sendMessageByType(messageFromClient);
            }catch(IOException e){
                closeAll(socket, br, bw);
                break;
            }
        }
    }

    public void sendMessageByType(String messageToSend){
        
        String type = messageToSend.split(" ")[0];
        
        if(type.equals("/quit")){
            removeClientManger();
        }
        else if(type.charAt(0) == '/'){
            String name = type.substring(1);
            messageToSend = messageToSend.replaceFirst(type+" ","");
            whisperMessage(name, ": " + messageToSend);
        }
        else{
            broadcastMessage(": " + messageToSend);
        }
    }

    public void whisperMessage(String name, String messgaeToSend){
        for(ClientManager clientManager : clientManagers){
            try{
                if(clientManager.name.equals(name)){
                    // write to corresponding client
                    clientManager.bw.write(this.name +" to You"+ messgaeToSend);
                    clientManager.bw.newLine();
                    clientManager.bw.flush();

                    // // write to writer
                    // bw.write("You to " + name + messgaeToSend);
                    // bw.newLine();
                    // bw.flush();
                    return;
                }
            }catch(IOException e){
                closeAll(socket, br, bw);
            }
        }
        // when not found
        try{
            bw.write(String.format("%s is not in the chat.", name));
            bw.newLine();
            bw.flush();
        }catch(IOException e){
            closeAll(socket, br, bw);
        }
    }
    
    public void broadcastMessage(String messageToSend){
        // send message to all clientManagers
        for(ClientManager clientManager: clientManagers){
            if(!clientManager.name.equals(name)){
                try{
                    clientManager.bw.write(this.name + messageToSend);
                    clientManager.bw.newLine();
                    clientManager.bw.flush();
                }catch(IOException e){
                    closeAll(socket, br, bw);
                }
            }
        }
    }

    public void removeClientManger(){
        clientManagers.remove(this);
        broadcastMessage(" has left the chat");
    }

    public void closeAll(Socket socket, BufferedReader br, BufferedWriter bw){
        removeClientManger();
        try {
            if(socket!=null){
                socket.close();
            }
            else if(br!=null){
                br.close();
            }
            else if(bw!=null){
                bw.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
