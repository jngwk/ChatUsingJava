package my_chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // list of clients & server
    private Socket socket; // of server
    private BufferedReader br; // of server
    private BufferedWriter bw; // of server
    private String name; // of client

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));            
            clientHandlers.add(this);
        } catch (IOException e) {
            closeAll(br, bw, socket);
        }
    }

    @Override
    public void run() {        
        // initialization of name and initial broadcast
        try {
            if(this.name == null){
                this.name = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        broadcastMessage(String.format("%s has joined the chat!", this.name));
        
        // read incoming string
        while(socket.isConnected()){
            try{
                String messageToSend = br.readLine();
                sendMessageByType(messageToSend);
            }catch(IOException e){
                closeAll(br, bw, socket);
            }
        }
    }

    public void sendMessageByType(String messageToSend){
        // to catch anything with '/'
        String type = messageToSend.split(" ")[0];
        
        // regular messgae
        if(type.charAt(0) != '/'){
            messageToSend = name + ": " + messageToSend;
            broadcastMessage(messageToSend);
        }
        // quitChat
        else if(type.equals("/quit")){
            quitChat();
        }
        // whisper
        else{
            String possRecipient = type.substring(1);
            messageToSend = messageToSend.replace(type, "");
            for(ClientHandler clientHandler : clientHandlers){
                // match
                if(clientHandler.name.equals(possRecipient)){
                    messageToSend = name + " to You:" + messageToSend;
                    whisperMessage(messageToSend, clientHandler);
                    return;
                }
            }
            // no match
            try {
                bw.write("No such user");
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                closeAll(br, bw, socket);
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        try {
            // send to everyone except user
            for(ClientHandler clientHandler : clientHandlers){
                if(!clientHandler.name.equals(this.name)){
                    clientHandler.bw.write(messageToSend);
                    clientHandler.bw.newLine();
                    clientHandler.bw.flush();
                }
            }
        } catch (IOException e) {
            closeAll(br, bw, socket);
        }
    }
    
    public void whisperMessage(String messageToSend, ClientHandler recipient){
        try {
            // send to recipient
            recipient.bw.write(messageToSend);
            recipient.bw.newLine();
            recipient.bw.flush();
        } catch (IOException e) {
            closeAll(br, bw, socket);
        }
    }

    public void quitChat(){
        try {
            bw.write("You left the chat");
            bw.newLine();
            bw.flush();
            // safe word to exit program
            bw.write(String.valueOf("quit".hashCode()));
            bw.newLine();
            bw.flush();
            // message for everyone else
            broadcastMessage(String.format("%s has left the chat", name));
        } catch (IOException e) {
            closeAll(br, bw, socket);
        }
        clientHandlers.remove(this);
        closeAll(br, bw, socket);

    }

    public void closeAll(BufferedReader br, BufferedWriter bw, Socket socket){
        try{
            // close devices and socket
            if(br != null){
                br.close();
            }
            if(bw != null){
                bw.close();
            }
            if(socket.isConnected()){
                socket.close();
            }
        }catch(IOException e){
            e.getStackTrace();
        }
    }
}
