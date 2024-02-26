package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import baseballGame.BaseballGame;
import memoryGame.MemoryGame;
import omokGame.OmokClient;

public class GameClientGUI extends JFrame {
	// 폰트 파일 경로
	String fontFilePath = "Font/BagelFatOne-Regular.ttf";
	Font nameFont;

	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

	// 서버
	String server = "127.0.0.1";// 서버IP
	int serverPort = 12345; // 임의 포트

	// 통신
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private clientInfo CI;

	// 클라이언트가 입력한 아이디값
	private String IDString;
	private String GameName;

	// 게임배경
	private MainPanel mainJPanel; // 게임배경

	// 채팅창
	private Font chatmsgfont;
	private JPanel chatJPanel; // 채팅패널
	private JLabel chatmsg; // "Chat Room"
	private Font chatfieldfont;
	private JTextField chatJTextField; // 채팅입력필드
	private String sendchat; // 입력한 값을 서버에 보낼 문자열 저장
	private String acceptchat; // 다른사람들이 보낸 채팅
	private JButton chatJButton; // 전송버튼
	int num = 0;// chat 갱신하기 위한
	private ArrayList<JLabel> chatLabels;
	private JLabel chat1;
	private JLabel chat2;
	private JLabel chat3;
	private JLabel chat4;
	private JLabel chat5;
	private JLabel chat6;
	private JLabel chat7;
	private JLabel chat8;
	private JLabel chat9;
	private JLabel chat10;
	private JLabel chat11;
	private JLabel chat12;
	private JLabel chat13;
	private JLabel chat14;
	private JLabel chat15;
	private JLabel chat16;
	private JLabel chat17;
	private JLabel chat18;
	private JLabel chat19;
	// 게임시작
	private JPanel gameStartJPanel; // 게임 시작화면
	private JButton gameStartbtn; // 게임 시작 버튼
	private ImageIcon gameStarticon; // 게임 시작 버튼 아이콘

	// 아이디입력
	private JPanel gameIdJPanel; // 아이디 입력 화면
	private ImageIcon Idinputmsg;
	private JLabel IdinputJLabel;// "닉네임을 입력하세요"
	private ImageIcon namefield_;
	private JLabel namefield_Label; // 닉네임 입력하는 곳 밑줄
	private JTextField IdinputField; // 사용자가 닉네임을 입력하는 필드
	private ImageIcon Idinputicon;
	private JButton Idinputbtn;// 게임 시작 버튼

	// 게임선택
	private JPanel gameSelectJPanel; // 게임 선택화면
	private ImageIcon gameselecticon;
	private JLabel gameSelectMsg;// "게임을 선택하세요"
	// 게임선택버튼들
	private ImageIcon memoryIcon;
	private JButton selectMemoryBtn; // 메모리게임 선택버튼
	private ImageIcon memorytxtIcon;
	private JLabel memorytxt;// 메모리 text
	private ImageIcon omokIcon;
	private JButton selectOmokBtn; // 오목게임 선택버튼
	private ImageIcon omoktxtIcon;
	private JLabel omoktxt;// 오목 text
	private ImageIcon baseballIcon;
	private JButton selectBaseballBtn; // 숫자야구게임 버튼
	private ImageIcon baseballtxtIcon;
	private JLabel baseballtxt;// 야구 text

	MemoryGame memorygame;
	OmokClient omokClient;
	BaseballGame baseball;

	// 생성자
	public GameClientGUI() {
		try {
			nameFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath));
			ge.registerFont(nameFont);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		init(); // 초기화 메서드 먼저 호출
		batch(); // 배치 메서드 호출
		setting(); // 세팅 메서드 호출
		listener(); // 리스너 메서드 호출
		setVisible(true);

		// 창 닫을 때 이벤트 처리 추가
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				sendExitMsg();
				disconnectServer(); // 서버와의 연결 끊기
			}
		});
	}

	// 서버에서 메세지 받기위한 스레드
	public class clientInfo extends Thread {
		private Socket socket;
		private BufferedReader reader;
		public String clientId;
		public String gamename;

		public clientInfo(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				while ((acceptchat = reader.readLine()) != null) {
					String[] parsedMsg = acceptchat.split("@");
					// chatreset(data);
					// Client Thread에서 동작하는 프로토콜
					handleProtocol(parsedMsg);
					// 받은 클래스 이름을 실행하고 결과를 클라이언트에게 다시 전송

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void handleProtocol(String[] parsedMsg) throws IOException {
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "chat":
					chatreset(data);
					break;
				case "baseball":
					// chatreset(data);
					sendMsgToBaseballClient(data);
					break;

				}
			}
		}
	}

	// 메시지를 BaseballGame으로 전달하는 메소드
	private void sendMsgToBaseballClient(String message) {
		// baseball.sendMessageToClient(message);
		baseball.parseHandleProtocol(message);
	}

	private void chatreset(String chatmsg) {
		// 기존 메시지를 한 칸씩 뒤로 옮기기
		for (int i = num; i > 0; i--) {
			JLabel prevLabel = chatLabels.get(i - 1);
			JLabel currentLabel = chatLabels.get(i);
			currentLabel.setText(prevLabel.getText()); // 이전 라벨의 텍스트를 현재 라벨로 이동
		}

		// 새로운 메시지를 첫 번째 채팅 라벨에 설정
		chatLabels.get(0).setText(chatmsg);

		// num 값 갱신
		num = Math.min(num + 1, 19); // num 값을 최대 19까지 유지

		// 만약 num이 19를 초과하면 마지막 채팅 라벨의 메시지를 지우고 num을 18로 설정
		if (num == 19) {
			chatLabels.get(18).setText(""); // 19번째 라벨의 텍스트를 지움
			num = 18; // num 값을 18로 설정
		}
	}

	// 생성
	private void init() {
		setSize(700, 700);
		// 게임배경
		mainJPanel = new MainPanel();
		mainJPanel.setLayout(null);
		mainJPanel.setBackground(new Color(0, 0, 0));
		mainJPanel.setBounds(0, 0, 700, 700);

		// 채팅
		chatmsgfont = new Font("맑은 고딕", Font.PLAIN, 20);
		chatfieldfont = new Font("맑은 고딕", Font.PLAIN, 14);
		chatJPanel = new JPanel();
		chatJPanel.setLayout(null);
		chatJPanel.setBackground(new Color(0, 0, 0));
		chatJPanel.setBounds(700, 0, 500, 700);
		chatmsg = new JLabel("Chat Room");
		chatJTextField = new JTextField();
		chatJButton = new JButton("전송");
		chat1 = new JLabel();
		chat2 = new JLabel();
		chat3 = new JLabel();
		chat4 = new JLabel();
		chat5 = new JLabel();
		chat6 = new JLabel();
		chat7 = new JLabel();
		chat8 = new JLabel();
		chat9 = new JLabel();
		chat10 = new JLabel();
		chat11 = new JLabel();
		chat12 = new JLabel();
		chat13 = new JLabel();
		chat14 = new JLabel();
		chat15 = new JLabel();
		chat16 = new JLabel();
		chat17 = new JLabel();
		chat18 = new JLabel();
		chat19 = new JLabel();
		chatLabels = new ArrayList<JLabel>();
		chatLabels.add(chat1);
		chatLabels.add(chat2);
		chatLabels.add(chat3);
		chatLabels.add(chat4);
		chatLabels.add(chat5);
		chatLabels.add(chat6);
		chatLabels.add(chat7);
		chatLabels.add(chat8);
		chatLabels.add(chat9);
		chatLabels.add(chat10);
		chatLabels.add(chat11);
		chatLabels.add(chat12);
		chatLabels.add(chat13);
		chatLabels.add(chat14);
		chatLabels.add(chat15);
		chatLabels.add(chat16);
		chatLabels.add(chat17);
		chatLabels.add(chat18);
		chatLabels.add(chat19);

		// 게임시작화면
		gameStartJPanel = new JPanel();
		gameStartJPanel.setLayout(null);
		gameStartJPanel.setBackground(new Color(228, 227, 255));
		gameStartJPanel.setBounds(165, 140, 380, 240);
		gameStarticon = new ImageIcon(getClass().getResource("/Client/images/gamestartbtn0.png"));
		gameStartbtn = new JButton(gameStarticon);

		// 닉네임입력 화면
		gameIdJPanel = new JPanel();
		gameIdJPanel.setLayout(null);
		gameIdJPanel.setBackground(new Color(228, 227, 255));
		gameIdJPanel.setBounds(165, 140, 380, 240);
		Idinputmsg = new ImageIcon(getClass().getResource("/Client/images/nameinputimg.png"));
		IdinputJLabel = new JLabel(Idinputmsg);
		namefield_ = new ImageIcon(getClass().getResource("/Client/images/__.png"));
		namefield_Label = new JLabel(namefield_);
		IdinputField = new JTextField(30);
		Idinputicon = new ImageIcon(getClass().getResource("/Client/images/nameinputbtn.png"));
		Idinputbtn = new JButton(Idinputicon);

		// 게임선택화면(라이어, 빙고, 오목 중 한가지 선택하는 버튼구현)
		gameSelectJPanel = new JPanel();
		gameSelectJPanel.setLayout(null);
		gameSelectJPanel.setBounds(165, 140, 380, 240);
		gameSelectJPanel.setBackground(new Color(228, 227, 255));
		gameselecticon = new ImageIcon(getClass().getResource("/Client/images/gameselectmsg.png"));
		gameSelectMsg = new JLabel(gameselecticon);

		// 메모리게임버튼
		memoryIcon = new ImageIcon(getClass().getResource("/Client/images/memorygame.png"));
		selectMemoryBtn = new JButton(memoryIcon);
		memorytxtIcon = new ImageIcon(getClass().getResource("/Client/images/memorytxt.png"));
		memorytxt = new JLabel(memorytxtIcon);

		// 오목게임버튼
		omokIcon = new ImageIcon(getClass().getResource("/Client/images/omokgame.png"));
		selectOmokBtn = new JButton(omokIcon);
		omoktxtIcon = new ImageIcon(getClass().getResource("/Client/images/omoktxt.png"));
		omoktxt = new JLabel(omoktxtIcon);

		// 야구게임버튼
		baseballIcon = new ImageIcon(getClass().getResource("/Client/images/baseballgame.png"));
		selectBaseballBtn = new JButton(baseballIcon);
		baseballtxtIcon = new ImageIcon(getClass().getResource("/Client/images/baseballtxt.png"));
		baseballtxt = new JLabel(baseballtxtIcon);

	}

	// 위치 등등 세팅
	private void setting() {
		setTitle("게임서버");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(mainJPanel);
		add(chatJPanel);
		// setContentPane(mainJPanel);

		// 채팅화면
		chatmsg.setFont(chatmsgfont);
		chatmsg.setForeground(Color.white);
		chatJTextField.setBounds(695, 633, 450, 40);
		chatJTextField.setFont(chatfieldfont);
		// chatJTextField.setVisible(true);
		chatJButton.setBounds(1146, 633, 50, 40);
		// chatJButton.setVisible(true);
		chatmsg.setBounds(700, 5, 450, 50);
		chat1.setBounds(700, 600, 450, 30);
		chat2.setBounds(700, 570, 450, 30);
		chat3.setBounds(700, 540, 450, 30);
		chat4.setBounds(700, 510, 450, 30);
		chat5.setBounds(700, 480, 450, 30);
		chat6.setBounds(700, 450, 450, 30);
		chat7.setBounds(700, 420, 450, 30);
		chat8.setBounds(700, 390, 450, 30);
		chat9.setBounds(700, 360, 450, 30);
		chat10.setBounds(700, 330, 450, 30);
		chat11.setBounds(700, 300, 450, 30);
		chat12.setBounds(700, 270, 450, 30);
		chat13.setBounds(700, 240, 450, 30);
		chat14.setBounds(700, 210, 450, 30);
		chat15.setBounds(700, 180, 450, 30);
		chat16.setBounds(700, 150, 450, 30);
		chat17.setBounds(700, 120, 450, 30);
		chat18.setBounds(700, 90, 450, 30);
		chat19.setBounds(700, 60, 450, 30);
		chat1.setForeground(Color.white);
		chat2.setForeground(Color.white);
		chat3.setForeground(Color.white);
		chat4.setForeground(Color.white);
		chat5.setForeground(Color.white);
		chat6.setForeground(Color.white);
		chat7.setForeground(Color.white);
		chat8.setForeground(Color.white);
		chat9.setForeground(Color.white);
		chat10.setForeground(Color.white);
		chat11.setForeground(Color.white);
		chat12.setForeground(Color.white);
		chat13.setForeground(Color.white);
		chat14.setForeground(Color.white);
		chat15.setForeground(Color.white);
		chat16.setForeground(Color.white);
		chat17.setForeground(Color.white);
		chat18.setForeground(Color.white);
		chat19.setForeground(Color.white);

		// 게임시작화면
		gameStartJPanel.setVisible(true);// 시작때 화면 표시
		gameStartbtn.setBounds(50, 55, 270, 110);
		gameStarticon = ImageSetSize(gameStarticon, 360, 130);
		gameStartbtn.setVisible(true);

		// 닉네임 입력 화면
		IdinputJLabel.setBounds(15, 35, 350, 50);
		namefield_Label.setBounds(40, 140, 240, 80);
		IdinputField.setBounds(50, 105, 250, 80);
		IdinputField.setOpaque(false);// 입력창 투명하게
		IdinputField.setBackground(new Color(0, 0, 0, 0));
		IdinputField.setBorder(null);
		IdinputField.setFont(nameFont.deriveFont(Font.PLAIN, 40)); // 폰트설정
		Idinputbtn.setBounds(290, 105, 80, 80);
		Idinputbtn.setOpaque(false);// 버튼 투명하게
		Idinputbtn.setContentAreaFilled(false);
		Idinputbtn.setBorderPainted(false);
		gameIdJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정

		// 게임선택화면
		gameSelectMsg.setBounds(43, 15, 300, 50);

		selectMemoryBtn.setBounds(13, 80, 110, 110);
		selectMemoryBtn.setOpaque(false);// 버튼 투명하게
		selectMemoryBtn.setContentAreaFilled(false);
		selectMemoryBtn.setBorderPainted(false);
		memorytxt.setBounds(16, 190, 110, 30);

		selectOmokBtn.setBounds(140, 79, 110, 110);
		selectOmokBtn.setOpaque(false);// 버튼 투명하게
		selectOmokBtn.setContentAreaFilled(false);
		selectOmokBtn.setBorderPainted(false);
		omoktxt.setBounds(140, 190, 110, 30);

		selectBaseballBtn.setBounds(262, 80, 110, 110);
		selectBaseballBtn.setOpaque(false);// 버튼 투명하게
		selectBaseballBtn.setContentAreaFilled(false);
		selectBaseballBtn.setBorderPainted(false);
		baseballtxt.setBounds(262, 190, 110, 30);

		gameSelectJPanel.setVisible(false); // 시작 시에는 화면에 표시되지 않도록 설정
	}

	// 화면에 배치
	private void batch() {
		mainJPanel.setLayout(null);
		mainJPanel.add(gameStartJPanel);
		mainJPanel.add(gameIdJPanel);
		mainJPanel.add(gameSelectJPanel);

		chatJPanel.add(chatmsg);
		chatJPanel.add(chatJTextField);
		chatJPanel.add(chatJButton);
		chatJPanel.add(chat1);
		chatJPanel.add(chat2);
		chatJPanel.add(chat3);
		chatJPanel.add(chat4);
		chatJPanel.add(chat5);
		chatJPanel.add(chat6);
		chatJPanel.add(chat7);
		chatJPanel.add(chat8);
		chatJPanel.add(chat9);
		chatJPanel.add(chat10);
		chatJPanel.add(chat11);
		chatJPanel.add(chat12);
		chatJPanel.add(chat13);
		chatJPanel.add(chat14);
		chatJPanel.add(chat15);
		chatJPanel.add(chat16);
		chatJPanel.add(chat17);
		chatJPanel.add(chat18);
		chatJPanel.add(chat19);

		gameStartJPanel.add(gameStartbtn);
		gameStartbtn.setIcon(gameStarticon);

		gameIdJPanel.add(IdinputJLabel);
		gameIdJPanel.add(namefield_Label);
		gameIdJPanel.add(IdinputField);
		gameIdJPanel.add(Idinputbtn);

		gameSelectJPanel.add(gameSelectMsg);
		gameSelectJPanel.add(selectMemoryBtn);
		gameSelectJPanel.add(selectOmokBtn);
		gameSelectJPanel.add(selectBaseballBtn);
		gameSelectJPanel.add(memorytxt);
		gameSelectJPanel.add(omoktxt);
		gameSelectJPanel.add(baseballtxt);
	}

	// 버튼 클릭 이벤트
	private void listener() {
		// 게임시작버튼 눌렀을때
		gameStartbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectServer(); // 서버에 연결
				gameIdJPanel.setVisible(true); // gameIdJPanel 활성화
				gameStartJPanel.setVisible(false); // gameStartJPanel 비활성화
			}
		});

		// 닉네임 작성 후 입장버튼눌렀을때 -> Id가 서버에 전달
		Idinputbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JButton gameStartBtn = (JButton) e.getSource();
				setSize(1200, 700);
				sendInsertId(); // 입력받은 아이디 서버에 전달
			}
		});

		// 채팅작성 -> 서버에 전달 -> 모든 클라이언트로 다시전달
		chatJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendchat();
			}
		});

		// 채팅입력필드에 ActionListener 추가
		chatJTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendchat(); // 메시지 전송 메서드 호출
			}
		});

		// 메모리게임 선택!
		selectMemoryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "memory";
				sendSelectgame(GameName);
				memorygame = new MemoryGame(socket);
			}
		});

		// 오목게임 선택!
		selectOmokBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "omok";
				sendSelectgame(GameName);
				omokClient = new OmokClient(socket);
			}
		});

		// 숫자 야구 게임 선택!
		selectBaseballBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameName = "baseball";
				sendSelectgame(GameName);
				baseball = new BaseballGame(socket);
			}
		});
	}

	// 서버연결메소드
	// 접속 시 서버 연결 메서드.
	private void connectServer() {
		try {
			socket = new Socket(server, serverPort);
			CI = new clientInfo(socket);
			CI.start();
		} catch (Exception e) {
			System.out.println(server + ": 서버 연결 실패");
		}
	}

	// 서버와의 연결 끊는 메서드
	private void disconnectServer() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			if (socket != null && !socket.isClosed()) {

				socket.close();
				System.out.println("서버와의 연결이 종료되었습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendExitMsg() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("EXIT&퇴장");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 서버에 아이디 전송 메소드
	// ID를 서버에 전달하는 메소드
	private void sendInsertId() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			IDString = IdinputField.getText();
			if ((IDString.equals(""))) { // NULL값 입력시
				IDString = Integer.toString(socket.hashCode());
				writer.println("ID&" + IDString);
				writer.flush();
				gameSelectJPanel.setVisible(true); // gameSelectJPanel 활성화
				gameIdJPanel.setVisible(false); // startJPanel 비활성화

			} else { // 아이디 값 입력시.
				writer.println("ID&" + IDString);
				IdinputField.setText("");
				writer.flush();
				gameSelectJPanel.setVisible(true); // gameSelectJPanel 활성화
				gameIdJPanel.setVisible(false); // startJPanel 비활성화

			}

		} catch (IOException e) {
			System.out.println(server + ": 준비 메세지 요청 실패");
		}
	}

	// 서버에 채팅전송 메소드
	// 채팅을 서버에 전달하는 메소드
	private void sendchat() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendchat = chatJTextField.getText();
			writer.println("CHAT&" + sendchat);
			writer.flush();
			chatJTextField.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 서버에 선택한 게임 전송 메소드
	// 게임을 선택하고 선택한 게임을 서버에 보내는 메소드
	private void sendSelectgame(String gamename) {
		writer.println("gamename&" + gamename);
		writer.flush();
	}

	// 이미지 아이콘 크기 조절 메소드
	private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
		Image xImage = icon.getImage();
		Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		ImageIcon xyImage = new ImageIcon(yImage);
		return xyImage;
	}

	// 메인패널에 이미지 삽입을 위한 메소드
	// 이미지 삽입 패널 클래스(게임배경)
	class MainPanel extends JPanel {
		private ImageIcon icon = new ImageIcon(getClass().getResource("/Client/images/gamemainbg.png"));
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