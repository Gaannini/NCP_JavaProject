package memoryGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class MemoryGame extends JFrame implements ActionListener {
	private static Socket socket;
	private final int BOARD_SIZE = 4; // 보드 크기
	private final int CARD_SIZE = 100; // 카드 크기
	private List<ImageIcon> symbols; // 카드에 표시될 이미지 목록
	private List<CardButton> buttons; // 카드 버튼 목록
	private CardButton firstCard; // 첫 번째 선택한 카드
	private CardButton secondCard; // 두 번째 선택한 카드
	private long startTime; // 게임 시작 시간
	private PrintWriter out;
	
	double seconds;//결과저장

	public MemoryGame() {
		setTitle("Memory Game");	// JFrame 창의 이름 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(BOARD_SIZE * CARD_SIZE, BOARD_SIZE * CARD_SIZE);	// (행, 열) 설정
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));	// 격자 모양의 그리드 크기 설정

		symbols = generateSymbols();
		buttons = new ArrayList<>();

		// 카드 버튼 생성
		for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
			CardButton button = new CardButton();
			button.addActionListener(this);
			buttons.add(button);
			add(button);
		}
		
		startGame();
	}

	public MemoryGame(Socket socket) {
		this.socket = socket;
		main(null);
	}

	// 이미지 목록 생성
	private List<ImageIcon> generateSymbols() {
	
		List<ImageIcon> symbols = new ArrayList<>();
		for (int i = 1; i <= (BOARD_SIZE * BOARD_SIZE) / 2; i++) {
			ImageIcon image = new ImageIcon("memoryimage/image" + i + ".png"); // 이미지 경로에 맞게 수정
			symbols.add(image);
			symbols.add(image); // 두 번씩 추가하여 쌍을 이룸
		
//	private ImageIcon[] generateSymbols() {
//		ImageIcon[] symbols = new ImageIcon[(BOARD_SIZE * BOARD_SIZE)];
//		for (int i = 0; i < symbols.length / 2; i++) {
//			ImageIcon image = new ImageIcon("memoryimage/image" + (i + 1) + ".png"); // 이미지 경로에 맞게 수정
//			symbols[i] = image;
//			symbols[i + symbols.length / 2] = image; // 쌍을 이룸
		}
		
//		Collections.shuffle(symbols);
		return symbols;
	}

	// 게임 시작
	private void startGame() {
		startTime = System.currentTimeMillis(); // 게임 시작 시간 기록
		for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
			buttons.get(i).setImage(symbols.get(i));
			buttons.get(i).setMatched(false);
			buttons.get(i).setVisible(true);
		}
	}

	// 카드 버튼 클릭 시 호출됨
	@Override
	public void actionPerformed(ActionEvent e) {
		CardButton clickedCard = (CardButton) e.getSource();

		if (!clickedCard.isMatched()) {
			if (firstCard == null) {
				firstCard = clickedCard;
				firstCard.setIcon(firstCard.getImage());
			} else if (secondCard == null && clickedCard != firstCard) {
				secondCard = clickedCard;
				secondCard.setIcon(secondCard.getImage());

				// 두 카드가 일치하는지 확인
				if (firstCard.getImage() == secondCard.getImage()) {
					firstCard.setMatched(true);
					secondCard.setMatched(true);
					firstCard.setEnabled(false);
					secondCard.setEnabled(false);
				}

				// 0.5 초 후에 카드 가리기
				Timer timer = new Timer(500, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						firstCard.setIcon(null);
						secondCard.setIcon(null);
						firstCard = null;
						secondCard = null;
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		}

		// 모든 카드가 매치되었는지 확인
		boolean allMatched = true;
		for (CardButton button : buttons) {
			if (!button.isMatched()) {
				allMatched = false;
				break;
			}
		}
		if (allMatched) {
			long endTime = System.currentTimeMillis(); // 게임 종료 시간 기록
			long elapsedTime = endTime - startTime; // 소요된 시간 계산
			seconds = elapsedTime / 1000.0; // 밀리초를 초로 변환
			//Object[] options = {"확인", "취소"};
			JOptionPane.showMessageDialog(this, "와~~ " + seconds + " 초 걸렸습니다! 짝짝짝..");

			 //결과를 서버에 보냄
			sendResultToServer(seconds);

			dispose();
		}
	}

	//게임결과를 서버로 보냄
	private void sendResultToServer(double seconds) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("게임 결과&" + seconds + " 초 걸렸습니다.");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		MemoryGame game = new MemoryGame();
		game.setVisible(true);
	}

}

class CardButton extends JButton {
	private char symbol;
	private boolean matched2;

	public CardButton(String imagePath) {
		super();
		setPreferredSize(getImageSize(imagePath));
		setIcon(new ImageIcon(imagePath));
	}

	// 이미지의 크기를 가져오는 메소드
	private Dimension getImageSize(String imagePath) {
		ImageIcon icon = new ImageIcon(imagePath);
		int width = icon.getIconWidth();
		int height = icon.getIconHeight();
		return new Dimension(width, height);
	}

	private ImageIcon image;
	private boolean matched;

	public CardButton() {
		super();
		setPreferredSize(new Dimension(100, 100));
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}