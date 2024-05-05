package book_store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import book_store.Client.Admin;
import book_store.Client.Member;

public class ClientManager implements Runnable{
    Socket socket;
    String clientType;
    String name;
    String phoneNo;
    protected BufferedReader br;
    protected BufferedWriter bw;

    public ClientManager(Socket socket){
        this.socket = socket;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book.loadBooks();        
    }

    @Override
    public void run() {
        String incomString;
        try {
            incomString = br.readLine();
            this.name = incomString.split(" ")[0];
            this.phoneNo = incomString.split(" ")[1];
            // check client type
            checkClientType(this.name, this.phoneNo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(socket.isConnected()){
            try {
                showMenu();
                incomString = br.readLine();
                checkCommand(incomString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            
    }

    public void checkClientType(String name, String phoneNo){
        for(Admin admin: Admin.admins){
            if(admin.getName().equals(name) && admin.getPhoneNo().equals(phoneNo)){
                this.clientType = "admin";
                break;
            }
        }
        for(Member member: Member.members){
            if(member.getName().equals(name) && member.getPhoneNo().equals(phoneNo)){
                this.clientType = "member";
                break;
            }
        }

        try {
            if(this.clientType == "admin"){
                bw.write(String.format("%s 관리자님, 환영합니다.\n", name));
                bw.flush();
            }
            else if(this.clientType == "member"){
                bw.write(String.format("%s 회원님, 환영합니다.\n", name));
                bw.flush();
            }
            else{
                bw.write(String.format("%s 님, 환영합니다.\n", name));
                bw.flush();
                this.signUp();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    public void signUp(){
        try {
            bw.write("회원 등록을 하시겠습니까? Y/N\n ");
            bw.flush();
            if(br.readLine().equalsIgnoreCase("y")){
                this.clientType = "member";
                bw.write("회원 등록이 완료되었습니다.\n");
                bw.flush();
            }
            else{
                bw.write("회원 등록이 취소되었습니다.\n");
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMenu(){
        try{
            bw.write("오늘의 선택, 코난문고\n");
            bw.write("영원한 스테디셀러, 명탐정 코난시리즈를 만나보세요~\n");
            bw.write("******************************************************\n");
            bw.write("1.고객 정보 확인하기 2.장바구니 상품 목록 보기\n");
            bw.write("3.바구니에 항목 추가하니 4.장바구니의 항목 삭제하기\n");
            bw.write("5.장바구니 비우기 6.영수증 표시하기\n");
            bw.write(("0.프로그램 종료"));
            bw.write("******************************************************\n");
            bw.write("메뉴 번호를 선택해주세요: ");
            bw.flush();
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    public void checkCommand(String incomString){
        int menu = Integer.parseInt(incomString);
        if(incomString.equals("/admin")){
            AdminProgram adminProgram = new AdminProgram(socket);
            executeAdminProgram(adminProgram);
        }
        else{
            try {
                while(menu<0 && menu>6){
                    bw.write("잘못 입력하셨습니다. 다시 시도해주세요.\n");
                    bw.write("메뉴 번호를 선택해주세요: ");
                    bw.flush();
                    menu = Integer.parseInt(br.readLine());
                }

                if(menu == 1){
                    checkClientInfo();
                }
                else if(menu == 2){
                    if(clientType == null){
                        bw.write("회원인 고객님만 이용 할 수 있는 기능입니다.\n");
                        bw.flush();
                        signUp();
                    }
                    if(clientType != null){
                        checkCartItem();
                    }
                }
                else if(menu == 3){

                }
                else if(menu == 4){

                }
                else if(menu == 5){

                }
                else if(menu == 6){

                }
                else if(menu == 0){

                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkClientInfo(){
        String type;
        if(this.clientType == "admin"){
            type = "관리자";
        }
        else if(this.clientType == "member"){
            type = "회원";
        }
        else{
            type = "비회원";
        }
        try {
            bw.write("현재 고객 정보:\n");
            bw.write(String.format("성함: %s\n연락처: %s\n등급: %s\n", this.name, this.phoneNo, type));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkCartItem(){
        
    }

    public void executeAdminProgram(AdminProgram adminProgram){

    }
}
