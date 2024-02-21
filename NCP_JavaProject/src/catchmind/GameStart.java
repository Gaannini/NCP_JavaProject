package catchmind;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.*;

public class GameStart extends JFrame {
	private static final String TAG = "GameStart :";
	private String IDString; // 클라이언트가 입력한 아이디 값을 클라이언트도 알도록 전역 변수로 설정

	// 제시어 배열 (임시) 아래 TODO 완성 시 삭제
	public String[] problem = { "대통령", "노트북", "커피", "기차" };
	public int selectProblem = 0;

	// TODO: 제시어 파일 txt로 만들고 한 줄씩 불러오기

	// 통신
	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;

	// 시작 화면
	private StartPanel mainPanel; // 메인 화면
	private JButton startButton; // 게임 시작 버튼

	// 아이디 
	private JPanel idPanel;
	private JPanel subPanel;

	// 아이디 - 구성
	private JLabel idLabel; // '아이디를 입력하세요' 라벨
	private TextField idTextField; // 입력 필드
	private JButton idButton; // 입력 완료 버튼

	// 그리기방 패널 
	private JPanel drowRoomPanel; // 화면 전체
	private DrawBoardPanel drawBoardPanel; // 그림판 이미지 

	// room1Panel : 방 이름 / 제시어 / 넘기기 버튼
	private JPanel room1Panel;

	// room1Panel - 구성
	private JLabel wordTitleLabel; // '제시어 : ' 라벨
	private JLabel wordLabel; // 제시어 변수 라벨
	private JButton skipButton; // 넘기기 버튼

	// 중간 그림판
	private JPanel midBoardPanel;

	// 팔레트
	private JPanel palettePanel;

	// 팔레트 - 구성
	private DeleteButton deleteButton; // 휴지통 (전체 삭제)
	private EraserButton eraserButton; // 지우개 (선택 삭제)
	private CrayonPanel crayonPanel; // 크레용판

	// 크레용들
	private JButton blackPenButton;
	private JButton redPenButton;
	private JButton orangePenButton;
	private JButton yellowPenButton;
	private JButton greenPenButton;
	private JButton skybluePenButton;
	private JButton bluePenButton;
	private JButton purplePenButton;

	// room2Panel : 유저목록 / 채팅 / 준비완료 / 나가기 버튼
	private JPanel room2Panel;

	// room2Panel - 구성
	private JTextArea userLisTextArea; // 유저 목록
	private JPanel chatPanel; // 채팅창

	// 채팅창 - 구성
	private TextField chaTextField; // 채팅 입력
	private JTextArea chaTextArea; // 채팅 로그
	private JScrollPane chatScrollPane;// 스크롤

	// room3Panel : 준비완료 / 나가기 버튼
	private JPanel room3Panel;

	// room3Panel - 구성
	private JButton readyButton; // 준비 완료 버튼
	private JButton exitButton; // 나가기 버튼

	// 폰트 크기 설정
	private Font smallFont; // 16px
	private Font mediumFont; // 24px
	private Font largeFont; // 36px

	// 브러쉬 좌표
	int x, y;

	// 캐치마인드에 필요한 선언 
	private BufferedImage bufferedImage;
	private JLabel drawLabel;
	private JPanel drawPanel;
	private Brush brush;
	String sendDraw = null;
	String sendColor = null;
	boolean drawPPAP = true;

	// 크레용 이미지
	private ImageIcon iconGameStart;
	private ImageIcon iconBlackPen;
	private ImageIcon iconRedPen;
	private ImageIcon iconOrangePen;
	private ImageIcon iconYellowPen;
	private ImageIcon iconGreenPen;
	private ImageIcon iconSkyBluePen;
	private ImageIcon iconBluePen;
	private ImageIcon iconPurplePen;

	// 이미지 매서드
	private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
		Image xImage = icon.getImage();
		Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		ImageIcon xyImage = new ImageIcon(yImage);
		return xyImage;
	}

	// 이미지 삽입 패널 클래스
	class StartPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("img/catchMindBG.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};
	
	class DrawBoardPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("img/draw.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};
	
	class CrayonPanel extends JPanel {
		private ImageIcon icon = new ImageIcon("img/drawColor.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	};

	class EraserButton extends JButton {
		private ImageIcon icon = new ImageIcon("img/drawEraser.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
			setBorderPainted(false); 
		}
	};

	class DeleteButton extends JButton {
		private ImageIcon icon = new ImageIcon("img/allDelete.png");
		private Image imgMain = icon.getImage();

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
			setBorderPainted(false); 
		}
	};

	// GameStart 생성자 
	public GameStart() {
		init();
		setting();
		batch();
		listener();
		setVisible(true);
	}

	private void init() {
		// 이미지 삽입 패널 
		mainPanel = new StartPanel(); 
		drawBoardPanel = new DrawBoardPanel();
		crayonPanel = new CrayonPanel(); 
		eraserButton = new EraserButton(); 
		deleteButton = new DeleteButton(); 

		// 패널
		idPanel = new JPanel(); 
		subPanel = new JPanel(); 
		drowRoomPanel = new JPanel();
		room1Panel = new JPanel();
		midBoardPanel = new JPanel();
		palettePanel = new JPanel();
		room2Panel = new JPanel();
		room3Panel = new JPanel(); 
		chatPanel = new JPanel();

		// 이미지
		iconGameStart = new ImageIcon("img/gameStart.png"); 
		iconBlackPen = new ImageIcon("img/drawBlackPen.png");
		iconRedPen = new ImageIcon("img/drawRedPen.png");
		iconOrangePen = new ImageIcon("img/drawOrangePen.png");
		iconYellowPen = new ImageIcon("img/drawYellowPen.png");
		iconGreenPen = new ImageIcon("img/drawGreenPen.png");
		iconSkyBluePen = new ImageIcon("img/drawBluePen.png");
		iconBluePen = new ImageIcon("img/drawIndigoPen.png");
		iconPurplePen = new ImageIcon("img/drawPurplePen.png");

		// 버튼
		startButton = new JButton(iconGameStart);
		idButton = new JButton(iconGameStart); 
		skipButton = new JButton("넘기기"); 
		readyButton = new JButton("준비");
		exitButton = new JButton("나가기"); 
		blackPenButton = new JButton(iconBlackPen);
		redPenButton = new JButton(iconRedPen);
		orangePenButton = new JButton(iconOrangePen);
		yellowPenButton = new JButton(iconYellowPen);
		greenPenButton = new JButton(iconGreenPen);
		skybluePenButton = new JButton(iconSkyBluePen);
		bluePenButton = new JButton(iconBluePen);
		purplePenButton = new JButton(iconPurplePen);

		// 라벨
		idLabel = new JLabel("아이디"); 
		wordTitleLabel = new JLabel("제시어");
		wordLabel = new JLabel("변수");

		// 텍스트 입력란
		idTextField = new TextField(); 
		chaTextField = new TextField(); 

		// 텍스트 영역
		chaTextArea = new JTextArea();
		userLisTextArea = new JTextArea();
		
		// 폰트
		smallFont = new Font("맑은고딕", Font.PLAIN, 16);
		mediumFont = new Font("맑은고딕", Font.PLAIN, 24);
		largeFont = new Font("맑은고딕", Font.PLAIN, 36);

		// 스크롤바 
		chatScrollPane = new JScrollPane(chaTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// 드로우 캔버스
		bufferedImage = new BufferedImage(750, 450, BufferedImage.TYPE_INT_ARGB);
		drawLabel = new JLabel(new ImageIcon(bufferedImage));
		drawPanel = new JPanel();
		brush = new Brush();
	}

	private void setting() {
		setTitle("캐치마인드");
		setSize(800, 640);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
		startButton.setBounds(300, 360, 180, 110); 
		startButton.setBorderPainted(false); 
		iconGameStart = ImageSetSize(iconGameStart, 180, 110); 
		
		idPanel.setLayout(null);
		idPanel.setVisible(false); 
		idPanel.setBackground(new Color(242, 242, 242));
		idPanel.setBounds(180, 200, 420, 300); 

		subPanel.setLayout(null);
		subPanel.setVisible(false); 
		subPanel.setBorder(new LineBorder(new Color(87, 87, 87), 3, true));
		subPanel.setBounds(90, 50, 246, 36); 

		idLabel.setBounds(0, 2, 62, 32); 
		idLabel.setBorder(new LineBorder(new Color(87, 87, 87), 2, true));
		idLabel.setFont(smallFont);
		idLabel.setHorizontalAlignment(JLabel.CENTER); 

		idTextField.setBounds(63, 3, 180, 30); 
		idTextField.setBackground(new Color(242, 242, 242, 255));
		idTextField.setFont(mediumFont);

		idButton.setBounds(120, 150, 180, 110); 
		idButton.setBorderPainted(false); 

		drowRoomPanel.setLayout(null);
		drowRoomPanel.setVisible(false); 
		drowRoomPanel.setBounds(70, 120, 1005, 660);
		
		drawBoardPanel.setLayout(null);
		drawBoardPanel.setBackground(new Color(255, 255, 255, 255));
		drawBoardPanel.setBounds(0, 0, 750, 530);

		room1Panel.setLayout(null);
		room1Panel.setBackground(new Color(255, 255, 255, 0));
		room1Panel.setBounds(0, 0, 750, 80); 

		midBoardPanel.setLayout(null);
		midBoardPanel.setBackground(new Color(255, 255, 255, 255));
		midBoardPanel.setBounds(0, 110, 750, 450); 

		palettePanel.setLayout(null);
		palettePanel.setBackground(new Color(242, 242, 242, 255));
		palettePanel.setBounds(0, 530, 700, 130); 

		iconBlackPen = ImageSetSize(iconBlackPen, 65, 130);
		iconRedPen = ImageSetSize(iconRedPen, 65, 130);
		iconOrangePen = ImageSetSize(iconOrangePen, 65, 130);
		iconYellowPen = ImageSetSize(iconYellowPen, 65, 130);
		iconGreenPen = ImageSetSize(iconGreenPen, 65, 130);
		iconSkyBluePen = ImageSetSize(iconSkyBluePen, 65, 130);
		iconBluePen = ImageSetSize(iconBluePen, 65, 130);
		iconPurplePen = ImageSetSize(iconPurplePen, 65, 130);

		blackPenButton.setBackground(new Color(242, 242, 242, 255));
		blackPenButton.setBounds(0, 0, 65, 130);
		blackPenButton.setBorderPainted(false);

		redPenButton.setBackground(new Color(242, 242, 242, 255));
		redPenButton.setBounds(65, 0, 65, 130);
		redPenButton.setBorderPainted(false); 

		orangePenButton.setBackground(new Color(242, 242, 242, 255));
		orangePenButton.setBounds(130, 0, 65, 130);
		orangePenButton.setBorderPainted(false); 

		yellowPenButton.setBackground(new Color(242, 242, 242, 255));
		yellowPenButton.setBounds(195, 0, 65, 130);
		yellowPenButton.setBorderPainted(false); 

		greenPenButton.setBackground(new Color(242, 242, 242, 255));
		greenPenButton.setBounds(260, 0, 65, 130);
		greenPenButton.setBorderPainted(false); 

		skybluePenButton.setBackground(new Color(242, 242, 242, 255));
		skybluePenButton.setBounds(325, 0, 65, 130);
		skybluePenButton.setBorderPainted(false); 

		bluePenButton.setBackground(new Color(242, 242, 242, 255));
		bluePenButton.setBounds(390, 0, 65, 130);
		bluePenButton.setBorderPainted(false); 

		purplePenButton.setBackground(new Color(242, 242, 242, 255));
		purplePenButton.setBounds(455, 0, 65, 130);
		purplePenButton.setBorderPainted(false); 

		room2Panel.setLayout(null);
		room2Panel.setBounds(750, 0, 255, 530); 
		
		chatPanel.setLayout(null);

		room3Panel.setLayout(null);
		room3Panel.setBackground(new Color(242, 242, 242, 255));
		room3Panel.setBounds(700, 530, 405, 130);

		crayonPanel.setLayout(null);
		crayonPanel.setBackground(new Color(242, 242, 242, 255));
		crayonPanel.setBounds(0, 0, 520, 130); 
		
		eraserButton.setBackground(new Color(242, 242, 242, 255));
		eraserButton.setBounds(520, 0, 80, 130); 

		deleteButton.setBackground(new Color(242, 242, 242, 255));
		deleteButton.setBounds(600, 0, 100, 130); 

		userLisTextArea.setBounds(0, 0, 255, 150); 
		userLisTextArea.setFont(mediumFont);
		userLisTextArea.setBackground(new Color(242, 242, 242, 255));
		userLisTextArea.setLineWrap(true);

		chatPanel.setBackground(Color.WHITE);
		chatPanel.setBounds(0, 150, 255, 385);

		chaTextField.setBackground(Color.WHITE);
		chaTextField.setBounds(0, 350, 255, 30); 
		chaTextField.setFont(mediumFont);
		chaTextField.setBackground(new Color(242, 242, 242, 255));
		chaTextField.setColumns(30);

		chatScrollPane.setBounds(0, 0, 255, 350);
		chatScrollPane.setFocusable(false);

		chaTextArea.setLineWrap(true);
		chaTextArea.setBackground(new Color(242, 242, 242, 255));

		wordTitleLabel.setVisible(true);
		wordTitleLabel.setBounds(0, 2, 155, 65); 
		wordTitleLabel.setFont(mediumFont);
		wordTitleLabel.setBackground(new Color(242, 242, 242, 255));
		wordTitleLabel.setHorizontalAlignment(JLabel.CENTER); 

		wordLabel.setVisible(false);
		wordLabel.setBounds(0, 67, 155, 65); 
		wordLabel.setFont(mediumFont);
		wordLabel.setBackground(new Color(242, 242, 242, 255));
		wordLabel.setHorizontalAlignment(JLabel.CENTER); 

		readyButton.setBounds(150, 2, 155, 65); 
		readyButton.setFont(mediumFont);
		readyButton.setBackground(new Color(242, 242, 242, 255));
		readyButton.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		skipButton.setVisible(false);
		skipButton.setBounds(150, 2, 155, 65); 
		skipButton.setFont(mediumFont);
		skipButton.setBackground(new Color(242, 242, 242, 255));
		skipButton.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		exitButton.setBounds(150, 62, 155, 65); 
		exitButton.setFont(mediumFont);
		exitButton.setBackground(new Color(242, 242, 242, 255));
		exitButton.setBorder(new LineBorder(new Color(87, 87, 87), 5, true));

		drawLabel.setBounds(0, 0, 750, 450);
		drawLabel.setBackground(new Color(255, 255, 255, 0));
		brush.setBounds(0, 0, 750, 450);
	}

	private void batch() {
		mainPanel.add(startButton);
		mainPanel.add(idPanel);
		mainPanel.add(drowRoomPanel);
		startButton.setIcon(iconGameStart);

		idPanel.add(subPanel);
		subPanel.add(idLabel);
		subPanel.add(idTextField);
		idPanel.add(idButton);
		idButton.setIcon(iconGameStart);
		
		drowRoomPanel.add(drawBoardPanel);
		
		drawBoardPanel.add(room1Panel);
		drawBoardPanel.add(midBoardPanel);

		drowRoomPanel.add(palettePanel);
		drowRoomPanel.add(room2Panel);
		drowRoomPanel.add(room3Panel);

		palettePanel.add(crayonPanel);
		palettePanel.add(eraserButton);
		palettePanel.add(deleteButton);

		crayonPanel.add(blackPenButton);
		crayonPanel.add(redPenButton);
		crayonPanel.add(orangePenButton);
		crayonPanel.add(yellowPenButton);
		crayonPanel.add(greenPenButton);
		crayonPanel.add(skybluePenButton);
		crayonPanel.add(bluePenButton);
		crayonPanel.add(purplePenButton);

		room2Panel.add(chatPanel);
		room2Panel.add(userLisTextArea);

		chatPanel.add(chatScrollPane);
		chatPanel.add(chaTextField);

		room3Panel.add(wordLabel);
		room3Panel.add(wordTitleLabel);
		room3Panel.add(skipButton);
		room3Panel.add(readyButton);
		room3Panel.add(exitButton);

		midBoardPanel.add(drawLabel);
		midBoardPanel.add(brush);

	}

	private void listener() {
		// 채팅
		chaTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChat();
			}
		});

		// startButton을 누르면 아이디 패널 등장!
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btnStart = (JButton) e.getSource();
				idPanel.setVisible(true); 
				subPanel.setVisible(true); // plId 활성화
				btnStart.setVisible(false); // btnStart 비활성화
			}
		});

		// 그리기방 입장!
		idButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// JButton btnId = (JButton)e.getSource();
				connectServer(); // 서버와 연결
				sendInsertId();
			}
		});

		// 나가기 버튼 
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendExit();
				System.exit(0);
			}
		});

		// 준비 버튼
		readyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendReady();
			}
		});

		// 넘기기 버튼 
		skipButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendSkip();
			}
		});
		
		// 그리기 
		drawLabel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (drawPPAP == true) {
					System.out.println("ppap true 실행 됨");
					sendDraw = "DRAW&" + e.getX() + "," + e.getY();
					brush.xx = e.getX();
					brush.yy = e.getY();
					brush.repaint();
					brush.printAll(bufferedImage.getGraphics());
					printWriter.println(sendDraw);
				} else {
					System.out.println("ppap false 실행 됨");
				}
			}
		});

		// 검은색 펜
		blackPenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Black";
				brush.setColor(Color.BLACK);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 빨간색 펜
		redPenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Red";
				brush.setColor(Color.RED);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 오렌지색 펜 
		orangePenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Orange";
				brush.setColor(Color.ORANGE);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 노란색 펜
		yellowPenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Yellow";
				brush.setColor(Color.YELLOW);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 초록색 펜 
		greenPenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Green";
				brush.setColor(Color.GREEN);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 하늘색 펜 
		skybluePenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Skyblue";
				brush.setColor(Color.CYAN);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 파란색 펜 
		bluePenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Blue";
				brush.setColor(Color.BLUE);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 보라색 펜 
		purplePenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "Purple";
				brush.setColor(Color.MAGENTA);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 지우개
		eraserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendColor = "COLOR&" + "White";
				brush.setColor(Color.WHITE);
				printWriter.println(sendColor);
				System.out.println("색 변경 : " + sendColor);
			}
		});
		
		// 휴지통 
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete 버튼 눌러짐");
				sendColor = "COLOR&" + "Delete";
				printWriter.println(sendColor);
				brush.setClearC(false);
				cleanDraw();
				System.out.println("드로우 캔버스 초기화");
			}
		});
	}

	// 접속 시 서버 연결 메서드
	private void connectServer() {
		try {
			socket = new Socket("localhost", 3000);
			ReaderThread rt = new ReaderThread();
			rt.start();
		} catch (Exception e) {
			System.out.println(TAG + "서버 연결 실패");
		}
	}

	// EXIT 프로토콜 메서드
	private void sendExit() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("EXIT&" + IDString);
		} catch (Exception e) {
			System.out.println(TAG + "Exit Msg writer fail...");
		}
	}

	// SKIP 프로토콜 메서드
	private void sendSkip() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("SKIP&");
		} catch (Exception e) {
			System.out.println(TAG + "Skip Msg writer fail...");
		}
	}

	// READY 프로토콜 메서드
	private void sendReady() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("READY&");
		} catch (Exception e) {
			System.out.println(TAG + "Ready Msg send fail...");
		}
	}

	// CHAT 프로토콜 메서드
	private void sendChat() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			String chatString = chaTextField.getText();
			printWriter.println("CHAT&" + chatString);
			chaTextField.setText("");
		} catch (Exception e) {
			System.out.println(TAG + "채팅 메세지 요청 실패");
		}
	}

	// ID 프로토콜 메서드
	private void sendInsertId() {
		try {
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			IDString = idTextField.getText();
			if ((IDString.equals(""))) { 
				IDString = "emptyID";
				printWriter.println("ID&" + IDString);
				idPanel.setVisible(false); 
				subPanel.setVisible(false); 
				drowRoomPanel.setVisible(true); 
				setSize(1152, 864);
			} else { 
				printWriter.println("ID&" + IDString);
				idTextField.setText("");
				idPanel.setVisible(false); 
				subPanel.setVisible(false); 
				drowRoomPanel.setVisible(true); 
				setSize(1152, 864);
			}

		} catch (IOException e) {
			System.out.println(TAG + "준비 메세지 요청 실패");
		}
	}

	private void cleanDraw() {
		brush.setClearC(false);
		brush.repaint();
		brush.printAll(bufferedImage.getGraphics());
	}

	class ReaderThread extends Thread {
		private BufferedReader bufferedReader;

		@Override
		public void run() {
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String readerMsg = null;
				String[] parsReaderMsg;
				while ((readerMsg = bufferedReader.readLine()) != null) {
					parsReaderMsg = readerMsg.split("&");
					if (parsReaderMsg[0].equals("DRAW")) {
						String[] drawM = parsReaderMsg[1].split(",");
						x = Integer.parseInt(drawM[0]);
						y = Integer.parseInt(drawM[1]);
						brush.setX(x);
						brush.setY(y);
						brush.repaint();
						brush.printAll(bufferedImage.getGraphics());
						System.out.println("브러쉬 값 : " + drawM);
						System.out.println("브러쉬 값 : " + x);
						System.out.println("브러쉬 값 : " + y);
					} else if (parsReaderMsg[0].equals("COLOR")) {
						System.out.println("색 변경 요청 들어옴");
						if (parsReaderMsg[1].equals("Black")) {
							System.out.println("검은색 요청");
							brush.setColor(Color.BLACK);
						} else if (parsReaderMsg[1].equals("Red")) {
							System.out.println("빨간색 요청");
							brush.setColor(Color.RED);
						} else if (parsReaderMsg[1].equals("Orange")) {
							System.out.println("주황색 요청");
							brush.setColor(Color.ORANGE);
						} else if (parsReaderMsg[1].equals("Yellow")) {
							System.out.println("노랑색 요청");
							brush.setColor(Color.YELLOW);
						} else if (parsReaderMsg[1].equals("Green")) {
							System.out.println("초록색 요청");
							brush.setColor(Color.GREEN);
						} else if (parsReaderMsg[1].equals("SkyBlue")) {
							System.out.println("하늘색 요청");
							brush.setColor(Color.CYAN);
						} else if (parsReaderMsg[1].equals("Blue")) {
							System.out.println("파란색 요청");
							brush.setColor(Color.BLUE);
						} else if (parsReaderMsg[1].equals("Purple")) {
							System.out.println("보라색 요청");
							brush.setColor(Color.MAGENTA);
						} else if (parsReaderMsg[1].equals("White")) {
							System.out.println("지우개 요청");
							brush.setColor(Color.WHITE);
						} else if (parsReaderMsg[1].equals("Delete")) {
							System.out.println("휴지통 요청");
							brush.setClearC(false);
							brush.repaint();
							brush.printAll(bufferedImage.getGraphics());
						}
					} else if (parsReaderMsg[0].equals("SERVER")) {
						chaTextArea.append("[SERVER]: " + parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("CHAT") && parsReaderMsg.length > 1) {
						chaTextArea.append(parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("START")) {
						readyButton.setVisible(false);

					} else if (parsReaderMsg[0].equals("ID")) {
						userLisTextArea.setText("");
					} else if (parsReaderMsg[0].equals("IDLIST")) {
						userLisTextArea.append(parsReaderMsg[1] + "\n");
					} else if (parsReaderMsg[0].equals("TURN")) {
						wordLabel.setText(problem[selectProblem]);
						wordLabel.setVisible(true);
						skipButton.setVisible(true);
						drawPPAP = true;
						chaTextField.setEnabled(false);
						palettePanel.setVisible(true);
						System.out.println("내 턴 임");
					} else if (parsReaderMsg[0].equals("NOTTURN")) {
						wordLabel.setVisible(false);
						skipButton.setVisible(false);
						System.out.println("내 턴 아님");
						brush.setDrawPen(false);
						drawPPAP = false;
						chaTextField.setEnabled(true);
						palettePanel.setVisible(false);
						System.out.println(drawPPAP);
					} else if (parsReaderMsg[0].equals("ANSWER")) {
						selectProblem++;
						if (selectProblem >= problem.length) {
							selectProblem = 0;
						}
					} else if (parsReaderMsg[0].equals("END")) {
						chaTextArea.append("[SERVER]: " + parsReaderMsg[1] + "\n");
						readyButton.setVisible(true);
						chaTextField.setEnabled(true);
						palettePanel.setVisible(true);
						skipButton.setVisible(false);
						readyButton.setVisible(true);
						wordLabel.setVisible(false);
						drawPPAP = true;
					} else {
						chaTextArea.append("\n");
					}
					// 스크롤을 밑으로 고정.
					chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
				}
			} catch (IOException e) {
				System.out.println(TAG + "통신 실패");
			}
		}
	}

	class Brush extends JLabel {
		public int xx, yy;
		public Color color = Color.BLACK;
		public boolean drawPen = true;
		public boolean clearC = true;

		@Override
		public void paint(Graphics g) {
			if (drawPen == true) {
				g.setColor(color);
				g.fillOval(xx - 10, yy - 10, 10, 10);
				System.out.println(drawPPAP);
			} else if (drawPen == false) {
				g.setColor(Color.WHITE);
				g.fillOval(0, 0, 0, 0);
				System.out.println(drawPPAP);
				System.out.println("브러쉬 사용 못 하게 막음");
			}
			if (clearC == true) {
				g.setColor(color);
				g.fillOval(xx - 10, yy - 10, 10, 10);
			} else if (clearC == false) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 1000, 1000);
				clearC = true;
				System.out.println("캔버스 클리어 실행됨");
			}

		}

		public void setX(int x) {
			this.xx = x;
		}

		public void setY(int y) {
			this.yy = y;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public void setDrawPen(boolean drawPen) {
			this.drawPen = drawPen;
		}

		public void setClearC(boolean clearC) {
			this.clearC = clearC;
		}
	}

	public static void main(String[] args) {
		new GameStart();
	}
}
