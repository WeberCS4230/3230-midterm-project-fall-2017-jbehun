package midterm;

import java.io.*;
import java.net.*;
import blackjack.message.*;
import blackjack.message.GameStateMessage.GameAction;
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
				new Thread(new InputHandler(reader)).start();
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

		public InputHandler(ObjectInputStream inputStream) {

			input = inputStream;
		}

		@Override
		public void run() {
			try {
				while (!s.isClosed() && s.isConnected() && !Thread.interrupted()) {
					Message newMessage = ((Message) input.readObject());
					if (newMessage != null) {
						if (newMessage.getType().equals(MessageType.ACK)
								|| newMessage.getType().equals(MessageType.ACK)) {
							gui.addChatMessage(newMessage.getType() + "\n");
						}
						if (newMessage.getType().equals(MessageType.CHAT)) {
							handleChatMessage((ChatMessage) newMessage);
						}
						if (newMessage.getType().equals(MessageType.CARD)) {
							handlecard((CardMessage) newMessage);
						}
						if (newMessage.getType().equals(MessageType.GAME_STATE)) {
							handleGameState((GameStateMessage) newMessage);
						}
						if(newMessage.getType().equals(MessageType.GAME_ACTION)) {
							handleGameAction((GameActionMessage)newMessage);
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

		private void handleGameAction(GameActionMessage actionMessage) {
			
			
		}

		private void handleGameState(GameStateMessage gameStateMessage) {
			if (GameAction.JOIN.equals(gameStateMessage.getRequestedState())) {
				gui.addChatMessage("To join game click join");
			} else if (GameAction.START.equals(gameStateMessage.getRequestedState())) {
				gui.addChatMessage("Game Started");
			}

		}

		private void handleChatMessage(ChatMessage chatMessage) {
			if (chatMessage.getUsername() != null) {
				gui.addChatMessage(chatMessage.getUsername() + ": " + chatMessage.getText());
			} else {
				gui.addChatMessage(name + ": " + chatMessage.getText());
			}
		}
	}

	public void handlecard(CardMessage cardMessage) {
		if (cardMessage.getUsername() != null) {
			gui.addChatMessage(cardMessage.getUsername() + ": " + cardMessage.getCard().getSuite() + " "
					+ cardMessage.getCard().getSuite());
		} else {
			gui.addChatMessage(name + ": " + cardMessage.getCard().getSuite() + " " + cardMessage.getCard().getSuite());
		}

	}
}
