import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

class Test {
	   public static void main(String[] args) {
		   GuiTest test = new GuiTest();
		   test.gui();
	   }
}

class GuiTest {
	
	   static JTextField text; 
	   static JTextField follow_num;
	   static JTextField userName;
	   static JTextField follower_list;
	   
	   public void gui(){
			JFrame mf = new JFrame();
			mf.setTitle("Twitter");
			mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			mf.setLayout(new FlowLayout()); 
			
			JButton btn = new JButton("Follow");
			text = new JTextField(20);
					
			btn.addActionListener(new MyActionListener());
			
			
			mf.add(btn);
			mf.add(text);
			
			mf.setSize(300, 150);
			mf.setVisible(true);
	   }
}

// 가지고 와야하는 정보
// 1. 유저 이름
// 2. 팔로우 정보
// 3. 메시지 정보

class MyActionListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			String a = GuiTest.text.getText();
			InfoJDBC so = new InfoJDBC();
			String name = so.getName(a);
			System.out.println(name);
			so.getFollower(a);
			so.getFollowing(a);
			so.getMessage(a);
		}
}

class InfoJDBC{
	Connection con;
	
	
	public InfoJDBC() {
		String url = "jdbc:mysql://127.0.0.1:3306/twitter";
		String userid="twitter";
	    String pwd="1234";
	    
	    try {
	    	con=DriverManager.getConnection(url, userid, pwd); 
		    System.out.println("MySQL connection ^O^ \n");
	    }
	    catch (SQLException e) {
	         e.printStackTrace();
	    }
	}
	
	
	public String getName(String me) {
		String userName = "";
		
		String query = "SELECT name FROM User WHERE id = \"" + me + "\";";
		try {
			Statement stmt=con.createStatement();
	  	  	ResultSet rs=stmt.executeQuery(query); 
	  	  	rs.next();
	  	  	userName = rs.getString(1);
	  	  	
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		return userName;
	}
	
	public String getFollower(String me) {
		String followerId = "";
		String followerName;
		String follower = "";
		
		String query = "SELECT follow_from FROM Follow WHERE follow_to = \"" + me + "\";";
		try {
			Statement stmt=con.createStatement();
	  	  	ResultSet rs=stmt.executeQuery(query); 
	  	  	
	  	  	while(rs.next()) {
	  	  		followerId = rs.getString(1);
	  	  		System.out.println("Follower: " + followerId);
	  	  		follower = follower + followerId + "@";
	  	  	}
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		
		
		// followerName = getName(followerId);
		System.out.println(follower);
		return follower;
	}
	
	public String getFollowing(String me) {
		String followingId = "";
		String followingName;
		String following = "";
		
		String query = "SELECT follow_to FROM Follow WHERE follow_from = \"" + me + "\";";
		try {
			Statement stmt2=con.createStatement();
	  	  	ResultSet rs2=stmt2.executeQuery(query); 
	  	  	
	  	  	while(rs2.next()) {
	  	  		followingId = rs2.getString(1);
	  	  		System.out.println("Following: " + followingId);
	  	  		following = following + followingId + "@";
	  	  	}
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		
		//followingName = getName(followingId);
		
		return following;
	}
	
	public String getMessage(String me) {
		String msg_to = "";
		String msg_from = "";
		String msg = "";
		String time = "";
		String total = "";
		
		String query = "SELECT * FROM message WHERE msg_to = \"" + me + "\";";
		try {
			Statement stmt=con.createStatement();
	  	  	ResultSet rs=stmt.executeQuery(query); 
	  	  	
	  	  	while(rs.next()) {
	  	  		msg_to = getName(rs.getString(1));
	  	  		msg_from = getName(rs.getString(2));
	  	  		msg = rs.getString(3);
	  	  		time = rs.getString(4);
	  	  		total = total + msg_to + "@" + msg_from + "@" + msg + "@" + time + "#";
	  	  	}
	  	  	
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		
		//System.out.println("msg :" + total);
		return total;
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
	
	public String getLoginInfo(String login_name) {
		String password = "";
//		System.out.println("name: " + login_name);
		String query = "SELECT password FROM user WHERE id = \"" + login_name + "\";";
		try {
			Statement stmt=con.createStatement();
	  	  	ResultSet rs=stmt.executeQuery(query); 

	  	  	rs.next();
	  	  	
	  	  	password = rs.getString(1);
//	  	  	System.out.println("password: "+ password);
	  	  	
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		
		return password;
	}
	
	public boolean check_following(String userId, String boardId) {
		String query = "SELECT follow_to FROM Follow WHERE follow_from = \"" + userId + "\"" + "and follow_to = \"" + boardId + "\";";
		try {
			Statement stmt=con.createStatement();
	  	  	ResultSet rs=stmt.executeQuery(query); 

	  	  	if(rs.next() == false) {
	  	  		return false;
	  	  	}
	  	  	
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		return true;
	}
	
	public void delete_follow(String userId, String boardId) {
		String query = "DELETE FROM Follow WHERE follow_to = \"" + boardId + "\" and follow_from = \"" + boardId + "\";" ;
		try {
			Statement stmt=con.createStatement();
	  	  	stmt.executeQuery(query); 
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
		
	}
	
	public void set_follow(String userId, String boardId) {
		String query = "INSERT INTO Follow VALUES(\"" + boardId + "\",\"" + userId + "\"" + ");";
		try {
			Statement stmt=con.createStatement();
	  	  	stmt.executeUpdate(query); 
		}
		catch(SQLException e) {
  	  	   e.printStackTrace();
  	    }
	}
}