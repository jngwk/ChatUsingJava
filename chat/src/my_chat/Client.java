package my_chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String name;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    public Client(Socket socket, String name){
        this.socket = socket;
        try{
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.name = name;
        }catch(IOException e){
            closeAll(br, bw, socket);
        }
    }

    public void sendMessage(){
        try{
            // send name
            bw.write(name);
            bw.newLine();
            bw.flush();

            // use main thread to send message
            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = sc.nextLine();
                bw.write(messageToSend);
                bw.newLine();
                bw.flush();
            }
            sc.close();
        }catch(IOException e){
            closeAll(br, bw, socket);
        }
    }

    public void readMessage(){
        // declare new thread to read message
        new Thread(new Runnable(){
            public void run(){
                while(socket.isConnected()){
                    try {
                        String messageToRead = br.readLine();
                        // safe word to exit program
                        if(messageToRead.equals(String.valueOf("quit".hashCode()))){
                            closeAll(br, bw, socket);
                            break;
                        }
                        System.out.println(messageToRead);
                    } catch (IOException e) {
                        closeAll(br, bw, socket);
                    }
                }
            }
        }).start();
    }

    public void closeAll(BufferedReader br, BufferedWriter bw, Socket socket){
        try{
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


    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = sc.next();
            Socket socket = new Socket("localhost", 9999);
            Client client = new Client(socket, name);
            // write & read msg
            client.readMessage();
            client.sendMessage();
            sc.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
