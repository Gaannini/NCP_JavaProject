package omokGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OmokClient extends JFrame {
	JPanel omokJPanel;
	JLabel omokJLabel;
	JButton omokJButton;
//	OmokGame omokGame = new OmokGame();

	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;
	
	public static void main(String[] args) {
	    }

	public OmokClient(Socket socket) {
		this.socket = socket; // 소켓을 받아서 멤버 변수에 할당
		init();
		setting();
		batch();
		listener();
		setVisible(true);
//		omokGame.startgame();
	}
	private void init() {
		//바둑판 생성
		//바둑알 생성
	}
	private void setting() {
		//크기, 타이틀, 사이즈 등등
	}
	private void batch() {
		//add
		
	}


	//클릭할때
	private void listener() {
		//클릭했을때 흰돌놓을지 검은돌놓을지
		
	}
	
	//바둑판 위치 서버에 전달하는 메소드
	

}
