package midterm;

import java.io.IOException;
import java.net.Socket;

public class ChatMain {

	public static void main(String[] args) {

		Socket s = null;

		try {
			s = new Socket("ec2-54-91-0-253.compute-1.amazonaws.com", 8989);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (s.isConnected()) {
			Client chatClient = new Client("Justin", s);
		}

	}

}
