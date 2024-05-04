package chat_copy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
        private Socket socket;
        private BufferedReader br;
        private BufferedWriter bw;
        private BufferedWriter stbw;
        private String name;
        
        public Client(Socket socket, String name) {
            try{
                this.socket = socket;
                this.name = name;
                this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.stbw = new BufferedWriter(new OutputStreamWriter(System.out));
            }catch(IOException e){
                e.getStackTrace();
            }
        }

        public void sendMessage(){
            try{
                // send name for cm to save
                bw.write(name);
                bw.newLine();
                bw.flush();

                // scan for msg
                Scanner sc = new Scanner(System.in);
                while(socket.isConnected()){
                    String messageToSend = sc.nextLine();
                
                    bw.write(messageToSend);
                    bw.newLine();
                    bw.flush();
                }
                sc.close();
            }catch(IOException e){
                closeAll(socket, br, bw);
            }
        }

        public void readMessage(){
            new Thread(new Runnable() {
                public void run(){
                    String msgFromChat;

                    while(socket.isConnected()){
                        try{
                            msgFromChat = br.readLine();
                            stbw.write(msgFromChat);
                            stbw.newLine();
                            stbw.flush();
                        }catch(IOException e){
                            closeAll(socket, br, bw);
                        }
                    }
                }
            }).start();
        }

        public void closeAll(Socket socket, BufferedReader br, BufferedWriter bw){
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

        public static void main(String[] args) {
            try{
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter your name:");
                String name = sc.nextLine();
                Socket socket = new Socket("localhost", 9999);
                Client client = new Client(socket, name);
                client.readMessage();
                client.sendMessage();
                sc.close();
            }catch(IOException e){
                e.getStackTrace();
            }
        }
        
}
