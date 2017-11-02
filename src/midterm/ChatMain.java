package midterm;

import java.io.IOException;
import java.net.Socket;

public class ChatMain {

	public static void main(String[] args) {

		Socket s = null;

		try {
			//s = new Socket("ec2-54-91-0-253.compute-1.amazonaws.com", 8989);
			s = new Socket("127.0.0.1", 8989);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (s.isConnected()) {
			
			new ClientMessageHandler("Justin", s, new ChatGraphics(s));
		}

	}

}
