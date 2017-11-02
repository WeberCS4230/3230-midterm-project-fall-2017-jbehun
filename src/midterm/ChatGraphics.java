package midterm;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;


public class ChatGraphics extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 800;
	private JTextArea inputText, outputText;
	private final Font GUI_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);;
	private Socket socket;

	public ChatGraphics(Socket s) {
		super("Chat Window");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(400, 50, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		socket = s;
		intializeComponents();
	}

	//void connectClientToSever() {
	//	String hostName = JOptionPane.showInputDialog(null, "Please input host to connect to");
	//	String userName = JOptionPane.showInputDialog(null, "Please input a user name");
	//	outputText.append("Attempting to connect to server\n");
	//	client = new Client(userName, hostName, outputText);
	//	if (!client.isConnected()) {
	//		outputText.append("Starting a server\n");
	//		new Thread(new Server()).start();
	//		outputText.append("Server started\n");
	//		client = new Client(userName, hostName, outputText);
	//	}
	//}

	private void intializeComponents() {
		JPanel chatPanel, mainPanel, gameControlPanel;
		JScrollPane scrollPane;
		JButton send;
		
		chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.setPreferredSize(new Dimension(500,800));

		outputText = new JTextArea();
		outputText.setEditable(false);
		outputText.setAlignmentX(Component.CENTER_ALIGNMENT);
		outputText.setMinimumSize(new Dimension(600, 250));
		outputText.setBorder(new EmptyBorder(0, 10, 0, 0));
		outputText.setForeground(Color.BLUE);
		outputText.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret) outputText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane = new JScrollPane(outputText);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setMinimumSize(new Dimension(600, 250));
		chatPanel.add(scrollPane);

		inputText = new JTextArea();
		inputText.setMinimumSize(new Dimension(800, 150));
		inputText.setPreferredSize(new Dimension(800, 150));
		inputText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		inputText.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
				BorderFactory.createEmptyBorder(5, 10, 0, 0)));
		inputText.requestFocus();
		inputText.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatPanel.add(inputText);

		Action ctrlEnter = new SendInputAction("Send");

		send = new JButton(ctrlEnter);
		send.setMinimumSize(new Dimension(1000, 100));
		send.setPreferredSize(new Dimension(1000, 100));
		send.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		send.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatPanel.add(send);

		InputMap imap = chatPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke("ctrl ENTER"), "addText");
		ActionMap amap = chatPanel.getActionMap();
		amap.put("addText", ctrlEnter);

		outputText.setFont(GUI_FONT);
		inputText.setFont(GUI_FONT);
		send.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		
		gameControlPanel = new JPanel();
		gameControlPanel.setLayout(new BoxLayout(gameControlPanel, BoxLayout.Y_AXIS));
		
		JButton startGame, hit, stay;
		startGame = new JButton("Start Game");
		startGame.setMinimumSize(new Dimension(1000, 100));
		startGame.setPreferredSize(new Dimension(1000, 100));
		startGame.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		startGame.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gameControlPanel.add(startGame);
		
		hit = new JButton("Hit");
		hit.setMinimumSize(new Dimension(1000, 100));
		hit.setPreferredSize(new Dimension(1000, 100));
		hit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		hit.setAlignmentX(Component.CENTER_ALIGNMENT);
		hit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gameControlPanel.add(hit);
		
		stay = new JButton("Stay");
		stay.setMinimumSize(new Dimension(1000, 100));
		stay.setPreferredSize(new Dimension(1000, 100));
		stay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		stay.setAlignmentX(Component.CENTER_ALIGNMENT);
		stay.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		gameControlPanel.add(stay);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(chatPanel);
		mainPanel.add(gameControlPanel);

		this.add(mainPanel);
		this.setVisible(true);
	}

	private void sendMessageToClient(String input) {
		//client.newMessage(input);
		inputText.setText("");
	}

	public void addPreviousChat(String chat) {
		outputText.append(chat);
		inputText.requestFocus();
	}

	private class SendInputAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SendInputAction(String name) {
			putValue(Action.NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessageToClient(inputText.getText() + "\n");
			inputText.requestFocus();
		}

	}

}
