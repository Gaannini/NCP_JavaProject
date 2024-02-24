package baseballGame;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import Server.Game;

public class BaseballServer implements Game {
	// 통신
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	private int[] comArr;

	@Override
	public void start(Socket socket) {
		System.out.println("################숫자야구게임################");
		System.out.println("##############[SERVER] 콘솔창##############");

		this.socket = socket;
		new ServerThread(socket).start(); // 클라이언트 연결을 처리하는 쓰레드 시작

		// 난수 생성 메소드 호출
		comArr = getRandomNum();
		System.out.println("[SERVER -> SERVER] 난수 생성 : " + Arrays.toString(comArr));
	}

	// 네트워크 통신을 처리하는 스레드
	public class ServerThread extends Thread {
		private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수
		public PrintWriter writer; // 쓰기 버퍼.
		private BufferedReader reader; // 읽기 버퍼.
		
		public int[] userArr_; // 유저 입력한 배열

		public ServerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				/*
				 * 메세지 처리 루프 클라이언트로부터 메시지를 읽고, 이를 '&'를 기준으로 나누어 handleProtocol() 메소드 호출
				 */
				String clientMsg;
				while ((clientMsg = reader.readLine()) != null) {
					String[] parsedMsg = clientMsg.split("&");
					// Client Thread에서 동작하는 프로토콜
					handleProtocol(parsedMsg);

					// [SERVER -> CLIENT] : 스트라이크와 볼 정보 [strike 수, ball 수]
					String result = decisionBall(comArr, userArr_);
					writer.println("result&" + result);
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 프로토콜 처리: 받은 메시지를 프로토콜에 따라 처리
		private void handleProtocol(String[] parsedMsg) {
			if (parsedMsg.length >= 2) {
				String protocol = parsedMsg[0];
				String data = parsedMsg[1];

				switch (protocol) {
				case "user":
					String[] dataArray = data.split(",");
					userArr_ = new int[dataArray.length];
					for (int i = 0; i < dataArray.length; i++) {
						userArr_[i] = Integer.parseInt(dataArray[i]);
					}
					System.out.println("[CLIENT -> SERVER] 유저가 입력한 숫자들 : " + Arrays.toString(userArr_));
					break;
				}
			}
		}
	}

	// 난수 생성 메소드
	public static int[] getRandomNum() {
		int[] numArr = new int[3];
		// 1번째 난수 생성
		numArr[0] = (int) (Math.random() * 9) + 1;
		// 2번째 난수 생성
		boolean isRun = true;
		while (isRun) {
			int rNum = (int) (Math.random() * 9) + 1;
			// 만약, 1번째 값과 같으면 다시 반복해서 생성하라!!
			if (rNum == numArr[0])
				continue;
			numArr[1] = rNum;
			isRun = false;
		}
		// 3번째 난수 생성
		isRun = true;
		while (isRun) {
			int rNum = (int) (Math.random() * 9) + 1;
			// 1번째 값 또는 2번째 값이 같으면 다시 반복해서 생성하라!!
			if (rNum == numArr[0] || rNum == numArr[1])
				continue;
			numArr[2] = rNum;
			isRun = false;
			System.out.println("");
		}
		return numArr;
	}

	// 스트라이크, 볼을 판단하는 역할
	public static String decisionBall(int[] comArr, int[] userArr) {
		int[] resultArr = new int[2];
		int strike = 0, ball = 0;
		for (int i = 0; i < comArr.length; i++) {
			for (int j = 0; j < userArr.length; j++) {
				// 숫자 일치
				if (comArr[i] == userArr[j]) {
					// 자릿수까지 일치
					if (i == j)
						strike++;
					else
						ball++;
				}
			}
		}
		System.out.println("[SERVER -> SERVER] : 스트라이크는 " + strike + ", 볼은 " + ball);
		resultArr[0] = strike;
		resultArr[1] = ball;
		return Arrays.toString(resultArr);
	}
}