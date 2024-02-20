package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Client.ClientHandler;
import liarGame.LiarGame;

public class GameServer {
	private Map<String, List<Socket>> gameClients; // 클라이언트들의 소켓을 저장하는 리스트
	private Map<String, Game> games;// 게임이름과 해당 게임을 매핑하는 맵

	public GameServer() {
		gameClients = new HashMap<>();
		games = new HashMap<String, Game>();
		games.put("liar", new LiarGame());
	}

	// 게임서버를 시작하는 메소드
	public void startServer(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("서버 시작. port : " + port + "...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("클라이언트 연결 : " + clientSocket);

				// 클라이언트 처리를 위한 쓰레드 시작
				ClientHandler clientHandler = new ClientHandler(clientSocket, this);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 클라이언트 소켓을 제거하는 메소드
	public synchronized void removeClientSocket(Socket socket) {
		for (List<Socket> clients : gameClients.values()) {
			clients.remove(socket);
		}
	}

	// 사용자가 선택한 게임 시작 메소드
	public void startGame(Socket socket, String gameName) {
		Game selectedGame = games.get(gameName.toLowerCase());
		if (selectedGame != null) {
			selectedGame.start(socket); // 게임실행

			// 해당게임에 플레이어 저장..?
			List<Socket> clients = gameClients.computeIfAbsent(gameName, k -> new ArrayList<>());
			clients.add(socket);
		} else {
			System.out.println("게임이름을 잘못입력하셨습니다.");
		}
	}
	/*
	 * 클라이언트 목록을 출력하려면 List<Socket> clients = gameClients.getOrDefault("liar", new
	 * ArrayList<>()); System.out.println("Liar Game Clients:"); for (Socket socket
	 * : clients) System.out.println(socket);
	 *
	 * 이런식으로 작성하시면 될거가타여,,,
	 */

	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		gameServer.startServer(12345);
	}
}
