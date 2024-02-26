package omokGame;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.naming.InitialContext;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Server.Game;

public class OmokClient extends JFrame implements Game {
	JPanel omokJPanel;
	JLabel omokJLabel;
	JButton omokJButton;
//	OmokGame omokGame = new OmokGame();

	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;
	ImageIcon img = new ImageIcon("images//empty.png");
	ImageIcon white = new ImageIcon("images//white.png");
	ImageIcon black = new ImageIcon("images//black.png");
	ImageIcon turn = black;

	@Override
	public void start(Socket socket) {
		// TODO Auto-generated method stub

	}

	public OmokClient(Socket socket) {
		this.socket = socket; // 소켓을 받아서 멤버 변수에 할당
		listener listener = new listener();
		listener.init();
		setting();
		batch();
		new listener();
		setVisible(true);
		new GoEgg(EXIT_ON_CLOSE, ABORT, img);
//		omokGame.startgame();
	}

	//
	
	private void batch() {
		// add

	}

	private  void setting() {
		// 크기, 타이틀, 사이즈 등등
	

		setTitle("===오목게임===");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(new GridLayout(26, 26));
	}

	// 클릭했을때 흰돌놓을지 검은돌놓을지
	private class listener extends OmokGame implements ActionListener {
		GoEgg goEgg[][];
		private Container c;

		@Override
		public void actionPerformed(ActionEvent e) {
			omokGame.GoEgg wi = (omokGame.GoEgg) e.getSource();
			if (turn == white) {
				wi.setIcon(white);
				wi.state = "W";
				turn = black;

			} else {
				wi.setIcon(black);
				wi.state = "B";
				turn = white;
			}
			checkWin(wi);
			wi.removeActionListener(this);
		}

		private  void setting() {
			// 크기, 타이틀, 사이즈 등등
			setTitle("===오목게임===");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			c = getContentPane();
			c.setLayout(new GridLayout(26, 26));
		}
		
		//착수한 돌을 배열에 저장
		private void init (){
			listener goAction = new listener();
			
			for (int i = 0; i < 26; i++) {
				goEgg[i] = new GoEgg[26];
				for (int j = 0; j < 26; j++) {
					goEgg[i][j] = new GoEgg(i, j, img);
					c.add(goEgg[i][j]);
					goEgg[i][j].addActionListener(goAction);
					goEgg[i][j].setBorderPainted(false);
				}
			}

			setSize(1000, 1000);
			setVisible(true);
			// 바둑알 생성

		}
		
		//오목 좌표 생성
		class GoEgg extends JButton {
			int x;
			int y;
			String state;

			public GoEgg(int x, int y, ImageIcon image) {
				super(image);
				this.x = x;
				this.y = y;
				state = "N";
			}

		}
	}
}



// 바둑판 위치 서버에 전달하는 메소드
