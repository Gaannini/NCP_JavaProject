package baseballGame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BaseballGame extends JFrame {
	// 통신
	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	// 폰트 크기 설정
	private Font smallFont; // 16px
	private Font mediumFont; // 24px
	private Font largeFont; // 36px

	// 숫자 야구 게임 전체 화면
	private JPanel mainPanel;

	// 게임 이름
	private NamePanel namePanel;

	// 유저가 입력한 숫자 패널
	private JPanel numPanel;

	// 유저가 입력했던 숫자 모음 영역 
	private JPanel userPanel;

	// 유저가 입력했던 오답 모음 (9개)
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	private JPanel panel5;
	private JPanel panel6;
	private JPanel panel7;
	private JPanel panel8;
	private JPanel panel9;

	// 키보드 패널
	private JPanel keyboardPanel;

	// 키보드 - 버튼(0 ~ 9)
	private JButton button0;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button9;

	// 키보드 버튼 - 이미지 아이콘
	private ImageIcon icon0;
	private ImageIcon icon1;
	private ImageIcon icon2;
	private ImageIcon icon3;
	private ImageIcon icon4;
	private ImageIcon icon5;
	private ImageIcon icon6;
	private ImageIcon icon7;
	private ImageIcon icon8;
	private ImageIcon icon9;

	// 이미지 아이콘 크기 조절 메소드
	private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
		Image xImage = icon.getImage();
		Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		ImageIcon xyImage = new ImageIcon(yImage);
		return xyImage;
	}
	
	// 이미지 삽입 패널 클래스
	class NamePanel extends JPanel {
		private ImageIcon icon = new ImageIcon(getClass().getResource("/baseballGame/imgs/title.jpeg"));
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	}

	// BaseballGame 생성자
	public BaseballGame(Socket socket) {
		this.socket = socket;
		init();
		setting();
		batch();
		listener();
		setVisible(true);
	}

	private void init() {
		// 이미지 삽입 패널
		namePanel = new NamePanel();
		namePanel.setBackground(new Color(192, 192, 192));
		namePanel.setForeground(new Color(169, 169, 169));
		
		// 패널
		mainPanel = new JPanel();
		mainPanel.setBorder(null);
		mainPanel.setBackground(new Color(255, 255, 255));
		numPanel = new JPanel();
		numPanel.setBorder(null);
		userPanel = new JPanel();
		userPanel.setBackground(new Color(255, 255, 255));
		userPanel.setLocation(26, 205);
		userPanel.setSize(561, 472);
		panel1 = new JPanel();
		panel1.setBackground(new Color(240, 255, 240));
		panel1.setBounds(0, 0, 170, 148);
		panel2 = new JPanel();
		panel2.setBackground(new Color(240, 255, 240));
		panel2.setBounds(200, 0, 170, 148);
		panel3 = new JPanel();
		panel3.setBackground(new Color(240, 255, 240));
		panel3.setBounds(400, 0, 170, 148);
		panel4 = new JPanel();
		panel4.setBackground(new Color(240, 255, 240));
		panel4.setBounds(0, 162, 170, 148);
		panel5 = new JPanel();
		panel5.setBackground(new Color(240, 255, 240));
		panel5.setBounds(200, 162, 170, 148);
		panel6 = new JPanel();
		panel6.setBackground(new Color(240, 255, 240));
		panel6.setBounds(400, 162, 170, 148);
		panel7 = new JPanel();
		panel7.setBackground(new Color(240, 255, 240));
		panel7.setBounds(0, 325, 170, 148);
		panel8 = new JPanel();
		panel8.setBackground(new Color(240, 255, 240));
		panel8.setBounds(200, 325, 170, 148);
		panel9 = new JPanel();
		panel9.setBackground(new Color(240, 255, 240));
		panel9.setBounds(400, 325, 170, 148);
		
		keyboardPanel = new JPanel();
		keyboardPanel.setBackground(new Color(255, 255, 255));

		// 이미지
		icon0 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon0.jpeg"));
		icon1 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon1.jpeg"));
		icon2 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon2.jpeg"));
		icon3 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon3.jpeg"));
		icon4 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon4.jpeg"));
		icon5 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon5.jpeg"));
		icon6 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon6.jpeg"));
		icon7 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon7.jpeg"));
		icon8 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon8.jpeg"));
		icon9 = new ImageIcon(getClass().getResource("/baseballGame/imgs/icon9.jpeg"));

		// 버튼
		button0 = new JButton(icon0);
		button1 = new JButton(icon1);
		button2 = new JButton(icon2);
		button3 = new JButton(icon3);
		button4 = new JButton(icon4);
		button5 = new JButton(icon5);
		button6 = new JButton(icon6);
		button7 = new JButton(icon7);
		button8 = new JButton(icon8);
		button9 = new JButton(icon9);

		// 라벨
		

		// 폰트
		smallFont = new Font("맑은고딕", Font.PLAIN, 16);
		mediumFont = new Font("맑은고딕", Font.PLAIN, 24);
		largeFont = new Font("맑은고딕", Font.PLAIN, 36);
	}

	private void setting() {
		setTitle("숫자 야구 게임");
		setSize(620, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 버튼
		setResizable(false); // 유저가 크기 조절 못하게 함

		setContentPane(mainPanel);

		namePanel.setVisible(true);
		namePanel.setBounds(170, 37, 280, 50);

		numPanel.setVisible(true);
		numPanel.setBounds(26, 112, 570, 70);
		numPanel.setBackground(new Color(240, 255, 255));

		userPanel.setVisible(true);
		userPanel.setBounds(26, 200, 570, 472);
		
		panel1.setVisible(true);
		panel2.setVisible(true);
		panel3.setVisible(true);
		panel4.setVisible(true);
		panel5.setVisible(true);
		panel6.setVisible(true);
		panel7.setVisible(true);
		panel8.setVisible(true);
		panel9.setVisible(true);

		keyboardPanel.setVisible(true);
		keyboardPanel.setBounds(15, 700, 590, 230);

		int buttonWidth = keyboardPanel.getWidth() / 10;
		int buttonHeigth = keyboardPanel.getHeight() / 2;
		button1.setBounds(0, 0, buttonWidth, buttonHeigth);
		button2.setBounds(0, 0, buttonWidth, buttonHeigth);
		button3.setBounds(0, 0, buttonWidth, buttonHeigth);
		button4.setBounds(0, 0, buttonWidth, buttonHeigth);
		button5.setBounds(0, 0, buttonWidth, buttonHeigth);
		button6.setBounds(0, 0, buttonWidth, buttonHeigth);
		button7.setBounds(0, 0, buttonWidth, buttonHeigth);
		button8.setBounds(0, 0, buttonWidth, buttonHeigth);
		button9.setBounds(0, 0, buttonWidth, buttonHeigth);
		button0.setBounds(0, 0, buttonWidth, buttonHeigth);

		icon0 = ImageSetSize(icon0, buttonWidth, buttonHeigth);
		icon1 = ImageSetSize(icon1, buttonWidth, buttonHeigth);
		icon2 = ImageSetSize(icon2, buttonWidth, buttonHeigth);
		icon3 = ImageSetSize(icon3, buttonWidth, buttonHeigth);
		icon4 = ImageSetSize(icon4, buttonWidth, buttonHeigth);
		icon5 = ImageSetSize(icon5, buttonWidth, buttonHeigth);
		icon6 = ImageSetSize(icon6, buttonWidth, buttonHeigth);
		icon7 = ImageSetSize(icon7, buttonWidth, buttonHeigth);
		icon8 = ImageSetSize(icon8, buttonWidth, buttonHeigth);
		icon9 = ImageSetSize(icon9, buttonWidth, buttonHeigth);

	}

	private void batch() {
		mainPanel.setLayout(null);
		mainPanel.add(numPanel);
		mainPanel.add(namePanel);
		
		mainPanel.add(userPanel);
		mainPanel.add(keyboardPanel);
		
		userPanel.setLayout(null); // 지우지마!! 

		userPanel.add(panel1);
		userPanel.add(panel2);
		userPanel.add(panel3);
		userPanel.add(panel4);
		userPanel.add(panel5);
		userPanel.add(panel6);
		userPanel.add(panel7);
		userPanel.add(panel8);
		userPanel.add(panel9);

		keyboardPanel.add(button1);
		keyboardPanel.add(button2);
		keyboardPanel.add(button3);
		keyboardPanel.add(button4);
		keyboardPanel.add(button5);
		keyboardPanel.add(button6);
		keyboardPanel.add(button7);
		keyboardPanel.add(button8);
		keyboardPanel.add(button9);
		keyboardPanel.add(button0);
	}

	private void listener() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ex)
				printWriter.println("baseball&" + "클라이언트");
			}
		});
		button2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button5.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button6.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button7.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button8.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button9.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});
		button0.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});

	}
}