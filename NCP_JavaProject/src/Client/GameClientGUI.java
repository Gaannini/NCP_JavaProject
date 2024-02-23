package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import omokGame.OmokGame;
import omokGame.OmokServer;
import exgame.exgameclient;

import baseballGame.BaseballGame;
import exgame.exgameclient;
import omokGame.OmokGame;

public class GameClientGUI extends JFrame {
	// 서버
	String server = "127.0.0.1";// 서버IP
	int serverPort = 12345; // 임의 포트

	// 통신
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	// 클라이언트가 입력한 아이디값
	private String IDString;
	private String GameName;

	// 게임배경
	private MainPanel mainJPanel; // 게임배경

	private JPanel gameStartJPanel; // 게임 시작화면
	private JButton gameStartbtn; // 게임 시작 버튼
	private ImageIcon gameStarticon; // 게임 시작 버튼 아이콘

	private JPanel gameIdJPanel; // 아이디 입력 화면
	private JLabel IdinputMsg; // "닉네임을 입력하세요"
	private JTextField IdinputField; // 사용자가 닉네임을 입력하는 필드
	private JButton Idinputbtn;// 게임 시작 버튼

	private JPanel gameSelectJPanel; // 게임 선택화면
	private JLabel gameSelectMsg;// "게임을 선택하세요"
	private JButton selectLiarBtn; // 라이어게임 선택버튼
	private JButton selectBingoBtn; // 빙고게임 선택버튼
	private JButton selectOmokBtn; // 오목게임 선택버튼
	// 예시
	private JPanel exGameJPanel;
	private JLabel exGameJLabel;
	private JButton selectexBtn; // 오목게임 선택버튼
	private JButton selectBaseballBtn; // 숫자야구게임 버튼


	// 생성자
	public GameClientGUI() {
		init(); // 초기화 메서드 먼저 호출
		batch(); // 배치 메서드 호출
		setting(); // 세팅 메서드 호출
		listener(); // 리스너 메서드 호출
		setVisible(true);
	}

	// 생성

	private void init() {
		setSize(700, 700);
		// 게임배경
		mainJPanel = new MainPanel();
		mainJPanel.setBackground(new Color(0, 0, 0));

		// 게임시작화면
		gameStartJPanel = new JPanel();
		gameStartJPanel.setBackground(new Color(0, 195, 218));
		gameStarticon = new ImageIcon(getClass().getResource("/Client/images/gamestartbtn.png"));
		gameStartbtn = new JButton(gameStarticon);

		// 닉네임입력 화면
		gameIdJPanel = new JPanel();
		gameIdJPanel.setBackground(new Color(138, 195, 218));
		IdinputMsg = new JLabel("닉네임을 입력하세요.");
		IdinputField = new JTextField();
		Idinputbtn = new JButton("입장");

		// 게임선택화면(라이어, 빙고, 오목 중 한가지 선택하는 버튼구현)
		gameSelectJPanel = new JPanel();
		gameSelectJPanel.setBackground(new Color(138, 195, 218));
		gameSelectMsg = new JLabel("게임을 선택하세요");
		selectLiarBtn = new JButton("Liar Game");
		selectBingoBtn = new JButton("Bingo Game");
		selectOmokBtn = new JButton("omok Game");
		selectexBtn = new JButton("ex Game");
		selectBaseballBtn = new JButton("Baseball Game");

	}

	// 위치 등등 세팅
	private void setting() {
		setTitle("게임서버");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);
		setContentPane(mainJPanel);

		// 게임시작화면
		gameStartJPanel.setLayout(null);
		gameStartbtn.setBounds(150, 117, 360, 130);
		gameStarticon = ImageSetSize(gameStarticon, 360, 130);
		gameStartbtn.setVisible(true);
		gameStartJPanel.setVisible(true);// 시작때 화면 표시

		// 닉네임 입력 화면
		gameIdJPanel.setLayout(null);
		gameIdJPanel.setBounds(140, 87, 420, 190);
		IdinputMsg.setBounds(160, 52, 165, 50);
		IdinputField.setBounds(170, 101, 191, 50);
		Idinputbtn.setBounds(169, 163, 109, 50);
		gameIdJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정

		// 게임선택화면
		gameSelectJPanel.setLayout(null);
		gameSelectJPanel.setBounds(140, 87, 420, 190);
		gameSelectMsg.setBounds(147, 6, 100, 50);
		selectLiarBtn.setBounds(25, 54, 100, 118);
		selectBingoBtn.setBounds(126, 54, 141, 118);
		selectOmokBtn.setBounds(279, 54, 135, 118);
		selectexBtn.setBounds(138, 275, 109, 50);
		selectBaseballBtn.setBounds(138, 325, 109, 50);
		gameSelectJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정
	}

	// 화면에 배치
	private void batch() {
		mainJPanel.setLayout(null);
		mainJPanel.add(gameStartJPanel);
		mainJPanel.add(gameIdJPanel);
		mainJPanel.add(gameSelectJPanel);

		gameStartJPanel.add(gameStartbtn);
		gameStartbtn.setIcon(gameStarticon);

		gameIdJPanel.add(IdinputMsg);
		gameIdJPanel.add(IdinputField);
		gameIdJPanel.add(Idinputbtn);

		gameSelectJPanel.add(gameSelectMsg);
		gameSelectJPanel.add(selectLiarBtn);
		gameSelectJPanel.add(selectBingoBtn);
		gameSelectJPanel.add(selectOmokBtn);
		gameSelectJPanel.add(selectexBtn);
		gameSelectJPanel.add(selectBaseballBtn);
	}

	// 버튼 클릭 이벤트
	private void listener() {
		// 게임시작버튼 눌렀을때
		gameStartbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameIdJPanel.setVisible(true); // gameIdJPanel 활성화
				gameStartJPanel.setVisible(false); // gameStartJPanel 비활성화
			}
		});

		// 닉네임 작성 후 입장버튼눌렀을때 -> Id가 서버에 전달
		Idinputbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JButton gameStartBtn = (JButton) e.getSource();
				connectServer(); // 서버에 연결
				sendInsertId(); // 입력받은 아이디 서버에 전달
			}
		});

		// 라이어게임 선택!
		selectBingoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "bingo";
				sendSelectgame(GameName);
			}
		});

		// 라이어게임 선택!
		selectOmokBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "omok";
				sendSelectgame(GameName);
				OmokServer omokServer = new OmokServer();
				omokServer.startgame();
			}
		});

		// 오목게임 선택!
		selectOmokBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "omok";
				sendSelectgame(GameName);
			}
		});

		// ex 선택!
		selectexBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "ex";
				sendSelectgame(GameName);
				exgameclient exgame = new exgameclient(socket);
			}
		});

		// 숫자 야구 게임 선택!
		selectBaseballBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "baseball";
				sendSelectgame(GameName);
				BaseballGame baseball = new BaseballGame(socket);
			}
		});

	}

	// 접속 시 서버 연결 메서드.
	private void connectServer() {
		try {
			socket = new Socket(server, serverPort);
		} catch (Exception e) {
			System.out.println(server + ": 서버 연결 실패");
		}
	}

	// ID를 서버에 전달하는 메소드
	private void sendInsertId() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			IDString = IdinputField.getText();
			if ((IDString.equals(""))) { // NULL값 입력시
				IDString = Integer.toString(socket.hashCode());
				writer.println("ID&" + IDString);
				gameSelectJPanel.setVisible(true); // gameSelectJPanel 활성화
				gameIdJPanel.setVisible(false); // startJPanel 비활성화

			} else { // 아이디 값 입력시.
				writer.println("ID&" + IDString);
				IdinputField.setText("");
				gameSelectJPanel.setVisible(true); // gameSelectJPanel 활성화
				gameIdJPanel.setVisible(false); // startJPanel 비활성화

			}

		} catch (IOException e) {
			System.out.println(server + ": 준비 메세지 요청 실패");
		}
	}

	// 게임을 선택하고 선택한 게임을 서버에 보내는 메소드
	private void sendSelectgame(String gamename) {
		writer.println("gamename&" + gamename);
	}

	// 이미지 아이콘 크기 조절 메소드
	private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
		Image xImage = icon.getImage();
		Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		ImageIcon xyImage = new ImageIcon(yImage);
		return xyImage;
	}

	// 이미지 삽입 패널 클래스(게임배경)
	class MainPanel extends JPanel {
		private ImageIcon icon = new ImageIcon(getClass().getResource("/Client/images/mainbg.png"));
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	}

	public static void main(String[] args) {
		new GameClientGUI();
	}

}