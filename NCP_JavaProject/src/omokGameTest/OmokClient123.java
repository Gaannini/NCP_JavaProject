package omokGameTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OmokClient123 extends JFrame {
    private Socket socket;
    private ImageIcon img = new ImageIcon("images//empty.png");
    private ImageIcon white = new ImageIcon("images//white.png");
    private ImageIcon black = new ImageIcon("images//black.png");
    private ImageIcon turn = black;
    private GoEgg[][] goEgg = new GoEgg[26][26];

    public OmokClient123(Socket socket) {
        this.socket = socket;
        Listener listener = new Listener();
        listener.init();
        setting();
        batch();
        setVisible(true);
    }

    private void batch() {
        // TODO: Add batch operations
    }

    private void setting() {
        setTitle("===오목게임===");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new GridLayout(26, 26));
    }

    // 클릭했을 때 흰 돌이나 검은 돌을 놓는 이벤트 처리
    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GoEgg wi = (GoEgg) e.getSource();
            if (turn == white) {
                wi.setIcon(white);
                wi.state = "W";
                turn = black;
            } else {
                wi.setIcon(black);
                wi.state = "B";
                turn = white;
            }

            // 클라이언트가 놓은 돌의 정보를 서버로 전송
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(wi); // GoEgg 객체를 서버로 전송
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 서버로부터 승리 여부를 받는 코드는 여기에 추가

            // wi.removeActionListener(this); // 오류 발생 가능성이 있는 코드
        }

        // 초기화 메서드
        private void init() {
            Container c = getContentPane();
            for (int i = 0; i < 26; i++) {
                goEgg[i] = new GoEgg[26];
                for (int j = 0; j < 26; j++) {
                    goEgg[i][j] = new GoEgg(i, j, img);
                    c.add(goEgg[i][j]);
                    goEgg[i][j].addActionListener(this);
                    goEgg[i][j].setBorderPainted(false);
                }
            }

            setSize(1000, 1000);
            setVisible(true);
        }
    }

    // GoEgg 클래스 정의
    private class GoEgg extends JButton {
        private int x;
        private int y;
        private String state;

        public GoEgg(int x, int y, ImageIcon icon) {
            super(icon);
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        // 클라이언트 코드에서 서버에 연결하는 부분을 여기에 작성
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 7878);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new OmokClient123(socket);
    }
}
