package midterm;
import java.io.*;
import java.net.*;

import blackjack.message.Message;
import blackjack.message.MessageFactory;

public class Client {

	private Socket s = null;
	private String name;
	private Boolean connected = false;

	public Client(String n, Socket s) {
		try {
			name = n;
			ObjectOutputStream writer = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(s.getInputStream());
			writer.writeObject((MessageFactory.getLoginMessage(name)));
			writer.flush();
			Message responseMessage = (Message)reader.readObject();
			System.out.println(responseMessage.toString());
			if (responseMessage.equals("ACKNOWLEDGE")) {
				connected = true;
				System.out.println("Connection  Accepted");
				new Thread(new InputHandler(s)).start();
			} else if (responseMessage.equals("DENY")) {
				System.out.println("User already connected\n");
				close();
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Unable to connect to server\n");
		}
	}

	public Boolean isConnected() {
		return connected;
	}

	public void close() {
		try {
			s.getInputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			s.getOutputStream().close();
		} catch (IOException e) {
			// printing previous stacktraces closing the inputstream closed the output
			// stream
		}
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newMessage(String message) {
		try {
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class InputHandler implements Runnable {

		private BufferedReader input;

		public InputHandler(Socket s) {
			
			try {
				input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to create reader for input");
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(input.readLine());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}
}
