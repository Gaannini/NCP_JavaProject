package liarGame.chae;

// swing 및 AWT 라이브러리를 가져옴 ->GUI 구축 
import javax.swing.*;    
import java.awt.*;      
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.*;    // 입출력 관련 클래스 

// JFrame을 확장하여 게임 UI 구현 
public class GameUI extends JFrame {
	// 텍스트 채팅을 표시하는 JTextArea 
    private JTextArea chatArea;  
    // 사용자가 메시지를 입력할 수 있는 JTextField 
    private JTextField messageField;

    // 생성자 
    public GameUI() {
    	// SwingUtilities.invokeLater()를 사용하면 스윙 스레드에서 안전하게 사용 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeUI();
            }
        });
    }

    // UI의 타이틀, 크기 및 닫기 작업 설정 
    private void initializeUI() {
        setTitle("Liar Game");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();                        // 패널 생성 
        panel.setLayout(new BorderLayout());                // BorderLayout 설정 

        chatArea = new JTextArea();                         // 채팅 내용을 표시하는 JTextArea 생성 
        chatArea.setEditable(false);                        // 편집 불가능 
        JScrollPane scrollPane = new JScrollPane(chatArea); // 스크롤 가능한 영역 
        panel.add(scrollPane, BorderLayout.CENTER);         // 패널의 중앙에 추가 

        // 메시지를 입력할 수 있는 하단 패널 생성 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 전송 버튼에 ActionListener 추가 -> 버튼 클릭 시 사용자가 입력 메시지를 가져와서 sendMessage 메서드 호출 
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                sendMessage(message);
            }
        });

        // 프레임의 컨텐츠 패널로 위에서 구성한 패널을 추가하고, 프레임을 표시 
        getContentPane().add(panel);
        setVisible(true);
    }

    private void sendMessage(String message) {
    	// PlayerClient를 통해 서버로 메시지를 전송
    	PlayerClient.sendMessageToServer(message);;
    	
    	// 전송 후 텍스트 필드를 비움
    	messageField.setText("");
    }

    public static void main(String[] args) {
        new GameUI();
    }
}
