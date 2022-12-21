
import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

class SignUp {
   
   //액션리스너에서 사용해야 할 변수들은 전역으로 선언
   static JFrame signUp;
   static TextField id, pw, pw2, name;
   static JLabel errorLb;
   
   //트위터 색
   Color T_blue = new Color(61, 156, 214);
   
   public SignUp() {
      
      //임시폰트 - 나중에 폰트 통일
      Font font = new Font("한컴 고딕", Font.PLAIN, 13);
      Font btnFont = new Font("한컴 고딕", Font.BOLD, 15);

      signUp = new JFrame();   
      
      //JPanel로 어느 위치에 어느 요소 넣을지 레이아웃 나눠줌  -------------------------------
      
      JPanel inputPanel1 = new JPanel();
      JPanel inputPanel2 = new JPanel();
      JPanel inputPanel3 = new JPanel();
      JPanel inputPanel4 = new JPanel();
      JPanel btnPanel = new JPanel();
      JPanel space1 = new JPanel(); //빈공간
      JPanel space2 = new JPanel(); //빈공간
      
      
      //패널 위치, 색 지정 -------------------------------------------------------

      
      btnPanel.setLayout(new FlowLayout());
      btnPanel.setBackground(Color.white);

      
      inputPanel1.setLayout(new BorderLayout());
      inputPanel1.setPreferredSize(new Dimension(300, 25));
      inputPanel1.setLocation(5, 50);
      inputPanel1.setBackground(Color.white);

      
      inputPanel2.setLayout(new BorderLayout());
      inputPanel2.setPreferredSize(new Dimension(300, 25));
      inputPanel2.setBackground(Color.white);
      
      inputPanel3.setLayout(new BorderLayout());
      inputPanel3.setPreferredSize(new Dimension(300, 25));
      inputPanel3.setBackground(Color.white);
      
      inputPanel4.setLayout(new BorderLayout());
      inputPanel4.setPreferredSize(new Dimension(300, 25));
      inputPanel4.setBackground(Color.white);
      
      space1.setPreferredSize(new Dimension(300, 20));
      space2.setPreferredSize(new Dimension(300, 30));
      space1.setBackground(Color.white);
      space2.setBackground(Color.white);
      

      //라벨 생성 후 세팅 --------------------------------------------------------
      
      
      Label idLb = new Label("ID:");
      Label pwLb = new Label("PASSWORD:");
      Label pwLb2 = new Label("Confirm PASSWORD:");
      Label nameLb = new Label("NAME:");
      errorLb = new JLabel("                                                   "); //에러메세지 띄울 라벨, 미리 안보이는 값으로 지정
      
      idLb.setFont(font);
      pwLb.setFont(font);
      pwLb2.setFont(font);
      nameLb.setFont(font);
      errorLb.setForeground(Color.red);
      errorLb.setHorizontalAlignment(JLabel.CENTER);
      errorLb.setFont(font);
      
      
      // 회원정보를 받아올 텍스트창 생성 ------------------------------------------------
      
      
      id = new TextField(20);
      pw = new TextField(20);
      pw2 = new TextField(20);
      name = new TextField(20);
      
      id.setBounds(200, 0, 9, 10);
      
      pw.setEchoChar('*'); //비밀번호 창은 *으로만 글자가 보이게 설정
      pw2.setEchoChar('*');
      
      
      // 확인 취소 버튼 생성 -------------------------------------------------------
      

      JButton nextBtn = new JButton("NEXT");
      JButton exitBtn = new JButton("QUIT");
      
      nextBtn.setBackground(T_blue);
      exitBtn.setBackground(T_blue);
      nextBtn.setForeground(Color.white);
      exitBtn.setForeground(Color.white);
      nextBtn.setBorder(new EmptyBorder(7, 20, 7, 20));
      exitBtn.setBorder(new EmptyBorder(7, 20, 7, 20));
      nextBtn.setFont(btnFont);
      exitBtn.setFont(btnFont);
      
      nextBtn.addActionListener(new nextBtnActionListener());
      exitBtn.addActionListener(new exitBtnActionListener());
      
      
      // JFrame에 차례대로 배치 ----------------------------------------------------

      
      signUp.add(space1);
         
      signUp.add(inputPanel1);
      inputPanel1.add(idLb);
      inputPanel1.add(id, BorderLayout.EAST);
      
      signUp.add(inputPanel2);
      inputPanel2.add(pwLb);
      inputPanel2.add(pw, BorderLayout.EAST);
      
      signUp.add(inputPanel3);
      inputPanel3.add(pwLb2);
      inputPanel3.add(pw2, BorderLayout.EAST);
      
      signUp.add(inputPanel4);
      inputPanel4.add(nameLb);
      inputPanel4.add(name, BorderLayout.EAST);
      
      signUp.add(space2, BorderLayout.CENTER);
      space2.add(errorLb);
      
      signUp.add(btnPanel);
      btnPanel.add(nextBtn);
      btnPanel.add(exitBtn);
      
      
      // 창 설정 -----------------------------------------------------------------
      
      
      Dimension frameSize = new Dimension(340, 285);
      Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
      
      
      signUp.setTitle("SIGN UP");
      signUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      signUp.setLayout(new FlowLayout());
      signUp.setBackground(new Color(61, 156, 214));
      signUp.setSize(frameSize);
      signUp.setVisible(true);
      signUp.getContentPane().setBackground(Color.white);
      signUp.setLocation((windowSize.width - frameSize.width)/2,
            (windowSize.height - frameSize.height)/2); //화면 정중앙에 뜨도록 함
      

   
   }
   
   //확인 버튼 눌렀을 때 호출
   class nextBtnActionListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         
         SignListJdbc list = new SignListJdbc();
         
         String ID = SignUp.id.getText();
         String PW = SignUp.pw.getText();
         String PW2 = SignUp.pw2.getText();
         String NAME = SignUp.name.getText();
         
         
         //제대로 된 값이 들어갔는지 확인해줄 bool
         boolean pw_right = false;
         boolean id_right = false;
         boolean name_right = false;
         
         
         //에러메세지 빨간색으로 설정
         SignUp.errorLb.setForeground(Color.red);
         
         
         
         if(!PW.equals(PW2)) { //비밀번호 확인이 틀렸을 때
            pw_right = false;
         }
         
         else if(PW.length() > 15) { //비밀번호가 15글자가 넘어갈 때
            pw_right = false;
         }
         
         else if(PW2.length() > 15) { //비밀번호가 15글자가 넘어갈 때
            pw_right = false;
         }
         
         else if(PW.equals("")||PW2.equals("")) { //비밀번호를 입력이 안됐을 때
            pw_right = false;
         }
         
         else{
            pw_right = true;
         }
         
         
         
         
         if(ID.equals("")) { //아이디 입력이 안됐을 떄
            SignUp.id.setText("Please enter the ID.");
            id_right = false;
         }
         
         else if(ID.length() > 15) { //아이디 길이가 15글자를 넘어갈때
            SignUp.id.setText("Please enter up to 15 characters.");
            id_right = false;
         }
         
         else
            id_right = true;
         
         
         
         
         
         if(NAME.equals("")) { //이름이 입력되지 않았을 떄
            SignUp.name.setText("Please enter the name");
            name_right = false;
         }
         
         else if(NAME.length() > 15) { //이름 길이가 15글자를 넘어갈 때
            SignUp.name.setText("Please enter under 15 characters.");
            name_right = false;
         }
         
         else
            name_right = true;
         
         
         
         
         //예외사항 처리
         
         if(!id_right) {
            SignUp.errorLb.setText("Please check the ID.");
         }
         
         else if(!pw_right) {
            SignUp.errorLb.setText("Please check the PASSWORD.");
         }
         
         else if(!name_right) {
            SignUp.errorLb.setText("Please check the NAME.");
         }
         
         
         
         //예외사항 없이 모든 값이 잘 들어갔을 경우 insert
         
         else {
            SignUp.errorLb.setText(" ");
            list.insert(ID, PW, NAME);
         }
         
      }
   }
   
   //취소버튼 누를 시 창 닫기
   class exitBtnActionListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         SignUp.signUp.dispose();
      }
   }
   
   //JDBC 연결
   class SignListJdbc{
      Connection con;
      Statement stmt;
      
      SignListJdbc(){
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
      
      
      //insert하려는 ID가 이미 있는지 확인
      public boolean check(String id, String pw, String name) {
         
         String query = "SELECT * from user WHERE id=\""+id+"\"";
         boolean ischeck = false;
         
         try {
            
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery(query);
            
            //검색결과가 존재한다면 check true
            if(rs.next()) {
               ischeck = true;
            }
         }
         
         catch (SQLException e) {
            e.printStackTrace();
         }
         
         return ischeck;
      }
      
      
      //insert해주는 함수
      public void insert(String id, String pw, String name) {
         
         boolean ischeck;
           ischeck = check(id, pw, name);//중복되는지 확인
           
           String query = "insert into user values(\"" + id + "\", \"" + name + "\", \"" + pw+ "\")";
           
           //중복되지 않을 시 데이터베이스에 추가
           if(!ischeck) {
              try {
               
               PreparedStatement st = con.prepareStatement(query);

               st.executeUpdate();
               
               SignUp.errorLb.setForeground(T_blue);
               SignUp.errorLb.setText("Your ID is successfully registered.");
               
               System.out.println("register new customer "+id);
               
            }
            
            catch (SQLException e) {
               e.printStackTrace();
            }
           }
           
           //중복되면 오류메세지 출력
           else
              SignUp.errorLb.setText("You are already registered.");
      }
      
   }
   
}
