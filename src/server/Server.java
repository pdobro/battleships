

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {

	public static int uniqueId;
	private final int port;
	private final SimpleDateFormat date;
	private ArrayList<GameThread> clients;
	private static ArrayList<GameThread> waitingRoom;
	private boolean keepGoing;
	private InetAddress ip;

	public Server(int port){
		this.port = port;
		this.date = new SimpleDateFormat("HH:mm:ss");
		this.clients = new ArrayList<>();
		waitingRoom = new ArrayList<>();
		try {
			this.ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		int portNumber = 1050;
		switch (args.length) {
		case 1:
			try {
				portNumber = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Invalid port number.");
				System.out.println("Usage is: > java Server [portNumber]");
				return;
			}
		case 0:
			break;
		default:
			System.out.println("Usage is: > java Server [portNumber]");
			return;
		}

		Server server = new Server(portNumber);
		server.start(server);
	}

	public void start(Server server) {
		keepGoing = true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (keepGoing) {
				display("Server waiting for Clients on" + ip + ":" + port + ".");
				Socket socket = serverSocket.accept();
				if (!keepGoing)
					break;
				GameThread pt = new GameThread(socket);
				pt.start();
				if(pt.isInGame())
					return;
			} // stop
			try {
				serverSocket.close();
			} catch (Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
			String msg = date.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}



	private void display(String msg) {
		String time = date.format(new Date()) + " " + msg;
		System.out.println(time);
	}

	public ArrayList<GameThread> getClients() {
		return clients;
	}

	public void setClients(ArrayList<GameThread> clients) {
		this.clients = clients;
	}

	public static ArrayList<GameThread> getWaitingRoom() {
		return waitingRoom;
	}

	public void setWaitingRoom(ArrayList<GameThread> waitingRoom) {
		Server.waitingRoom = waitingRoom;
	}
}

