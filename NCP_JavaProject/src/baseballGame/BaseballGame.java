package baseballGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class BaseballGame extends JFrame {
	// 통신
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	// 폰트 파일 경로
	String fontFilePath = "Font/BagelFatOne-Regular.ttf";
	Font customFont;
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

	// 숫자 야구 게임 전체 화면
	private JPanel mainPanel;

	// 게임 이름
	private NamePanel namePanel;

	// 유저가 입력한 숫자 패널
	private JPanel numPanel;

	// 유저 입력 배열
	int[] userArr = new int[3];

	// 유저 입력 배열 보이게 하는 라벨
	private JLabel userArrLabel;

	// 유저가 입력했던 숫자 모음 영역
	private JPanel userPanel;

	// 유저가 입력했던 오답 모음 (9개)
	private Panel1 panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	private JPanel panel5;
	private JPanel panel6;
	private JPanel panel7;
	private JPanel panel8;
	private JPanel panel9;

	// 스트라이크, 볼 라벨
//	private JLabel strikeLabel;
//	private JLabel ballLabel;

	// 입력 확인 버튼
	private JButton inputButton;

	// 키보드 패널
	private JPanel keyboardPanel;

	// 키보드 - 버튼(1 ~ 9)
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button9;

	// 키보드 - 버튼(지우개)
	private JButton backButton;

	// 키보드 버튼 - 이미지 아이콘
	private ImageIcon icon1;
	private ImageIcon icon2;
	private ImageIcon icon3;
	private ImageIcon icon4;
	private ImageIcon icon5;
	private ImageIcon icon6;
	private ImageIcon icon7;
	private ImageIcon icon8;
	private ImageIcon icon9;
	private ImageIcon iconBack;

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

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
		}
	}

	class Panel1 extends JPanel {
		private int strike = 0;
		private int ball = 0;

		public void setData(int strike, int ball) {
	        this.strike = strike;
	        this.ball = ball;
		}

		@Override
		public void paintComponent(Graphics g) { 
			int panelWidth = getWidth(); // panel1의 너비
		    int panelHeight = getHeight(); // panel1의 높이

		    // 스트라이크
		    if (strike == 3) {
		        g.drawString("홈S2런!!", panelWidth / 3, panelHeight / 2);
		    } else {
		        if (strike == 0) {
		            g.setColor(Color.RED);
		            g.drawString("S", panelWidth / 8, panelHeight * 3 / 4);
		            g.drawOval(panelWidth / 4, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		            g.drawOval(panelWidth * 3 / 8, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		        } else if (strike == 1) {
		            g.setColor(Color.RED);
		            g.drawString("S", panelWidth / 8, panelHeight * 3 / 4);
		            g.fillOval(panelWidth / 4, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		            g.drawOval(panelWidth * 3 / 8, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		        } else if (strike == 2) {
		            g.drawString("S", panelWidth / 8, panelHeight * 3 / 4);
		            g.fillOval(panelWidth / 4, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		            g.fillOval(panelWidth * 3 / 8, panelHeight / 2, panelWidth / 8, panelHeight / 8);
		        }
		    }

		    // 볼
		    if (ball == 0) {
		        g.setColor(Color.BLUE);
		        g.drawString("B", panelWidth / 8, panelHeight * 9 / 10);
		        g.drawOval(panelWidth / 4, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.drawOval(panelWidth * 3 / 8, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.drawOval(panelWidth / 2, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		    } else if (ball == 1) {
		        g.setColor(Color.BLUE);
		        g.drawString("B", panelWidth / 8, panelHeight * 9 / 10);
		        g.fillOval(panelWidth / 4, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.drawOval(panelWidth * 3 / 8, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.drawOval(panelWidth / 2, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		    } else if (ball == 2) {
		        g.setColor(Color.BLUE);
		        g.drawString("B", panelWidth / 8, panelHeight * 9 / 10);
		        g.fillOval(panelWidth / 4, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.fillOval(panelWidth * 3 / 8, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.drawOval(panelWidth / 2, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		    } else if (ball == 3) {
		        g.setColor(Color.BLUE);
		        g.drawString("B", panelWidth / 8, panelHeight * 9 / 10);
		        g.fillOval(panelWidth / 4, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.fillOval(panelWidth * 3 / 8, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		        g.fillOval(panelWidth / 2, panelHeight * 5 / 8, panelWidth / 8, panelHeight / 8);
		    }
		}
	}

	// BaseballGame 생성자
	public BaseballGame(Socket socket) {
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath));
			ge.registerFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("################숫자야구게임################");
		System.out.println("##############[CLIENT] 콘솔창##############");

		init();
		setting();
		batch();
		listener();
		setVisible(true);

		this.socket = socket;
		new ClientThread().start();
	}

	// 네트워크 통신을 처리하는 스레드
	class ClientThread extends Thread {
		// [CLIENT -> SERVER] : 사용자가 입력한 숫자 배열
		public void sendUserArr(int[] userArr) {
			try {
				// 사용자가 입력한 숫자 배열을 문자열로 변환
				StringBuilder userInputBuilder = new StringBuilder();
				for (int i = 0; i < userArr.length; i++) {
					userInputBuilder.append(userArr[i]);
					if (i != userArr.length - 1) {
						userInputBuilder.append(",");
					}
				}
				String userInput = userInputBuilder.toString();
				writer.println("user&" + userInput);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				/*
				 * 메세지 처리 루프 클라이언트로부터 메시지를 읽고, 이를 '&'를 기준으로 나누어 handleProtocol() 메소드 호출
				 */
				String serverMsg;
				while ((serverMsg = reader.readLine()) != null) {
					String[] parsedMsg = serverMsg.split("&");
					// Client Thread에서 동작하는 프로토콜
					handleProtocol(parsedMsg);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 스레드 종료 시 자원 해제
				try {
					if (writer != null) {
						writer.close();
					}
					if (reader != null) {
						reader.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// 프로토콜 처리: 받은 메시지를 프로토콜에 따라 처리한다.
		private void handleProtocol(String[] parsedMsg) {
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "result":
					System.out.println("[SEVER -> CLIENT] 게임 결과 | 스트라이크,볼 : " + data);
					// 서버로부터 받은 결과를 스트라이크와 볼의 개수로 파싱하여 panel1에 전달
	                String[] result = data.split(",");
	                int strike = Integer.parseInt(result[0]);
	                int ball = Integer.parseInt(result[1]);
	                panel1.setData(strike, ball);
	                // 화면 갱신을 위해 다시 그리기
	                panel1.repaint();
					break;
				}
			}
		}

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
		panel1 = new Panel1(); // Panel1 객체 생성
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
		iconBack = new ImageIcon(getClass().getResource("/baseballGame/imgs/back.jpeg"));
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
		inputButton = new JButton("확인");

		backButton = new JButton(iconBack);
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
		userArrLabel = new JLabel();
		userArrLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
//		strikeLabel = new JLabel("Strike : ");
//		strikeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
//		ballLabel = new JLabel("Ball: ");
//		ballLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

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

		userArrLabel.setFont(customFont.deriveFont(Font.PLAIN, 40)); // 폰트설정

		userPanel.setVisible(true);
		userPanel.setBounds(26, 200, 570, 472);
		
		userPanel.add(panel1);

		panel1.setVisible(true);
		panel2.setVisible(true);
		panel3.setVisible(true);
		panel4.setVisible(true);
		panel5.setVisible(true);
		panel6.setVisible(true);
		panel7.setVisible(true);
		panel8.setVisible(true);
		panel9.setVisible(true);

//		strikeLabel.setBounds(10, 10, 70, 20);
//		ballLabel.setBounds(10, 40, 70, 20);

		inputButton.setBounds(479, 674, 117, 29);

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
		backButton.setBounds(0, 0, buttonWidth, buttonHeigth);

		iconBack = ImageSetSize(iconBack, buttonWidth, buttonHeigth);
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

		numPanel.add(userArrLabel);

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

//		panel1.setLayout(new GridLayout(2, 1)); // 스트라이크, 볼을 위아래로 배치
//	    panel1.add(strikeLabel);
//	    panel1.add(ballLabel);

		mainPanel.add(inputButton);

		keyboardPanel.add(button1);
		keyboardPanel.add(button2);
		keyboardPanel.add(button3);
		keyboardPanel.add(button4);
		keyboardPanel.add(button5);
		keyboardPanel.add(button6);
		keyboardPanel.add(button7);
		keyboardPanel.add(button8);
		keyboardPanel.add(button9);
		keyboardPanel.add(backButton);
	}

	private void listener() {
		// TODO : 버튼 고쳐야 함 (두번 눌러야 서버에 전송됨, 클릭시 초기화도 해야 함)
		inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ClientThread().sendUserArr(userArr);
			}
		});

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(1);
			}
		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(2);
			}
		});
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(3);
			}
		});
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(4);
			}
		});
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(5);
			}
		});
		button6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(6);
			}
		});
		button7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(7);
			}
		});
		button8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(8);
			}
		});
		button9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 버튼을 클릭하면 해당 숫자를 userArr에 추가
				addUserInput(9);
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// userArr[]에서 마지막 입력된 숫자 지우기
				for (int i = userArr.length - 1; i >= 0; i--) {
					if (userArr[i] != 0) {
						userArr[i] = 0;
						break;
					}
				}
				// userArrLabel 업데이트
				updateUserArrLabel();
			}
		});
	}

	// 사용자 입력을 userArr에 추가하고 userArrLabel에 표시하는 메소드
	private void addUserInput(int number) {
		for (int i = 0; i < userArr.length; i++) {
			if (userArr[i] == 0) {
				userArr[i] = number;
				break;
			}
		}
		// userArr를 문자열로 변환하여 userArrLabel에 설정
		StringBuilder userInputBuilder = new StringBuilder();
		for (int i = 0; i < userArr.length; i++) {
			if (userArr[i] != 0) {
				userInputBuilder.append(userArr[i]);
			}
		}
		userArrLabel.setText(userInputBuilder.toString());
	}

	// userArr를 문자열로 변환하여 userArrLabel에 설정
	private void updateUserArrLabel() {
		StringBuilder userInputBuilder = new StringBuilder();
		for (int i = 0; i < userArr.length; i++) {
			if (userArr[i] != 0) {
				userInputBuilder.append(userArr[i]);
			}
		}
		userArrLabel.setText(userInputBuilder.toString());
	}

}