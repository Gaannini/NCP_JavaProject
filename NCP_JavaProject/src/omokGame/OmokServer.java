package omokGame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import Server.Game;

public class OmokServer implements Game {


    private Map<int[][], String> playerEgg; //돌변경 위치, 클라이언트1/2
    private static ServerSocket serverSocket; // 서버 소켓.
    private static Socket clientsocket; // 클라이언트가 접속하면 새로 만드는 소켓

    //생성자
    public OmokServer(){

    }

    public static void main(String[] args) {
        OmokServer.startServer(99999);
    }
    public static void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("서버 시작. port : " + port + "...");

            while (true) {
                clientsocket = serverSocket.accept(); // 서버의 접속을 대기중
                System.out.println("클라이언트 연결 : " + clientsocket);


//                // 클라이언트 처리를 위한 쓰레드 시작
//                clientInfo CI = new clientInfo(clientsocket);
//                CI.start();
            }
        } catch (Exception e) {
            System.out.println("게임서버 연결안됨.");
        }
    }

    //client 받을것들, 돌위치 변경,, 전달 할것들..?
    public class clientInfo extends Thread {

        private final Socket socket;

        public clientInfo(Socket socket) {
            this.socket = socket;
        }

    }

	@Override
	public void start(Socket socket) {
		// TODO Auto-generated method stub
		
	}

    }
