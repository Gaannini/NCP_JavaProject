package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import omokGame.OmokGame;
import omokGame.OmokServer;
import exgame.exgameclient;


public class GameClientGUI extends JFrame {
	// 서버
	String server = "192.168.0.34";// 서버IP
	int serverPort = 12345; // 임의 포트

	// 통신
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	// 클라이언트가 입력한 아이디값
	private String IDString;
	private String GameName;

	private JPanel startJPanel; // 게임 첫 화면
	private JLabel WelcomeMsg; // 제목
	private JLabel IdinputMsg; // "닉네임을 입력하세요"
	private JTextField IdinputField; // 사용자가 닉네임을 입력하는 필드
	private JButton gameStartBtn;// 게임 시작 버튼

	private JPanel gameSelectJPanel; // 게임 선택화면
	private JLabel gameSelectMsg;// "게임을 선택하세요"
	private JButton selectLiarBtn; // 라이어게임 선택버튼
	private JButton selectBingoBtn; // 빙고게임 선택버튼
	private JButton selectOmokBtn; // 오목게임 선택버튼
	private JButton selectexBtn; // ex게임 선택버튼

	// 예시
	private JPanel exGameJPanel;
	private JLabel exGameJLabel;

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
		setSize(500, 500);
		// 게임 첫 화면(닉네임입력과 게임시작버튼)
		startJPanel = new JPanel();
		startJPanel.setBounds(0, 0, 500, 500);
		WelcomeMsg = new JLabel("게임");
		IdinputMsg = new JLabel("닉네임을 입력하세요.");
		IdinputField = new JTextField();
		gameStartBtn = new JButton("게임시작");

		// 게임선택화면(라이어, 빙고, 오목 중 한가지 선택하는 버튼구현)
		gameSelectJPanel = new JPanel();
		gameSelectJPanel.setBounds(0, 0, 500, 500);
		gameSelectMsg = new JLabel("게임을 선택하세요");
		selectLiarBtn = new JButton("Liar Game");
		selectBingoBtn = new JButton("Bingo Game");
		selectOmokBtn = new JButton("omok Game");
		selectexBtn = new JButton("ex Game");

//		exGameJPanel = new JPanel();
//		exGameJPanel.setBounds(0, 0, 500, 500);
//		exGameJLabel = new JLabel("예시게임");
	}

	// 위치 등등 세팅
	private void setting() {
		setTitle("게임서버");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		// 게임시작화면
		getContentPane().add(startJPanel);

		startJPanel.setLayout(null);
		startJPanel.setVisible(true); // 시작때 화면 표시

		WelcomeMsg.setBounds(204, 6, 100, 50);
		IdinputMsg.setBounds(177, 52, 165, 50);
		IdinputField.setBounds(129, 101, 191, 50);
		gameStartBtn.setBounds(169, 163, 109, 50);

		// 게임선택화면
		getContentPane().add(gameSelectJPanel);
		gameSelectJPanel.setLayout(null);
		gameSelectJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정

		gameSelectMsg.setBounds(204, 6, 100, 50);
		selectLiarBtn.setBounds(129, 53, 165, 50);
		selectBingoBtn.setBounds(113, 122, 191, 50);
		selectOmokBtn.setBounds(138, 225, 109, 50);
		selectexBtn.setBounds(138, 275, 109, 50);

		// 예시게임
//		getContentPane().add(exGameJPanel);
//		exGameJPanel.setLayout(null);
//		exGameJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정
//
//		exGameJLabel.setBounds(200, 200, 100, 100);

	}

	// 화면에 배치
	private void batch() {
		startJPanel.add(WelcomeMsg);
		startJPanel.add(IdinputMsg);
		startJPanel.add(IdinputField);
		startJPanel.add(gameStartBtn);

		gameSelectJPanel.add(gameSelectMsg);
		gameSelectJPanel.add(selectLiarBtn);
		gameSelectJPanel.add(selectBingoBtn);
		gameSelectJPanel.add(selectOmokBtn);
		gameSelectJPanel.add(selectexBtn);

		// exGameJPanel.add(exGameJLabel);
	}

	// 버튼 클릭 이벤트
	private void listener() {
		// 닉네임 작성 후 게임시작버튼눌렀을때 -> Id가 서버에 전달
		gameStartBtn.addActionListener(new ActionListener() {
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
				startJPanel.setVisible(false); // startJPanel 비활성화

			} else { // 아이디 값 입력시.
				writer.println("ID&" + IDString);
				IdinputField.setText("");
				gameSelectJPanel.setVisible(true); // gameSelectJPanel 활성화
				startJPanel.setVisible(false); // startJPanel 비활성화

			}

		} catch (IOException e) {
			System.out.println(server + ": 준비 메세지 요청 실패");
		}
	}

	// 게임을 선택하고 선택한 게임을 서버에 보내는 메소드
	private void sendSelectgame(String gamename) {
		writer.println("gamename&" + gamename);
	}

	public static void main(String[] args) {
		new GameClientGUI();
	}

}