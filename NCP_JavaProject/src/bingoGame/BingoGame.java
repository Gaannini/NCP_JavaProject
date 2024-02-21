package bingoGame;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import Server.Game;

public class BingoGame implements Game {
	
	@Override
	public void start(Socket socket) {
		int[][] board = makeBingoBoard();

		playBingo(board);
	}

	public static int[][] makeBingoBoard() {
		int[][] board = new int[5][5];
		List<Integer> numbers = new ArrayList<>();

		for (int i = 1; i <= 25; i++) {
			numbers.add(i); // 1~25 값을 numbers에 대입
		}

		Collections.shuffle(numbers);

		int putNum = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				board[i][j] = numbers.get(putNum++);
			}
		}

		return board;
	}

	public static void playBingo(int[][] board) {
		Scanner scanner = new Scanner(System.in);
		boolean[][] checked = new boolean[5][5];
		System.out.println("==== 빙고 게임 시작 ====");
		while (true) {

			printBoard(board, checked);

			System.out.print("숫자를 입력하세요: ");
			int number = scanner.nextInt();

			markNumber(board, checked, number);

			if (isBingo(checked)) {
				System.out.println("빙고! 축하합니다!");
				break;
			}
		}
		scanner.close();
	}

	public static void printBoard(int[][] board, boolean[][] checked) {
		System.out.println("--------------------------");
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (checked[i][j]) {
					System.out.print("| ** ");
				} else {
					System.out.printf("| %02d ", board[i][j]);
				}
			}
			System.out.println("|");
			System.out.println("--------------------------");
		}
	}

	public static void markNumber(int[][] board, boolean[][] checked, int number) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (board[i][j] == number) {
					checked[i][j] = true;
					return;
				}
			}
		}
	}

	public static boolean isBingo(boolean[][] checked) {
		for (int i = 0; i < 5; i++) {
			if (checked[i][0] && checked[i][1] && checked[i][2] && checked[i][3] && checked[i][4]) {
				return true;
			}
			if (checked[0][i] && checked[1][i] && checked[2][i] && checked[3][i] && checked[4][i]) {
				return true;
			}
		}	// 여기서 반복문을 사용하는 이유는 가로&세로 빙고 확인하기 위해서

		if (checked[0][0] && checked[1][1] && checked[2][2] && checked[3][3] && checked[4][4]) {
			return true;
		}
		if (checked[0][4] && checked[1][3] && checked[2][2] && checked[3][1] && checked[4][0]) {
			return true;
		}
		return false;
		
	}
}