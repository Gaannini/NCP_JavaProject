package omokGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

	private PrintWriter writer;
	private Socket socket;

	public OmokClient(Socket socket) {
		this.socket = socket; // 소켓을 받아서 멤버 변수에 할당
		listener();
		setVisible(true);
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
