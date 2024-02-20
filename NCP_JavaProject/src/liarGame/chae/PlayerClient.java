package liarGame.chae;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 사용자 인터페이스와 서버와의 통신 담당, 클라이언트의 역할 
 */
public class PlayerClient {
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 3000;
	
	// 서버에 메시지를 전송하는 메소드 
	public static void sendMessageToServer(String message) {
		try {
			// 서버에 연결
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			// 데이터 출력 스트림 생성
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			// 메시지 전송
			outputStream.writeUTF(message);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
