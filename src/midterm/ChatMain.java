package midterm;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ChatMain {

	public static void main(String[] args) {

		Socket s = null;
		String host = JOptionPane.showInputDialog("Enter the host address");
		try {
			s = new Socket(host, 8989);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (s.isConnected()) {
			String name = JOptionPane.showInputDialog("Enter a user name");
			new ClientMessageHandler(name, s, new ChatGraphics(s));
		}

	}

}
