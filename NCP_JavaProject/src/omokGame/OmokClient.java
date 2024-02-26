package omokGame;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

import Server.Game;

public class OmokClient extends JFrame {

	//
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private clientInfo CI;

	// 화면

	private ImageIcon img = new ImageIcon("images//empty.png");
	private ImageIcon white = new ImageIcon("images//white.png");
	private ImageIcon black = new ImageIcon("images//black.png");
	private ImageIcon turn = black;

	GoEgg[][] goEgg = new GoEgg[26][];

	// 생성자
	public OmokClient(Socket socket) {
//		
		init(); // 초기화 메서
		setVisible(true);

		this.socket = socket;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
//                sendExitMsg();
//                disconnectServer(); // 서버와의 연결 끊기
			}
		});
	}

	// 서버에서 상대방 받기위한 스레드
	public class clientInfo extends Thread {
		private Socket socket;
		private BufferedReader reader;
		public int[][] goEgg;

		public clientInfo(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 화면 생성
	private void init() {

		setTitle("===오목 게임===");
		// 윈도우 창 종료시 프로세스 종료
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(new GridLayout(26, 26));

		ActionListener goAction = new myActionListener();
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

	}

	// 게임 이벤트 처리
	class myActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			GoEgg wi = (GoEgg) e.getSource();
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

		// 승패 판단
		public void checkWin(GoEgg e) {
			int checkx = e.x;
			int checky = e.y;
			int count = 0;
			while (checky >= 0 && goEgg[checkx][checky].state.equals(e.state)) {
				checky -= 1;
			}
			checky += 1;
			while (checky < 26 && goEgg[checkx][checky].state.equals(e.state)) {
				checky += 1;
				count++;
			}
			if (count == 5) {
				if (e.state.equals("B")) {
					JOptionPane.showMessageDialog(null, "흑돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null, "백돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				}

			}
			checkx = e.x;
			checky = e.y;
			count = 0;

			while (checkx >= 0 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx -= 1;
			}
			checkx += 1;
			while (checkx < 26 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx += 1;
				count++;
			}
			if (count == 5) {
				if (e.state.equals("B")) {
					JOptionPane.showMessageDialog(null, "흑돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null, "백돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				}

			}
			////////////////////
			checkx = e.x;
			checky = e.y;
			count = 0;

			while (checkx >= 0 && checky >= 0 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx -= 1;
				checky -= 1;
			}
			checkx += 1;
			checky += 1;
			while (checkx < 26 && checky < 26 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx += 1;
				checky += 1;
				count++;
			}
			if (count == 5) {
				if (e.state.equals("B")) {
					JOptionPane.showMessageDialog(null, "흑돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null, "백돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				}

			}

			checkx = e.x;
			checky = e.y;
			count = 0;

			while (checkx >= 0 && checky < 26 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx -= 1;
				checky += 1;
			}
			checkx += 1;
			checky -= 1;
			while (checkx < 26 && checky >= 0 && goEgg[checkx][checky].state.equals(e.state)) {
				checkx += 1;
				checky -= 1;
				count++;
			}

			if (count == 5) {
				if (e.state.equals("B")) {
					JOptionPane.showMessageDialog(null, "흑돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null, "백돌이 승리하였습니다.", "게임 승리", JOptionPane.QUESTION_MESSAGE);
					int result = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?.", "게임종료", JOptionPane.YES_OPTION);
					if (result == JOptionPane.YES_OPTION)
						System.exit(0);

				}

			}
		}

	}

}

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