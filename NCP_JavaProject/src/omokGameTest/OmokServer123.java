package omokGameTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Server.Game;

public class OmokServer123 implements Game {
	
	private GoEgg[][] goEgg; // 인스턴스 변수로 선언
	private Socket socket;
	

	public static void main(String[] args) {
		final int PORT = 7878; // 사용할 포트 번호

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("서버가 포트 " + PORT + "에서 실행 중...");

			// 클라이언트의 연결을 수신하고, 클라이언트와 통신할 소켓을 얻습니다.
			Socket clientSocket = serverSocket.accept();
			System.out.println("클라이언트가 연결되었습니다.");

			// 클라이언트와 데이터를 주고받을 입력 및 출력 스트림을 얻습니다.
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// 클라이언트에게 메시지를 전송합니다.
			out.println("서버로부터의 메시지: 연결이 성공적으로 수립되었습니다.");

			// 클라이언트로부터 메시지를 수신하고 출력합니다.
			String clientMessage = in.readLine();
			System.out.println("클라이언트로부터의 메시지: " + clientMessage);

			// 클라이언트와 통신을 종료합니다.
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("서버 오류: " + e.getMessage());
		}
	}

	public void start(Socket socket) {
		this.socket = socket;
		new ClientInfo(socket).start(); // 클라이언트 연결을 처리하는 쓰레드 시작
	}

	// 클라이언트 정보를 처리하는 쓰레드
	public class ClientInfo extends Thread implements Game{
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수
		private PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.

		public ClientInfo(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				String clientMsg;
				while ((clientMsg = reader.readLine()) != null) {
					String[] parsedMsg = clientMsg.split("&");
					handleProtocol(parsedMsg);

					// 클라이언트가 보낸 GoEgg 객체를 읽어옴
					ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
					GoEgg receivedEgg = (GoEgg) inputStream.readObject();

					// 승리 여부를 확인하고 결과를 클라이언트에게 전송
					String win = checkWin(receivedEgg, goEgg);
					writer.println("win&" + win);
					writer.flush();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		private void handleProtocol(String[] parsedMsg) {
			// 클라이언트로부터 받은 프로토콜 처리
		}

		@Override
		public void start(Socket socket) {
			// TODO Auto-generated method stub
			
		}
	}

	// 클라이언트가 놓은 돌과 게임 보드를 기반으로 승리 여부를 확인하는 메서드
	public String checkWin(GoEgg receivedEgg, GoEgg[][] goEgg) {
		// 게임 보드를 기반으로 승리 여부를 확인하는 로직을 구현하세요.
		// receivedEgg와 goEgg를 활용하여 승리 여부를 확인하고, 그 결과를 반환합니다.
		// 이 메서드는 클라이언트가 놓은 돌과 게임 보드를 기반으로 승리 여부를 확인합니다.
		return null;
	}

	// 클라이언트가 놓은 돌을 나타내는 클래스
	public class GoEgg extends JButton {
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
