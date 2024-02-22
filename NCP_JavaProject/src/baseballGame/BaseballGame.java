package baseballGame;

// AbsoluteLayout 사용할건디 !

import javax.management.monitor.MonitorSettingException;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.Game;

public class BaseballGame extends JFrame implements Game {
	private Socket socket;

	@Override
	public void start(Socket socket) {
		this.socket = socket;
		new bal(socket).start();
	}

	public class bal extends Thread {
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수
		public PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.
		public String exMsg; // 클라이언트 아이디를 담는 변수.
		public String gamename; // 클라이언트가 선택한 게임 이름

		public bal(Socket socket) {
			this.socket = socket;
		}
	}
	
	// 폰트 크기 설정
	private Font smallFont; // 16px
	private Font mediumFont; // 24px
	private Font largeFont; // 36px

	// 숫자 야구 게임 전체 화면
	private JPanel mainPanel;

	// 게임 이름
	private JLabel nameLabel;

	// 유저가 입력한 숫자 패널
	private JPanel numPanel;

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
	private KeyboardPanel keyboardPanel;

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

	// TODO : 버튼 이미지 삽입 
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
	
	// TODO : 경로 삽입 
	// 이미지 삽입 패널 클래스
	class KeyboardPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	}

	// BaseballGame 생성자 
	public BaseballGame() {
		init();
		setting();
		batch();
		listener();
		setVisible(true);
	}

	private void init() {
		// 이미지 패널
		keyboardPanel = new KeyboardPanel();
		
		// 패널
		mainPanel = new JPanel();
		numPanel = new JPanel();
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		panel6 = new JPanel();
		panel7 = new JPanel();
		panel8 = new JPanel();
		panel9 = new JPanel();
		
		// TODO : 경로 삽입 
		// 이미지
		icon0 = new ImageIcon("");
		icon1 = new ImageIcon("");
		icon2 = new ImageIcon("");
		icon3 = new ImageIcon("");
		icon4 = new ImageIcon("");
		icon5 = new ImageIcon("");
		icon6 = new ImageIcon("");
		icon7 = new ImageIcon("");
		icon8 = new ImageIcon("");
		icon9 = new ImageIcon("");
		
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
		nameLabel = new JLabel("숫자 야구 게임");
		
		// 폰트 
		smallFont = new Font("맑은고딕", Font.PLAIN, 16);
		mediumFont = new Font("맑은고딕", Font.PLAIN, 24);
		largeFont = new Font("굴림", Font.PLAIN, 36);
	}

	private void setting() {
		setTitle("숫자 야구 게임");
		setSize(500, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false); // 유저가 크기 조절 못하게 함 
		
		setContentPane(mainPanel);
		mainPanel.setLayout(null); // 레이아웃 매니저 막아둠
		
		nameLabel.setBounds(0, 2, 500, 36); 
		nameLabel.setFont(largeFont);
		
		numPanel.setVisible(true);
		numPanel.setBounds(0, 36, 500, 100);
		numPanel.setBackground(Color.LIGHT_GRAY);
	}

	private void batch() {
		mainPanel.add(numPanel);
		mainPanel.add(nameLabel);
	}

	private void listener() {

	}

	public static void main(String[] args) {
		new BaseballGame();
	}
}
