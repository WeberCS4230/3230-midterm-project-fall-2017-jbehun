package midterm;

import java.io.*;
import java.net.*;

import blackjack.message.*;

public class ClientMessageHandler {

	private Socket s = null;
	private String name;

	public ClientMessageHandler(String n, Socket s) {
		try {
			name = n;
			ObjectOutputStream writer = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(s.getInputStream());
			writer.writeObject((MessageFactory.getLoginMessage(name)));
			writer.flush();
			StatusMessage responseMessage = (StatusMessage) reader.readObject();
			if (MessageFactory.getAckMessage().getType().equals(responseMessage.getType())) {
				System.out.println("Connection  Accepted");
				new Thread(new InputHandler(reader)).start();
			} else if (MessageFactory.getDenyMessage().getType().equals(responseMessage.getType())) {
				System.out.println("User already connected\n");
				close();
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Unable to connect to server\n");
			close();
		}
	}

	public void close() {
		try {
			if (!s.isInputShutdown()) {
				s.getInputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (!s.isOutputShutdown()) {
				s.getOutputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (!s.isClosed()) {
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class InputHandler implements Runnable {

		private ObjectInputStream input;

		public InputHandler(ObjectInputStream inputStream) {

			input = inputStream;
		}

		@Override
		public void run() {
			try {
				while (!s.isClosed()) {
					System.out.println((Message) input.readObject());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}
}
