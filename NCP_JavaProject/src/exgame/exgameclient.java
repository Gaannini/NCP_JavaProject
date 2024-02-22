package exgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class exgameclient extends JFrame {
	JPanel exJPanel;
	JLabel exJLabel;
	JButton exJButton;

	private PrintWriter writer;
	private Socket socket;

	public exgameclient(Socket socket) {
		this.socket = socket; // 소켓을 받아서 멤버 변수에 할당
		init();
		batch();
		setting();
		listener();
		setVisible(true);
	}

	public void init() {
		setSize(500, 500);
		exJPanel = new JPanel();
		exJPanel.setBounds(0, 0, 500, 500);
		exJLabel = new JLabel("ex");
		exJButton = new JButton("확인");
	}

	// 화면에 배치
	private void batch() {
		exJPanel.add(exJLabel);
		exJPanel.add(exJButton);

	}

	private void setting() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		// 게임시작화면
		getContentPane().add(exJPanel);

		exJPanel.setLayout(null);
		exJPanel.setVisible(true); // 시작때 화면 표시

		exJLabel.setBounds(100, 6, 100, 50);
		exJButton.setBounds(204, 6, 100, 50);
	}

	private void listener() {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 닉네임 작성 후 게임시작버튼눌렀을때 -> Id가 서버에 전달
		exJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// connectServer(); // 서버에 연결
				// sendInsertId(); // 입력받은 아이디 서버에 전달
				writer.println("ex&" + "클라이언트");

			}
		});
	}

}
