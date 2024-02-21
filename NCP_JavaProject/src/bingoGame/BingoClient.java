package bingoGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BingoClient {
    private static final String SERVER_IP = "192.168.0.158"; // 서버 IP
    private static final int SERVER_PORT = 12345; // 서버 포트

    public static void main(String[] args) {
        try {
            // 서버에 연결
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("서버에 연결됨");

            // 입출력 스트림 생성
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 사용자로부터 닉네임 입력 받음
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your nickname: ");
            String nickname = userInputReader.readLine();

            // 닉네임을 서버에 전송
            out.println("ID&" + nickname);

            // 선택한 게임 이름을 서버에 전송
            System.out.print("Choose your game: ");
            String gameName = userInputReader.readLine();
            out.println("gamename&" + gameName);

            System.out.println("당신의 닉네임은 '" + nickname + "' 이고 선택한 게임은 '" + gameName + "' 입니다.");

            // 서버로부터의 응답 출력
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println("Server: " + serverResponse);
            }

            // 클라이언트 종료
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}