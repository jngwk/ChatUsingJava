package book_store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private String name;
    private String phoneNo;
    private String address;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private CartItem cartItem;
    
    public Client(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;

    }

    public Client(Socket socket, String name, String phoneNo){
        this.socket = socket;
        this.name = name;
        this.phoneNo = phoneNo;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void enterProgram(){
        // send over name and phoneNo
        try {
            bw.write(name + " " + phoneNo);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // loop
        Scanner sc = new Scanner(System.in);
        while(socket.isConnected()){
            try {
                String incomString;
                while((incomString = sc.nextLine()) != null){
                incomString = br.readLine();
                System.out.println(incomString);
                bw.write(incomString);
                bw.newLine();
                bw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sc.close();
    }
    
    
    // admin
    static class Admin extends Client{
        public static ArrayList<Admin> admins = new ArrayList<>();
        private String id;
        private int pwd;
        
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("관리자 계정 생성을 위해 아이디를 입력해주세요.");
            String inputId = sc.next();
            setId(inputId);
            System.out.println("ID: " + this.id);
            int inputPwd1;
            int inputPwd2;
            do{
            System.out.println("비밀번호를 입력해주세요.");
            inputPwd1 = sc.next().hashCode();
            System.out.println("비밀번호를 재입력해주세요.");
            inputPwd2 = sc.next().hashCode();
            if(inputPwd1 != inputPwd2){
                System.out.println("비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
            }
            }while(inputPwd1 != inputPwd2);
            setPwd(inputPwd1);;
            System.out.println("관리자 계정 생성이 완료되었습니다.");
        }

        public Admin(String name, String phoneNo){
            super(name, phoneNo);
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        
        public void setPwd(int pwd) {
            this.pwd = pwd;
        }

        public void resetPwd(){

        }

        public void addBook(File file){
            // implement method
        }


        public int getPwd() {
            return pwd;
        }
    }

    static class Member extends Client{
        public static ArrayList<Member> members = new ArrayList<>();
        
        public Member(String name, String phoneNo){
            super(name, phoneNo);
        }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("코난문고에 오신 것을 환영합니다.");
            System.out.println("성함을 입력해주세요");
            String name = sc.next();
            System.out.println("전화번호를 입력해주세요");
            String phoneNo = sc.next();
            Socket socket = new Socket("localhost", 9999);
            Client client = new Client(socket, name, phoneNo);
            client.enterProgram();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
