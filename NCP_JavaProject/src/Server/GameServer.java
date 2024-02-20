package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BingoGame.Bingo;
import Client.ClientHandler;
import liarGame.LiarGame;

public class GameServer {
	private Map<String, List<Socket>> gameClients; // 클라이언트들의 소켓을 저장하는 리스트
	private Map<String, Game> games;// 게임이름과 해당 게임을 매핑하는 맵

	public GameServer() {
		gameClients = new HashMap<>();
		games = new HashMap<String, Game>();
		games.put("liar", new LiarGame());
		games.put("bingo", new Bingo());
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
	
	
	public static void main1(String[] args) {
	    GameServer gameServer = new GameServer();
	    gameServer.startServer(12345);

	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        while (true) {
	            System.out.println("게임을 선택하세요:");
	            System.out.println("1. Liar Game");
	            System.out.println("2. Bingo Game");
	            System.out.print("번호 입력: ");
	            String choice = reader.readLine().trim();
	            
	            switch (choice) {
	                case "1":
	                    // 클라이언트 소켓을 생성하여 Liar Game 시작 메소드에 전달합니다.
	                    Socket liarSocket = new Socket("127.0.0.1", 12345);
	                    gameServer.startGame(liarSocket, "liar");
	                    break;
	                case "2":
	                    // 클라이언트 소켓을 생성하여 Bingo Game 시작 메소드에 전달합니다.
	                    Socket bingoSocket = new Socket("127.0.0.1", 12345);
	                    gameServer.startGame(bingoSocket, "bingo");
	                    break;
	                default:
	                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}



	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		gameServer.startServer(12345);
	}
}
