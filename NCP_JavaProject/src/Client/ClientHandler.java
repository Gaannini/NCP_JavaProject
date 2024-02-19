package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Server.GameServer;

public class ClientHandler extends Thread {
	private Socket socket;
	private GameServer gameServer;
	String nickname;
	String gamename;

	public ClientHandler(Socket socket, GameServer gameServer) {
		this.socket = socket;
		this.gameServer = gameServer;
	}

	@Override
	public void run() {
		try {
			// 클라이언트로부터 닉네임과 선택한 게임 정보 읽기
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			nickname = (String) objectInputStream.readObject(); // 닉네임입력
			gamename = (String) objectInputStream.readObject(); // 게임이름입력

			System.out.println("Nickname: " + nickname);
			System.out.println("Selected game: " + gamename);
			gameServer.startGame(socket, gamename);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 클라이언트 소켓을 제거
			gameServer.removeClientSocket(socket);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
