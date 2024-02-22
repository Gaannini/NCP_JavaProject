package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient {
	public static void main(String[] args) {
		String server = "192.168.0.34";
		int serverPort = 12345;

		try {
			Socket socket = new Socket(server, serverPort);
			System.out.println("Connected to server.");
			// ObjectOutputStream 객체 생성
			// 객체를 직렬화하여 출력 스트림으로 전송하기 위한 클래스
			// 클라이언트 소켓의 출력 스트림을 이용하여 서버에 데이터를 전송하기 위해 생성
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			// 사용자 입력을 위한 bufferedReader
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			

			// 사용자로부터 닉네임 입력 받고 서버에 전송
			System.out.print("닉네임을 입력하세요 >> ");
			String nickname = bf.readLine();
			objectOutputStream.writeObject(nickname); // 닉네임 전송

			// 선택한 게임 이름을 서버에 전송
			System.out.print("게임을 선택하세요 >> ");
			String gamename = bf.readLine();
			objectOutputStream.writeObject(gamename); // 게임 선택

			System.out.println("당신의 닉네임은 '" + nickname + "' 이고 선택한 게임은 '" + gamename + "' 입니다.");
			
			 String result = bf.readLine();
	            System.out.println(result);

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
