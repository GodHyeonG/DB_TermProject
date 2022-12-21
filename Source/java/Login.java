import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class Login extends JFrame {
   
   Font f1 = new Font("Tahoma", Font.PLAIN, 15);
   Color T_blue = new Color(61, 156, 214);
   
   Dimension frameSize = new Dimension(320, 250);
    Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
   
   Login() {
      
      setTitle("Twitter - Login");
      setLayout(null);
      setSize(frameSize);
      setLocation((windowSize.width - frameSize.width)/2,
               (windowSize.height - frameSize.height)/2); //화면 정중앙에 뜨도록 함
      
      JPanel pn = new JPanel();
      pn.setSize(320, 250);
      pn.setBackground(Color.white);
      
      JLabel lb = new JLabel();
      lb.setBounds(20, 200, 300, 50);
      
      JLabel id = new JLabel("ID");
      id.setBounds(40, 40, 25, 30);
      id.setFont(f1);
      
      TextField idField = new TextField();
      idField.setBounds(70, 40, 120, 30);
      idField.setFont(f1);
      
      JLabel pw = new JLabel("PW");
      pw.setBounds(40, 90, 25, 30);
      pw.setFont(f1);
      
      TextField pwField = new TextField();
      pwField.setBounds(70, 90, 120, 30);
      pwField.setEchoChar('*'); //비밀번호 창은 *으로만 글자가 보이게 설정
      
      JButton login = new JButton("Login");
      login.setBounds(200, 65, 70, 30);
      login.setFont(f1);
      login.setBackground(T_blue);
      login.setForeground(Color.white);
      
      JButton signUp = new JButton("Sign up");
      signUp.setBounds(50, 140, 85, 30);
      signUp.setFont(f1);
      signUp.setBackground(T_blue);
      signUp.setForeground(Color.white);
      
      JButton changePW = new JButton("Change PW");
      changePW.setBounds(150, 140, 110, 30);
      changePW.setFont(f1);
      changePW.setBackground(T_blue);
      changePW.setForeground(Color.white);
      
      
      add(lb); 
      add(id); add(idField);
      add(pw); add(pwField);
      add(login); add(signUp); add(changePW); add(pn);
      setVisible(true);
      
//      String ID = idField.getText();
//      String PW = pwField.getText();
      
      login.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 String ID = idField.getText();
             String PW = pwField.getText();
        	 
         	InfoJDBC info = new InfoJDBC();
         	
        	 if (info.getLoginInfo(ID) == "") {
        		 JOptionPane.showMessageDialog(login, "아이디를 확인하세요.");
        	 }
        	 else {
        		 System.out.println("getLoginInfo(ID): " + info.getLoginInfo(ID));
        		 System.out.println("PW: " + PW);
        		 
        		 if(info.getLoginInfo(ID).equals(PW)) {
        			 new PersonalBoard(ID, ID);
        			 
        		 }
        		 else {
        			 JOptionPane.showMessageDialog(login, "비밀번호가 올바르지 않습니다.");
        		 }
        	 }
         }
         
      });
      
      /**
       * 새창 열어서 회원가입하기
       */
      signUp.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SignUp signUp = new SignUp();
         }
         
      });
      
      /**
       * 새창 열어서 아이디&기존 비밀번호 입력한 후, 새로운 비밀번호 입력하기
       */
      changePW.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ChangePassword();
         }
         
      });
   }
   
   private void ChangePassword() {
      JFrame cPW = new JFrame();
      cPW.setTitle("Change Password");
      cPW.setBounds(40, 30, 300, 320);
      cPW.setLayout(null);
      cPW.setLocation((windowSize.width - frameSize.width)/2,
               (windowSize.height - frameSize.height)/2); //화면 정중앙에 뜨도록 함
      
      JPanel pn = new JPanel();
      pn.setSize(300, 320);
      pn.setBackground(Color.white);
      
      JLabel lb = new JLabel();
      lb.setBounds(20, 200, 300, 50);
      
      JLabel id = new JLabel("ID");
      id.setBounds(40, 40, 25, 30);
      id.setFont(f1);
      
      JTextField idField = new JTextField();
      idField.setBounds(105, 40, 120, 30);
      idField.setFont(f1);
      
      JLabel pw = new JLabel("PW");
      pw.setBounds(40, 80, 25, 30);
      pw.setFont(f1);
      
      TextField pwField = new TextField();
      pwField.setBounds(105, 80, 120, 30);
      pwField.setEchoChar('*'); //비밀번호 창은 *으로만 글자가 보이게 설정
      
      JLabel newPW = new JLabel("New PW");
      newPW.setBounds(40, 135, 60, 30);
      newPW.setFont(f1);
      
      TextField newPwField = new TextField();
      newPwField.setBounds(105, 135, 120, 30);
      
      JLabel confirmPW = new JLabel("Confirm");
      confirmPW.setBounds(40, 175, 60, 30);
      confirmPW.setFont(f1);
      
      TextField confirmPwField = new TextField();
      confirmPwField.setBounds(105, 175, 120, 30);
      
      JButton change = new JButton("Change");
      change.setBounds(95, 230, 90, 30);
      change.setFont(f1);
      change.setBackground(T_blue);
      change.setForeground(Color.white);
      
      cPW.dispose(); //현재 창만 닫기
      cPW.add(lb);
      cPW.add(id); cPW.add(idField);
      cPW.add(pw); cPW.add(pwField);
      cPW.add(newPW); cPW.add(newPwField); 
      cPW.add(confirmPW); cPW.add(confirmPwField);
      cPW.add(change); cPW.add(pn);
      cPW.setVisible(true);
      
      change.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 String id = idField.getText();
        	 String pw = pwField.getText();
        	 String newPw = newPwField.getText();
        	 InfoJDBC info = new InfoJDBC();
        	 
        	 if(info.getName(id).equals("")){
        		 JOptionPane.showMessageDialog(change, "아이디를 확인하세요.");
        		 return;
        	 }
        	 if(!info.getLoginInfo(id).equals(pw)){
        		 JOptionPane.showMessageDialog(change, "기존의 비밀번호가 올바르지 않습니다.");
        		 return;
        	 }
        	 if(!newPw.equals(confirmPwField.getText())){
        		 JOptionPane.showMessageDialog(change, "변경할 비밀번호가 일치하지 않습니다.");
        		 return;
        	 }
        	 
        	 
        	 ChangePwJdbc jdbc = new ChangePwJdbc(); 
        	 jdbc.changePw(id, newPw);
        	 JOptionPane.showMessageDialog(change, "성공적으로 변경되었습니다.");
         }
         
      });
      
 
   }
   
   public static void main(String[] args) {
      new Login();
   }
}

//JDBC 연결
class ChangePwJdbc{
   Connection con;
   Statement stmt;
   
   ChangePwJdbc(){
      String url = "jdbc:mysql://localhost:3306/twitter";
      String userid = "twitter";
      String pwd = "1234";
   
      try {
         con = DriverManager.getConnection(url, userid, pwd);
         stmt = con.createStatement();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   
   }
   

   public void changePw(String id, String newPw) {
        
        String query = "UPDATE user SET password = \"" + newPw + "\" WHERE id = \"" + id +"\";";
        

       try {
      	 Statement stmt=con.createStatement();

      	 stmt.executeUpdate(query);
            
            
         }
         
         catch (SQLException e) {
            e.printStackTrace();
         }
        }
        
   
}