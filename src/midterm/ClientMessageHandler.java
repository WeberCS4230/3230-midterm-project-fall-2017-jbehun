package midterm;

import java.io.*;
import java.net.*;

import blackjack.game.Card;
import blackjack.message.*;
import blackjack.message.Message.MessageType;

public class ClientMessageHandler {

	private Socket s = null;
	private String name;
	private ChatGraphics gui;

	public ClientMessageHandler(String n, Socket socket, ChatGraphics g) {
		try {
			gui = g;
			name = n;
			s = socket;
			ObjectOutputStream writer = gui.getGuiOutPutStream();
			ObjectInputStream reader = gui.getGuiInPutStream();
			writer.writeObject((MessageFactory.getLoginMessage(name)));
			writer.flush();
			Message responseMessage = (Message) reader.readObject();
			if (MessageFactory.getAckMessage().getType().equals(responseMessage.getType())) {
				gui.addChatMessage("Connection  Accepted\n");
				new Thread(new InputHandler(reader, writer)).start();
			} else if (MessageFactory.getDenyMessage().getType().equals(responseMessage.getType())) {
				gui.addChatMessage("User already connected\n");
				close();
			}
		} catch (IOException | ClassNotFoundException e) {
			gui.addChatMessage("Unable to connect to server\n");
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
		private ObjectOutputStream output;

		public InputHandler(ObjectInputStream inputStream, ObjectOutputStream outputStream) {

			input = inputStream;
			output = outputStream;
		}

		@Override
		public void run() {
			try {
				while (!s.isClosed() && s.isConnected() && !Thread.interrupted()) {
					Message newMessage = ((Message) input.readObject());
					if (newMessage != null) {
						if (newMessage.getType().equals(MessageType.CHAT)) {
							handleChatMessage((ChatMessage) newMessage);
						}
						if (newMessage.getType().equals(MessageType.CARD)) {
							handlecard((CardMessage) newMessage);
						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}

		private void handleChatMessage(ChatMessage newMessage) {
			if (newMessage.getUsername() != null) {
				gui.addChatMessage(newMessage.getUsername() + ": " + newMessage.getText());
			} else {
				gui.addChatMessage(name + ": " + newMessage.getText());
			}
		}
	}

	public void handlecard(CardMessage newMessage) {
		if (newMessage.getUsername() != null) {
			gui.addChatMessage(newMessage.getUsername() + ": " + newMessage.getCard().getSuite() + " "
					+ newMessage.getCard().getSuite());
		} else {
			gui.addChatMessage(name + ": " + newMessage.getCard().getSuite() + " " + newMessage.getCard().getSuite());
		}

	}
}
