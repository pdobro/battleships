package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import command.Message;

public class GameThread extends Thread {

	private Socket socket;
	private ObjectOutputStream Output;
	private ObjectInputStream Input;

	private int uniqueid; // unique id easier to remove
	private boolean inGame = false;
	private String pseudo;
	private GameThread opponentPairThread;
	private boolean isItYourTurn;

	private Message message;

	public GameThread(Socket socket) {
		this.setSocket(socket);
		this.setUniqueid(Server.uniqueId++);
		try {
			Output = new ObjectOutputStream(socket.getOutputStream());
			Input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (!inGame) {
			try {
				message = (Message) Input.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("Nie znaleziono wiadmosci" + e);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Błąd");
				e.printStackTrace();
			}
			switch (message.getType()) {
			case Message.ADD:
				addPlayer(message.getMsg());
				System.out.println(isItYourTurn);
				sendMessage(new Message(Message.ACCEPTADD, isItYourTurn));
				break;
			case Message.OPPONENT:
				if (opponentPairThread != null) {
					sendMessage(new Message(Message.ACCEPTOPPONENT, opponentPairThread.getPseudo()));
				} else {
					sendMessage(new Message(Message.FULLOPPONENT, "Prosze czekac..."));
				}
				break;
			case Message.SHOOT:
				System.out.println(this.pseudo+" strzela w "+ opponentPairThread.pseudo);
				opponentPairThread.sendMessage(new Message(Message.SHOOT, message.getPosX(), message.getPosY()));
				break;
			case Message.HIT:
				System.out.println("Trafiony");
				opponentPairThread.sendMessage(new Message(Message.HIT, message.getPosX(), message.getPosY()));
				isItYourTurn = true;
				break;
			case Message.MISS:
				System.out.println("Pudlo");
				opponentPairThread.sendMessage(new Message(Message.MISS, message.getPosX(), message.getPosY()));
				isItYourTurn = true;
				break;
			case Message.WIN:
				System.out.println("Koniec gry");
				opponentPairThread.sendMessage(new Message(Message.WIN, message.getPosX(), message.getPosY()));
				isItYourTurn = false;
				inGame = false;
				break;
			}
		}
	}

	public void addPlayer(String s) {
		if (Server.getWaitingRoom().size() == 1) {
			opponentPairThread = Server.getWaitingRoom().get(0);
			opponentPairThread.setOpponentPairThread(this);
			pseudo = s;
			Server.getWaitingRoom().clear();
			isItYourTurn = false;
		} else {
			Server.getWaitingRoom().add(this);
			pseudo = s;
			isItYourTurn = true;
		}
	}

	private void display(String msg) {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
		String time = date.format(new Date()) + " " + msg;
		System.out.println(time);
	}

	/*
	 * To send a message to the server
	 */
	public void sendMessage(Message msg) {
		try {
			Output.writeObject(msg);
			Output.flush();
		} catch (IOException e) {
			inGame = false;
			display("Exception writing to server: " + e);
		}
	}




	public void setUniqueid(int uniqueid) {
		this.uniqueid = uniqueid;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public GameThread getOpponentPairThread() {
		return opponentPairThread;
	}

	public void setOpponentPairThread(GameThread opponentPairThread) {
		this.opponentPairThread = opponentPairThread;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public boolean isInGame() {
		return inGame;
	}
}
