import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Client {

	private Socket s = null;
	private String name;
	private Boolean connected = false;

	public Client(String n, Socket s) {
		try {
			name = n;
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.write(name + "\n");
			writer.flush();
			String responseString = reader.readLine();
			if (responseString.equals("ACK")) {
				connected = true;
				new Thread(new InputHandler(s)).start();
			} else if (responseString.equals("Decline")) {
				//chatOutput.append("User already connected\n");
				close();
			}
		} catch (IOException e) {
			//chatOutput.append("Unable to connect to server\n");
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
