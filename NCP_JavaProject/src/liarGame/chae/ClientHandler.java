package liarGame.chae;

/**
 * 서버 측에서 클라이언트의 연결을 관리  
 * 클라이언트로부터 메시지를 받아 서버에 전달 
 * 서버로부터 받은 메시지를 클라이언트에게 전달 
 */
import java.io.*; // 입출력 관련 클래스 
import java.net.*; // 네트워크 관련 클래스 

// 클라이언트와의 통신을 담당하는 스레드 
public class ClientHandler implements Runnable {
	// 소켓과 데이터 입력 및 출력 스트림을 저장하는 멤버 변수 선언
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	// 클라이언트 핸들러의 생성자
	public ClientHandler(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	/*
	 * Runnable 인터페이스에서 요구하는 run 메서드 구현 클라이언트로부터 메시지를 받아서 서버에 브로드캐스트 함 클라이언트의 연결이
	 * 종료되면 입력 및 출력 스트림을 닫고 소켓 닫음
	 */
	@Override
	public void run() {
		while (true) {
			try {
				if (inputStream.available() > 0) { // 데이터가 있는지 확인
					String message = inputStream.readUTF();
					// 클라이언트가 보낸 메시지를 서버 콘솔에 출력
					System.out.println("From client: " + message);
					// 서버에 브로드캐스트
					Server.broadcast(message, this);
				} else {
					// 데이터가 없으면 잠시 대기
					Thread.sleep(100);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 클라이언트로 메시지를 보내는 메서드
	public void send(String message) {
		try {
			outputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isSocketClosed() {
	    return socket.isClosed();
	}

}
