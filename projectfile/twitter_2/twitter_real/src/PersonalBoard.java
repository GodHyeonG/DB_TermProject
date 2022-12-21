import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonalBoard extends JFrame {
	
	String login_;
    JPanel messagePn;
    int vertical, messageNum;

    Color T_blue = new Color(61, 156, 214);

    //Font
    Font font = new Font("한컴 고딕", Font.PLAIN, 15);
    Font f1 = new Font("한컴 고딕", Font.BOLD, 40);
    Font f2 = new Font("SanSerif", Font.PLAIN, 20);
    Font f3 = new Font("한컴 고딕", Font.PLAIN,25);
    Font f4 = new Font("한컴 고딕", Font.BOLD,30);
    Font f5 = new Font("한컴 고딕",Font.BOLD,17);    //titile
    Font f6 = new Font("한컴 고딕",Font.PLAIN,13);  //text area
    Font f7 = new Font("한컴 고딕", Font.ITALIC, 17);
    
    String userName;
    String userId;
    String boardId;
    
    //개인보드 기본 창
    PersonalBoard(String cur_id, String board_id) {
    	userId = cur_id;
    	boardId = board_id;
    	InfoJDBC info = new InfoJDBC();
    	
    	userName = info.getName(board_id);
    	
    			
        Dimension frameSize = new Dimension(770, 950);
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Twitter - Personal Board");
        setLayout(null);
        setLocation((windowSize.width - frameSize.width)/2,
                (windowSize.height - frameSize.height)/2); //화면 정중앙에 뜨도록 함
        setSize(frameSize);

        vertical = 540;
        messageNum = 0;

        JPanel pn1 = new JPanel();
        pn1.setBounds(0, 0, 770, 950);
        pn1.setBackground(Color.white);

        ImageIcon profile = new ImageIcon("image/profile.jpg");
        JPanel pn2 = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(profile.getImage(), 0, 0, 200, 200, null);
            }
        };
        pn2.setBounds(60, 60, 200, 200);
        add(pn2);

        
        JLabel id = new JLabel(userName /* id database */);
        id.setBounds(300, 130, 200, 55);
        id.setFont(f1);
        add(id);

        //팔로워 버튼
        JButton bt1 = new JButton("Follower");
        bt1.setBounds(300,210,130,30);
        bt1.setBorder(new EmptyBorder(0, 0, 0, 0));
        bt1.setBackground(T_blue);
        bt1.setForeground(Color.white);
        bt1.setFont(f2);
        bt1.setFocusPainted(false);
        bt1.addActionListener(new MyActionListener()); //누르면 새 창
        add(bt1);

        //팔로잉 버튼
        JButton bt2 = new JButton("Following");
        bt2.setBounds(460,210,130,30);
        bt2.setBorder(new EmptyBorder(0, 0, 0, 0));
        bt2.setBackground(T_blue);
        bt2.setForeground(Color.white);
        bt2.setFont(f2);
        bt2.setFocusPainted(false);
        bt2.addActionListener(new MyActionListener2()); //누르면 새 창
        add(bt2);

        //검색
        ImageIcon s1 = new ImageIcon("image/search.jpg");
        Image s2 = s1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon search = new ImageIcon(s2);
        JButton bt3 = new JButton(s1);
        bt3.setBounds(620, 210, 30, 30);
        bt3.setBorder(new EmptyBorder(0, 0, 0, 0));
        bt3.setFocusPainted(false);
        bt3.setBackground(T_blue);
        add(bt3);

        //누르면 검색하는 창
        bt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Search(cur_id, board_id);
            }
        });
        

        //NewPost 열기 button
        JButton bt4 = new JButton("New post");
        bt4.setBounds(560,260,150,30);
        bt4.setBorder(new EmptyBorder(0, 0, 0, 0));
        bt4.setBackground(T_blue);
        bt4.setForeground(Color.white);
        bt4.setFont(f2);
        bt4.setFocusPainted(false);
        bt4.addActionListener(new MyActionListener3());
        add(bt4);
        

        // 메세지 패널
        messagePn = new JPanel();
        messagePn.setBounds(25, 300, 705, 540);
        messagePn.setBackground(T_blue);
        messagePn.setPreferredSize(new Dimension(680, vertical));

        JScrollPane scrollPane = new JScrollPane(messagePn, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(25, 300, 704, 540);
        messagePn.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        add(scrollPane);
        
        String message = info.getMessage(cur_id);
        
        String[] messages = message.split("#");
        
        String msg = "";
        String[] msgSplit = {};
        for (int i = 0; i < messages.length; i++) {
        	msg = messages[i];
        	msgSplit = msg.split("@");
        	if (msgSplit[0] == "") {
        		System.out.println("no message");
        		break;
        	}
        	addMessage(msgSplit[0], msgSplit[3], msgSplit[1], msgSplit[2]);
        	
        }
        

        // 팔로우/언팔로우 버튼
        // cur_id와 id가 같지 않을 때만
        if(!cur_id.equals(board_id)) {
        	JButton bt5;
        	
        	// 만약 아직 팔로우하지 않은 상태라면,
        	if(info.check_following(cur_id, board_id)) {
        		bt5 = new JButton("Unfollow");
        	}
        	// 만약 이미 팔로우 상태라면
        	else {
        		bt5 = new JButton("Follow");
        	}
        	bt5.setBounds(50,210,130,30);
        	bt5.setBorder(new EmptyBorder(0, 0, 0, 0));
        	bt5.setBackground(T_blue);
        	bt5.setForeground(Color.white);
        	bt5.setFont(f2);
        	bt5.setFocusPainted(false);
        	//bt5.addActionListener(new MyActionListener4()); //누르면 새 창
            add(bt5);
            
            
            bt5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	if(info.check_following(userId, boardId)) {
                		info.delete_follow(userId, boardId);
                    	bt5.setText("Follow");
                    }
                    else {
                    	info.set_follow(userId, boardId);
                    	bt5.setText("Unfollow");
                    }
                }
            });
            
        }
        add(pn1);
        setVisible(true);
    }
    

    /**
     * 팔로워 보기
     */
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new window1();
        }
    }

    /**
     * 팔로잉 보기
     */
    class MyActionListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new window2();
        }
    }

    /**
     * New post 열기 새창
     */
    class MyActionListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new NewPost(userId);
        }
    }
    

    /**
     * ID 검색하는 새 창
     */
    public class Search extends JFrame {

        Search(String cur_id, String board_id) {

            setBounds(1300, 300, 250, 200);
            setLayout(null);
            setTitle("Search");

            JPanel pn = new JPanel();
            pn.setSize(250, 200);
            pn.setBackground(Color.white);

            TextField searchID = new TextField();
            searchID.setSize(205, 30);
            searchID.setFont(font);

            //검색
            ImageIcon s1 = new ImageIcon("image/search.jpg");
            Image s2 = s1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon search = new ImageIcon(s2);
            JButton bt = new JButton(s1);
            bt.setBounds(204, 0, 30, 30);
            bt.setFocusPainted(false);
            add(bt);

            add(searchID); add(pn);
            searchID.setText("search user id!");
            setVisible(true);


            //누르면 ID 검색 결과 나오도록
            bt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	
                	String id = searchID.getText();
                	searchID.setText("");
                	InfoJDBC info = new InfoJDBC();
                	String name = info.getName(id);
                    
                    if(name.equals("")) {
                    	System.out.println("such user doesn't exist...");	
                    }
                    else {
                    	new PersonalBoard(cur_id, id);
                    }
                }
            });
        }
    }


    public void addMessage( String To, String time, String From, String message) {

        JPanel Message = new JPanel();
        Message.setPreferredSize(new Dimension(670, 200));
        Message.setBackground(Color.white);
        Message.setLayout(new BorderLayout());
        JPanel space = new JPanel();
        space.setBackground(Color.white);

        JLabel ToLb = new JLabel("To. "+To);
        JLabel FromLb = new JLabel("From. "+From);
        JLabel messageLb = new JLabel(message);
        JLabel timeLb = new JLabel(time);

        ToLb.setFont(font);
        FromLb.setFont(font);
        messageLb.setFont(font);
        timeLb.setFont(font);

        ToLb.setForeground(T_blue);
        FromLb.setForeground(T_blue);
        timeLb.setForeground(Color.GRAY);

        ToLb.setBounds(15, 15, 500, 20);
        timeLb.setBounds(15, 35, 500, 20);
        messageLb.setBounds(15, 55, 500, 100);
        FromLb.setBounds(15, 165, 500, 20);

        Message.add(ToLb);
        Message.add(FromLb);
        Message.add(messageLb);
        Message.add(timeLb);
        Message.add(space);

        if (messageNum == 2) {
            vertical += 100;
            messagePn.setPreferredSize(new Dimension(680, vertical));
        }

        else if(messageNum>2) {
            vertical += 210;
            messagePn.setPreferredSize(new Dimension(680, vertical));
        }

        messagePn.add(Message);
        messageNum++;
    }


    /**
     * follower 새창
     */
    class window1 extends JFrame{
    	String tmp;
        window1() {
        	
        	InfoJDBC info = new InfoJDBC();
        	
        	tmp = info.getFollower(boardId);
        	
            JPanel p1 = new JPanel();
            p1.setBounds(0, 0, 300, 300);
            p1.setBackground(Color.white);
            p1.setPreferredSize(new Dimension(300, 300));
            p1.setLayout(new FlowLayout());

            Dimension frameSize = new Dimension(300, 300);
            Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

            setLocation((windowSize.width - frameSize.width)/2,
                    (windowSize.height - frameSize.height)/2);

            JList list_er = new JList();
            list_er.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            DefaultListModel er=new DefaultListModel(); //JList는 직접추가할수있는 모델이 없어서 새로 만들어 준다.
            
            
            String[] followers = tmp.split("@");
            
            String id_to_name;
            for (int i = 0; i < followers.length; i++) {
            	id_to_name = info.getName(followers[i]);
            	er.addElement(id_to_name);
            }

            list_er.setModel(er);

            JScrollPane p =new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(p);

            p1.add(list_er);

            p1.setFont(f3);
            setTitle("All Followers");

            setSize(300,150);
            setVisible(true);

        }
    }


    /**
     * following 새창
     */
    class window2 extends JFrame {
    	String tmp;
        window2() {
        	InfoJDBC info = new InfoJDBC();
        	
        	tmp = info.getFollowing(boardId);
            JPanel p1 = new JPanel();
            p1.setBounds(0, 0, 300, 300);
            p1.setBackground(Color.white);
            p1.setPreferredSize(new Dimension(300, 300));
            p1.setLayout(new FlowLayout());

            Dimension frameSize = new Dimension(300, 300);
            Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

            setLocation((windowSize.width - frameSize.width)/2,
                    (windowSize.height - frameSize.height)/2);


            JList list_ing = new JList();
            list_ing.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            DefaultListModel ing=new DefaultListModel(); //JList는 직접추가할수있는 모델이 없어서 새로 만들어 준다.
            
            String[] followings = tmp.split("@");
            
            String id_to_name;
            for (int i = 0; i < followings.length; i++) {
            	id_to_name = info.getName(followings[i]);
            	ing.addElement(id_to_name);
            }


            list_ing.setModel(ing);
            JScrollPane p =new JScrollPane(p1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(p);

            p1.add(list_ing);
            p1.setFont(f3);

            setTitle("Following");



            setSize(300,150);
            setVisible(true);


        }
    }


    public static void main(String[] args) {
        new PersonalBoard("jihae", "jihae");
    }
}
