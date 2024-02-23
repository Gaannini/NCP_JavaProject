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
	private Socket socket;
	
	  public static void main(String[] args) {
	        final String SERVER_ADDRESS = "192.168.0.34";// 서버 주소
	        final int PORT = 7878; // 서버의 포트 번호

	        try (
	            // 서버에 연결합니다.
	            Socket socket = new Socket(SERVER_ADDRESS, PORT);
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
	        ) {
	            // 서버로부터 메시지를 수신하고 출력합니다.
	            String serverMessage = in.readLine();
	            System.out.println("서버로부터의 메시지: " + serverMessage);

	            // 서버에 메시지를 전송합니다.
	            out.println("클라이언트로부터의 메시지: 연결이 성공적으로 수립되었습니다.");
	        } catch (IOException e) {
	            System.err.println("클라이언트 오류: " + e.getMessage());
	        }
	    }

	public OmokClient(Socket socket) {
		this.socket = socket; // 소켓을 받아서 멤버 변수에 할당
		listener();
		setVisible(true);
//		omokGame.startgame();
	}


	private void listener() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 닉네임 작성 후 게임시작버튼눌렀을때 -> Id가 서버에 전달
		omokJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// connectServer(); // 서버에 연결
				// sendInsertId(); // 입력받은 아이디 서버에 전달
				writer.println("omok&" + "클라이언트");

			}
		});
	}

}
