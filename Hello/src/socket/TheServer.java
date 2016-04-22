package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.net.ServerSocket;
import java.net.Socket;

public class TheServer implements Runnable {
	private boolean started;
	private boolean running;
	
	// This is where the server listens to new connections
	private ServerSocket serverSocket;
    private int sessionCounter = 0;
    private List<SessionHandler> sessions;
	private Thread serverThread;

	private static final int PORT = 9999;

	public static void main(String args[]) {
		TheServer server = new TheServer();
		server.start();
	}

	public TheServer() {
		started = false;
		serverSocket = null;
	}

	public void start() {
		if (!started) {
			started = true;

			try {
				serverSocket = new ServerSocket(PORT);
				sessions = new ArrayList<SessionHandler>();
				running = true;

				serverThread = new Thread(this);
				serverThread.start();

				System.out.println("Server started!\n");

			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void stop() {
		running = false;
		started = false;

		if (serverThread != null) {
			serverThread.interrupt();
		}
		serverThread = null;
	}

	public void run() {
		try {
			while (running) {
				try {
					Socket client = serverSocket.accept();

					SessionHandler handler = new SessionHandler(client, sessionCounter++);
					sessions.add(handler);
					
					handler.sendMessage("New client " + handler.getID() + " accepted.");
					System.out.println("No. client so far: " + sessions.size());
					
					for (SessionHandler h: sessions) {
						try {
							if (h != handler) {
								h.sendMessage("New client arrived");
							}
						}
						catch(Exception e) {}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class SessionHandler implements Runnable {
		private Socket socket;
	    private int sessionID;

		private PrintWriter writer;
		private BufferedReader reader;

		private Thread runningThread;
		private boolean running;

		public SessionHandler(Socket socket, int sessionID) {
			this.socket = socket;
			this.sessionID = sessionID;

			try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				running = true;

				runningThread = new Thread(this);
				runningThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				disconnect();
			}
		}

		public int getID() {
			return sessionID;
		}

		public void run() {
			try {
				String message = "";
				while ((message = reader.readLine()) != null && running) {
					System.out.println("Client: " + sessionID + " says: "+ message);
					sendMessage(message.toUpperCase() + " too.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				disconnect();
			}
		}
		
		public void sendMessage(String message) {
			if (running) {
				writer.println(message);
				writer.flush();
			}
		}

		public void disconnect() {
			running = false;
			if (runningThread != null) {
				runningThread.interrupt();
			}
			runningThread = null;

			try {
				reader.close();
			} 
			catch (Exception e) {}
			finally { reader = null; }

			try {
				writer.close();
			} 
			catch (Exception e) {}
			finally { writer = null; }
			
			try {
				socket.close();
			} 
			catch (Exception e) {}
			finally { socket = null; }
		}
	}
}