package liarGame.chae;

import java.io.*;    // 입출력 관련 클래스 
import java.net.*;   // 네트워크 관련 클래스 
import java.util.*;  // 유틸리티 클래스 

public class Server {
    private static final int PORT = 3000; // 서버가 수신 대기할 포트 번호
    /* 
     * ClientHandler 객체들을 저장할 수 있는 정적 벡터 clinets 선언 
     * Vector 클래스는 동기화된 스레드에 안전한 리스트
     */
    private static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) throws IOException {
    	/*
    	 * 서버 소켓을 생성하고, 포트번호로 초기화
    	 * 클라이언트 연결을 수락하기 위해 대기 
    	 */
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server 실행 중...");

        // 서버는 계속해서 클라이언트의 연결을 수락하고 처리 
        while (true) {
        	// 클라이언트의 연결을 수락하고, 클라이언트와 통신할 소켓을 생성 
            Socket clientSocket = serverSocket.accept();
            System.out.println("새로운 클라이언트와 연결 : " + clientSocket);

            /* 2줄 
             * 클라이언트와 통신하기 위한 데이터 입력 및 출력 스트림을 생성 
             */
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("이 클라이언트에 대한 새로운 핸들러를 생성 중...");
            // 새로운 클라이언트 핸들러 객체를 생성하고, 클라이언트의 소켓과 입력 및 출력 스트림을 전달 
            ClientHandler clientHandler = new ClientHandler(clientSocket, inputStream, outputStream);

            /* 3줄 
             * 클라이언트 핸들러를 실행할 새로운 스레드를 생성하고 시작 
             * 클라이언트 핸들러를 클라이언트 목록에 추가 
             */
            Thread thread = new Thread(clientHandler);
            clients.add(clientHandler);
            thread.start();
        }
    }

    // 이 메서드는 메시지를 모든 클라이언트에게 브로드캐스트 함 
    /*
     * excludeClient -> 브로드캐스트에서 제외될 클라이언트 
     */
    public static void broadcast(String message, ClientHandler excludeClient) {
        Iterator<ClientHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientHandler client = iterator.next();
            if (client != excludeClient) {
                if (client.isSocketClosed()) { // 클라이언트의 연결 상태를 확인
                    // 클라이언트가 연결을 끊으면 클라이언트 목록에서 제거
                    iterator.remove();
                    continue;
                }
                client.send(message);
            }
        }
    }


}
