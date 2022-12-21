import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * --------------
 * InfoJDBC
 * --------------
 * MySQL에서 정보를 가져오고 업데이트 함.
 * 
 * 1. getName
 * 입력: 아이디
 * 출력: 입력에 매칭되는 이름
 * 
 * 2. getFollower
 * 입력: 아이디
 * 출력: 입력한 아이디 주인을 팔로우하는 사람들의 아이디 
 * (* 각 아이디가 @으로 분리되어 있음.)
 * 
 * 3. getFollowing
 * 입력: 아이디
 * 출력: 입력한 아이디 주인이 팔로우하는 사람들의 아이디 
 * (* 각 아이디가 @으로 분리되어 있음.)
 * 
 * 4. getMessage
 * 입력: 아이디
 * 출력: 입력한 아이디 주인이 받은 메시지들 (요소: 받는 사람, 보낸 사람, 메시지 내용, 보낸 시각)
 * (* 각 요소는 @으로 분리되어 있음.)
 * (* 메시지가 여러 개인 경우, 각 메시지는 #으로 분리되어 있음.)
 * 
 * 5. convertNameToId 
 * 입력: 이름
 * 출력: 이름과 매칭되는 아이디
 * 
 * 6. getLoginInfo
 * 입력: 아이디
 * 출력: 아이디와 매칭되는 비밀번호
 * 
 * 7. check_following
 * 입력: 아이디1, 아이디2
 * 출력: True / False (아이디1이 아이디2를 팔로우하고 있는지 여부)
 * 
 * 8. delete_follow
 * 입력: 아이디1, 아이디2
 * 수행: 아이디1이 아이디2를 팔로우하고 있다는 정보를 데이터베이스에서 삭제
 * 
 * 9. set_follow
 * 입력: 아이디
 * 수행: 아이디1이 아이디2를 팔로우한다는 정보를 데이터베이스에 추가
 * 
 * */
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