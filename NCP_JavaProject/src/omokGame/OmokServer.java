package omokGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Server.Game;

public class OmokServer implements Game {
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
	OmokGame omokGame = new OmokGame();
	@Override
	public void start(Socket socket) {
		this.socket = socket;
		new clientInfo(socket).start(); // 클라이언트 연결을 처리하는 쓰레드 시작
		
		omokGame.startgame();
	}

	public class clientInfo extends Thread {
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수
		public PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.
		public String omokMsg; // 클라이언트 아이디를 담는 변수.
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
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "omok":
					omokMsg = data;
					// clientIds.put(socket, clientId);
					System.out.println(omokMsg + "확인");
					break;

				}
			}
		}
	}
	

	public void startgame() {
		new OmokGame();
		// TODO Auto-generated method stub
		
	}

}
