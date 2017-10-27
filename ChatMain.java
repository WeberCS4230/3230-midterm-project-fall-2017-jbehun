import java.net.Socket;

public class ChatMain {

	public static void main(String[] args) {
		Socket s = new Socket("ec2-54-91-0-91-0-253.compute-1amazonaws.com", 8989);
		
		if(s.isConnected()) {
			Client chatClient = new Client("Jusitn", s);
		}

	}

}
