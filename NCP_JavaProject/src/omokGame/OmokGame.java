package omokGame;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.Game;

public class OmokGame extends JFrame implements Game {
	

	@Override
	public void start(Socket socket) {
		
	}
	GoEgg goEgg[][];
	ImageIcon img = new ImageIcon("images//empty.png");
	ImageIcon white = new ImageIcon("images//white.png");
	ImageIcon black = new ImageIcon("images//black.png");
	ImageIcon turn = black;
	
		public OmokGame() {
		
	}
	public void startgame() {
//		OmokBoard();
		OmokGame.main(null);
	}
	public void OmokBoard() {
		setTitle("===오목게임===");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(new GridLayout(26, 26));

		goEgg = new GoEgg[26][];

		myActionListener goAction = new myActionListener();
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

	class myActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
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

	}

	public void checkWin(GoEgg e) {
		//오목 조건 체크
		int checkx = e.x;
		int checky = e.y;
		int count = 0;
		//세로 방향 체크
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
				JOptionPane.showMessageDialog(null,  "흑돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "백돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			}

		}
		/////////////////////////////////
		checkx = e.x;
		checky = e.y;
		count = 0;
		
		//가로 방향 체크
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
				JOptionPane.showMessageDialog(null, "흑돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "백돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			}
		}
		////////////////////
		checkx = e.x;
		checky = e.y;
		count = 0;

		//↖방향 체크
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
				JOptionPane.showMessageDialog(null, "흑돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "백돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			}
		}
		//		//////////////////
		checkx = e.x;
		checky = e.y;
		count = 0;

		//↗↙방향 체크
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
				JOptionPane.showMessageDialog(null, "흑돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "백돌이 승리했습니다.","게임종료", JOptionPane.QUESTION_MESSAGE);
			}
		}

	}

	public static void main(Socket socket) {
		new OmokGame();
		OmokGame omokGame = new OmokGame();
		omokGame.OmokBoard();	
		
		
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
