package bingoGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Server.Game;

// 빙고 게임을 구현한 클래스
public class BingoGame extends JFrame implements Game {
	private int[][] board; // 빙고판의 숫자 배열
	private boolean[][] checked; // 빙고판의 선택 여부 배열
	private JPanel boardPanel; // 빙고판을 담을 패널
	private JLabel[][] labels; // 빙고판의 각 라벨

	@Override
	public void start(Socket socket) {
		makeBingoBoard();
		restartGame();
		updateBoard();
		isBingo();
	}

	// 생성자

	// 빙고판 생성 메서드
	public int[][] makeBingoBoard() {
		int[][] board = new int[5][5];
		java.util.List<Integer> numbers = new ArrayList<>();

		// 1부터 25까지의 숫자를 리스트에 추가
		for (int i = 1; i <= 25; i++) {
			numbers.add(i);
		}

		Collections.shuffle(numbers); // 리스트를 섞음

		// 섞인 숫자를 빙고판에 배치
		int putNum = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				board[i][j] = numbers.get(putNum++);
			}
		}
		return board;
	}

	// 게임 재시작 메서드
	public void restartGame() {
		board = makeBingoBoard(); // 새로운 빙고판 생성
		checked = new boolean[5][5]; // 선택 상태 초기화
		updateBoard(); // 빙고판 업데이트
	}

	// 빙고판 업데이트 메서드
	public void updateBoard() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				// 선택된 번호는 "X"로 표시, 선택되지 않은 번호는 해당 번호로 표시
				if (checked[i][j]) {
					labels[i][j].setText("X");
				} else {
					labels[i][j].setText(Integer.toString(board[i][j]));
				}
			}
		}
	}

	// 빙고 여부 확인 메서드
	public boolean isBingo() {
		for (int i = 0; i < 5; i++) {
			// 가로, 세로 방향으로 모든 번호가 선택되었는지 확인
			if (checked[i][0] && checked[i][1] && checked[i][2] && checked[i][3] && checked[i][4]) {
				return true; // 빙고인 경우 true 반환
			}
			if (checked[0][i] && checked[1][i] && checked[2][i] && checked[3][i] && checked[4][i]) {
				return true; // 빙고인 경우 true 반환
			}
		}

		// 대각선 방향으로 모든 번호가 선택되었는지 확인
		if (checked[0][0] && checked[1][1] && checked[2][2] && checked[3][3] && checked[4][4]) {
			return true; // 빙고인 경우 true 반환
		}
		if (checked[0][4] && checked[1][3] && checked[2][2] && checked[3][1] && checked[4][0]) {
			return true; // 빙고인 경우 true 반환
		}
		return false; // 빙고가 아닌 경우 false 반환
	}

	// 메인 메서드
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BingoGame();
			}
		});
	}

	class JFBingo extends JFrame() {

		setTitle("빙고 게임"); // 창 제목 설정
		setSize(400, 400); // 창 크기 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 설정

		// 빙고판을 담을 패널 생성 및 레이아웃 설정
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(5, 5));

		// 빙고판 생성 및 초기화
		board = makeBingoBoard();
		checked = new boolean[5][5];

		// 빙고판의 각 라벨을 생성하여 패널에 추가
		labels = new JLabel[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				final int row = i;
				final int col = j;
				labels[i][j] = new JLabel(Integer.toString(board[i][j]), SwingConstants.CENTER);
				labels[i][j].setFont(new Font("Arial", Font.BOLD, 20));
				labels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				labels[i][j].setOpaque(true);
				labels[i][j].setBackground(Color.WHITE);
				// 각 라벨에 마우스 클릭 이벤트 추가
				labels[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						// 선택되지 않은 번호인 경우 선택 처리
						if (!checked[row][col]) {
							checked[row][col] = true; // 선택 상태로 변경
							updateBoard(); // 빙고판 업데이트
							// 빙고 여부 확인 및 메시지 출력
							if (isBingo()) {
								JOptionPane.showMessageDialog(null, "빙고!~~~");
							}
						}
					}
				});
				boardPanel.add(labels[i][j]); // 라벨을 패널에 추가
			}
		}

		add(boardPanel, BorderLayout.CENTER); // 패널을 프레임의 중앙에 추가

		// 재시작 버튼 생성 및 리스너 등록
		JButton restartButton = new JButton("재시작");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame(); // 게임 재시작
			}
		});
		add(restartButton, BorderLayout.SOUTH); // 버튼을 프레임의 하단에 추가

		setVisible(true); // 프레임 표시
	}

}