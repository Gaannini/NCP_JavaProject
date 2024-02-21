package omokGame;

import java.net.Socket;
import java.util.Scanner;

import Server.Game;
public class OmokGame implements Game{
	
	@Override
	public void start(Socket socket) {
		System.out.println("===오목게임 시작===");
		
	}
	
	
    private static final int SIZE = 15;
    private static final int EMPTY = 0;
    private static final int BLACK = 1;
    private static final int WHITE = 2;
    
    private int[][] board;
    private boolean blackTurn; // true면 흑돌 차례, false면 백돌 차례
    
    public OmokGame() {
        board = new int[SIZE][SIZE];
        blackTurn = true;
    }
    
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    System.out.print(". ");
                } else if (board[i][j] == BLACK) {
                    System.out.print("● ");
                } else if (board[i][j] == WHITE) {
                    System.out.print("○ ");
                }
            }
            System.out.println();
        }
    }
    
    public boolean placeStone(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE || board[y][x] != EMPTY) {
            return false; // 잘못된 위치에 돌을 놓을 경우 false 반환
        }
        
        if (blackTurn) {
            board[y][x] = BLACK;
        } else {
            board[y][x] = WHITE;
        }
        
        blackTurn = !blackTurn; // 플레이어 차례 변경
        return true; // 돌을 성공적으로 놓았을 경우 true 반환
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OmokGame game = new OmokGame();
        
        while (true) {
            game.printBoard();
            
            int player = game.blackTurn ? BLACK : WHITE;
            String playerName = game.blackTurn ? "흑돌" : "백돌";
            
            System.out.println(playerName + "의 차례입니다. 돌을 놓을 위치를 입력하세요 (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            
            if (game.placeStone(x, y)) {
                if (game.checkWin(player, x, y)) {
                    game.printBoard();
                    System.out.println(playerName + "이(가) 승리했습니다!");
                    break;
                }
            } else {
                System.out.println("잘못된 위치입니다. 다시 입력해주세요.");
            }
        }
        
        scanner.close();
    }
    
    // 승리 조건 확인 메서드 (가로, 세로, 대각선)
    public boolean checkWin(int player, int x, int y) {
        int count = 1;
        
        // 가로 방향 체크
        count += countStones(player, x, y, 1, 0); // 오른쪽
        count += countStones(player, x, y, -1, 0); // 왼쪽
        
        if (count >= 5) {
            return true;
        }
        
        // 세로 방향 체크
        count = 1;
        count += countStones(player, x, y, 0, 1); // 아래
        count += countStones(player, x, y, 0, -1); // 위
        
        if (count >= 5) {
            return true;
        }
        
        // 대각선 방향 체크
        count = 1;
        count += countStones(player, x, y, 1, 1); // 오른쪽 아래
        count += countStones(player, x, y, -1, -1); // 왼쪽 위
        
        if (count >= 5) {
            return true;
        }
        
        count = 1;
        count += countStones(player, x, y, 1, -1); // 오른쪽 위
        count += countStones(player, x, y, -1, 1); // 왼쪽 아래
        
        return count >= 5;
    }
    
    // 특정 방향으로 연속된 돌의 개수를 세는 메서드
    public int countStones(int player, int x, int y, int dx, int dy) {
        int count = 0;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || x >= SIZE || y < 0 || y >= SIZE || board[y][x] != player) {
                break;
            }
            count++;
        }
        return count;
    }


}
