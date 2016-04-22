package socket;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class TheClient implements Runnable {
	// A socket and port representing a communication
	private Socket socket;
	private static final int PORT = 9999;

	// A means to reading from and writing to the socket 
	private PrintWriter writer;
	private BufferedReader reader;

	// A thread because reader.readLine() is blocking
	private Thread runningThread;
	private boolean running;
	
	private Scanner in;

	public static void main(String args[]) {
		TheClient client = new TheClient();
		client.init();
	}

	
	public void init() {
		try {
			socket = new Socket("127.0.0.1", PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			running = true; // This is actually not necessary

			runningThread = new Thread(this);
			runningThread.start();
			
			in = new Scanner(System.in);
			String message;
			System.out.println("New message or just enter to quit: ");
			while ((message = in.nextLine()).length() > 0) {
				sendMessage(message); 
				System.out.println("Type new message or just enter to quit: ");
			}

			in.close();
			running = false; // This is unnecessary since reader.readLine() blocks; close the socket instead.
			socket.close(); // This will make reader.readLine() in the run method to fail, and thus kill the thread!
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			socket = null;
			clearup();
			System.out.println("Bye, bye");
		}
	}

	public void sendMessage(String message) {
		if (running) {
			System.out.println("Sending the message: " + message);
			writer.println(message);
			writer.flush();
		}
	}

	public void run() {
		try {
			String message = "";
			while ((message = reader.readLine()) != null && running) {
				System.out.println("Server says: " + message);
			}
		} 
		catch (Exception e) {
			running = false;
			System.out.println("Thread stopping");
		}
	}

	public void clearup() {
		running = false;
		if (runningThread != null) {
			runningThread.interrupt();
		}
		runningThread = null;
		System.out.println("Disconnected");

		try {
			reader.close();
		}
		catch (Exception e) {}
		reader = null;

		try {
			writer.close();
		}
		catch (Exception e) {}
		writer = null;
	}
}