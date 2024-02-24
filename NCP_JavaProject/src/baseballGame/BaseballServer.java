package baseballGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import Server.Game;

public class BaseballServer implements Game {
	// 통신 
	private Socket socket;
	
	// 난수 배열
	private int[] comArr;

	@Override
	public void start(Socket socket) {
		this.socket = socket;
		new clientInfo(socket).start(); // 클라이언트 연결을 처리하는 쓰레드 시작
		
		// 난수 생성 메소드 호출
	    comArr = getRandomNum();
	    System.out.println("[SERVER] 난수 생성 : " + Arrays.toString(comArr));
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
				// 클라이언트로부터 입력을 읽고 클라이언트에게 추력을 보내기 위해 초기화 
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				/* 메세지 처리 루프
				 * 클라이언트로부터 메시지를 읽고, 이를 '&'를 기준으로 나누어 
				 * handleProtocol() 메소드 호출 
				 */
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

		// 프로토콜 처리: 받은 메시지를 프로토콜에 따라 처리한다. 
		private void handleProtocol(String[] parsedMsg) {
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "baseball":
					exMsg = data;
					System.out.println(exMsg + "확인");
					break;
				}
			}
		}
	}
	
	// 난수 생성 메소드 
	public static int[] getRandomNum() {
		int[] numArr = new int[3];
		// 1번째 난수 생성 
		numArr[0] = (int)(Math.random()*9) + 1; 
		// 2번째 난수 생성
		boolean isRun = true;
		while(isRun) {
			int rNum = (int)(Math.random()*9) + 1; 
			// 만약, 1번째 값과 같으면 다시 반복해서 생성하라!!
			if(rNum == numArr[0])
				continue;
			numArr[1] = rNum;
			isRun = false;
		}
		// 3번째 난수 생성
		isRun = true;
		while(isRun) {
			int rNum = (int)(Math.random()*9) + 1; 
			// 1번째 값 또는 2번째 값이 같으면 다시 반복해서 생성하라!!
			if(rNum == numArr[0] || rNum == numArr[1])
				continue;
			numArr[2] = rNum;
			isRun = false;
			System.out.println("");
		}
		return numArr;
	}

}