package exgame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.Game;

public class exgameserver implements Game {
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
					handleProtocol(parsedMsg);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void handleProtocol(String[] parsedMsg) {
			// 전달된 메시지 배열의 길이가 2 이상인지 확인합니다.
			if (parsedMsg.length >= 2) {
				// 메시지에서 프로토콜과 데이터를 추출합니다.
				String protocol = parsedMsg[0]; // 프로토콜은 배열의 첫 번째 요소입니다.
				String data = parsedMsg[1]; // 데이터는 배열의 두 번째 요소입니다.

				// 프로토콜에 따라 처리합니다.
				switch (protocol) {
				case "ex":
					// 'ex' 프로토콜인 경우
					exMsg = data; // 데이터를 exMsg 변수에 저장합니다.
					// 여기에 필요한 추가 동작을 수행할 수 있습니다.
					System.out.println(exMsg + "확인"); // 데이터 확인을 위해 콘솔에 출력합니다.
					break;
				// 다른 프로토콜에 대한 처리를 추가할 수 있습니다.
				}
			}
		}

	}
}
