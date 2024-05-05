package book_store;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import book_store.Client.Admin;

public class AdminProgram extends BookstoreProgram{
    
    public static ArrayList<Admin> admins = new ArrayList<>();
    int programPwd = 1;

    public AdminProgram(Socket socket) {
        super(socket);
        logIn();
        showMenu();
    }

    public void logIn(){
        
        try {
            bw.write("관리자 페이지에 접속하시기 위해 로그인해주세요. (계정 생성: /프로그램 비밀번호)\n");
            String incomString = br.readLine();
            if(incomString.equals("/"+programPwd)){
                bw.write("성함을 입력해주세요: ");
                bw.flush();
                String adminName = br.readLine();
                bw.write("전화번호를 입력해주세요: ");
                bw.flush();
                String adminPhoneNo = br.readLine();
                Admin newAdmin = new Admin(adminName, adminPhoneNo);
                admins.add(newAdmin);
            }
            else{
                String adminId;
                int adminPwd;
                int counter = 0;
                do{
                    bw.write("아이디 ");
                    bw.flush();
                    adminId = br.readLine();
                    bw.write("비밀번호: ");
                    bw.flush();
                    adminPwd = br.readLine().hashCode();
                    for(Admin admin : admins){
                        if(admin.getId().equals(adminId) && admin.getPwd() == adminPwd){
                            break;
                        }
                    }
                    bw.write("아이디 또는 비밀번호가 틀렸습니다. 다시 입력해주세요.\n");
                    bw.flush();
                    counter ++;
                }while(counter < 3);
                if(counter >= 3){
                    bw.write("시도 횟수를 초과하였습니다.");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMenu(){
        try {
            bw.write("1.도서 정보 추가 2.도서 정보 삭제 3.도서 정보 조회 0.종료");
            // TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBook(){
        try {
            
            bw.write("도서 정보를 추가하시겠습니까? Y/N");

            PrintWriter pw= new PrintWriter(new FileWriter(super.file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     

}
