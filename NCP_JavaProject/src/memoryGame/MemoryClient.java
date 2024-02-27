package memoryGame;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class MemoryClient extends JFrame {
	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;

	public MemoryClient(Socket socket) {
		this.socket = socket;
		
	}
}
