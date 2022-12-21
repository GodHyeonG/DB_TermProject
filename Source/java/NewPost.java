import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.*;

public class NewPost extends JFrame {
    // Font
    Font f1 = new Font("나눔고딕",Font.BOLD,17); 
    Font f2 = new Font("나눔고딕",Font.PLAIN,13);
    Font f3 = new Font("나눔고딕", Font.ITALIC, 17);

    // New Post 창
    NewPost(String userId){

        // title
        JLabel lb1 = new JLabel("NEW POST");
        lb1.setBounds(50,35,100,30);
        lb1.setFont(f1);

        // 이미지(뱅기뱅기)
        JLabel imgLabel = new JLabel();
        ImageIcon icon  = new ImageIcon("send.png");
        Image img = icon.getImage();
        Image updateImg = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon updateIcon = new ImageIcon(updateImg);

        imgLabel.setIcon(updateIcon);

        imgLabel.setBounds(12,35,30,30);

        getContentPane().add(imgLabel);


        // 입력창
        JTextArea jt1 = new JTextArea(1,1);
        jt1.setBounds(50, 75, 460, 500);
        jt1.setFont(f2);

        // complete & time
        JLabel lb2 = new JLabel();
        lb2.setBounds(50,600,300,30);

        lb2.setFont(f1);

        // Send button
        JButton bt1 = new JButton("Send");
        bt1.setBounds(400, 600, 100, 30);
        bt1.setFont(f1);

        // button click -> 완료 & 시간 문구 생성
        bt1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // 시간 날짜 가져오고 저장
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date = dateFormat.format(new Date());

                // TextArea 내용 저장
                String getmes = jt1.getText();

                // 시간 db 저장
                
                
                // 데이터베이스 보내기
     		    PostListJDBC so=new PostListJDBC();
     	  	    if(so.add(userId, getmes, date) == 1) {
     	  	    	lb2.setText("complete!  " + date.format(date));
     	  	    	lb2.setFont(f3);
     	  	    	lb2.setForeground(Color.blue);
     	  	    }
     	  	    else if(so.add(userId, getmes, date) == 0) {
	  	  	    	lb2.setText("Message must be started with @id");
	  	  	    	lb2.setFont(f3);
	  	  	    	lb2.setForeground(Color.blue);
	  	  	    }
     	  	    else {
     	  	    	lb2.setText("No such id");
     	  	    	lb2.setFont(f3);
     	  	    	lb2.setForeground(Color.blue);
     	  	    }
     	  	    
	     	  	 

            }

        });

        //add
        add(jt1);
        add(lb1);
        add(bt1);
        add(lb2);

        //전체 창 가운데 고정 표시
        Dimension frameSize = new Dimension(600, 700);
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation((windowSize.width - frameSize.width)/2,
                (windowSize.height - frameSize.height)/2);

        setTitle("NEW POST");   //창 이름
        setSize(600,700);   //전체 창 크기

        
        setLayout(null);
        setVisible(true);


    }



    public static void main(String args[]){

        new NewPost("id");

    }

}

class PostListJDBC {

	   Connection con;
	   int number;
	   
	   String to_id;
	   String to_name;
	   String from_id;
	   String msg;
	   
	   public PostListJDBC() {
	     String url="jdbc:mysql://localhost:3306/twitter"; 
	     String userid="twitter";
	     String pwd="1234";
	  
	     try { 	
	       con=DriverManager.getConnection(url, userid, pwd); 
	       System.out.println("MySQL connection ^O^ \n");
	     } catch(SQLException e) {
	         e.printStackTrace();
	       }
	   }
	   
	   public String convertNameToId(String myName) {
		   
			String userId = "";
			
			String query = "SELECT id FROM user WHERE name = \"" + myName + "\";";
			try {
				Statement stmt=con.createStatement();
		  	  	ResultSet rs=stmt.executeQuery(query); 
		  	  	rs.next();
		  	  	userId = rs.getString(1);
		  	  	
			}
			catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	    }
			
			return userId;
		}
	   
	   public int add(String from_id, String a, String date) {
		   if (a.startsWith("@")) {
			   a = a.substring(1);
			   int idx = a.indexOf(" ");
			   to_id = a.substring(0, idx);
			   System.out.println("to_id = " + to_id +" \n");
			   msg = a.substring(idx+1);
			   System.out.println("msg = " + msg +" \n");
		   }
		   else {
			   return 0;
		   }
		   
		   String query="select id from user where id = \"" + to_id + "\"";
		   String search = "";
		   try {
	  	  	 Statement stmt=con.createStatement(); 
	  	  	 ResultSet rs =stmt.executeQuery(query);
	  	  	 rs.next();
	  	  	 search = rs.getString(1);
	  	  	
	  	   } 
	  	   catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	   }
		   if (search.equals("")) {
			   System.out.println("No such name!");
			   return 2;
		   }
		   
		   String query2="INSERT INTO message VALUES(\"" + to_id + "\",\"" + from_id + "\", " + "\"" + msg + "\""+ ", " + "\"" + date + "\""+ ")"; 
	  	   try {
	  	  	 Statement stmt=con.createStatement(); 
	  	  	 stmt.executeUpdate(query2);
	  	  	 con.close();
	  	   } 
	  	   catch(SQLException e) {
	  	  	   e.printStackTrace();
	  	   }
	  	   return 1;
	   }
	}