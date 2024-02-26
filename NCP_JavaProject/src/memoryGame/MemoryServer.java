package memoryGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Server.Game;

public class MemoryServer implements Game {
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	@Override
	public void start(Socket socket) {
		System.out.println("메모리 서버가 시작되었습니다.");
		System.out.println("게임 시작");

		this.socket = socket;
		new SeverThread(socket).start(); // 클라이언트 연결 처리 스레드 시작
	}

	// 클라이언트 통신을 처리하기 위한 내부 클래스
	public class SeverThread extends Thread {
		private Socket socket;

		public SeverThread(Socket socket) {
			this.socket = socket;
		}

//        private String memoryMsg; // 클라이언트 메시지를 저장하는 변수
//        private String gamename; // 선택한 게임 이름을 저장하는 변수

		@Override
		public void run() {
			try {
				// 통신 스트림 설정
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				String clientMsg;
				while ((clientMsg = reader.readLine()) != null) {
					String[] parsedMsg = clientMsg.split("&");
					handleProtocol(parsedMsg);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handleProtocol(String[] parsedMsg) {
		String protocol = parsedMsg[0];
		switch (protocol) {
		case "게임 결과":
			String result = parsedMsg[1];
			System.out.println("게임 결과: " + result);
			// 여기에 결과 처리 로직 추가
			break;
		default:
			System.out.println("알 수 없는 프로토콜입니다.");
			break;
		}
	}
}
