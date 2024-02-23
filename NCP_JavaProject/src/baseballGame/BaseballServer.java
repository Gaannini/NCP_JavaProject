package baseballGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.Game;

public class BaseballServer implements Game {
	private Socket socket;

	@Override
	public void start(Socket socket) {
		this.socket = socket;
		// new exgameserver();
		new clientInfo(socket).start(); // 클라이언트 연결을 처리하는 쓰레드 시작
	}

	public class clientInfo extends Thread {
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수
		public PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.
		public String exMsg; // 클라이언트 아이디를 담는 변수.
		public String gamename; // 클라이언트가 선택한 게임 이름

		public clientInfo(Socket socket) {
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
					// Client Thread에서 동작하는 프로토콜
					A(parsedMsg);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void A(String[] parsedMsg) {
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "ex":
					exMsg = data;
					// clientIds.put(socket, clientId);
					System.out.println(exMsg + "확인");

					// notifyClients(clientId + " is enter the room.", "ID");
					// sendClientList();
					break;
//				case "gamename":
//					System.out.println(clientId + "님이 " + data + "게임을 선택하셨습니다.");
//					// startGame(data);
//					break;
//				// Handle other protocols
				}
			}
		}
	}

}
