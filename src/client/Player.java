package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.GameController;
import command.Message;
import model.BoardPlayer;

public class Player {

	private String nick;
	private String serverAddress;
	private int port;

	private Socket socket;
	private ObjectInputStream Input; // to read from the socket
	private ObjectOutputStream Output; // to write on the socket
	private Message message;

	private boolean partnerAwait = false;
	private boolean opponentFind = false;
	private static boolean isItYourTurn;

	public Player(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public static void main(String[] args) {
		Player p = new Player("localhost");
		BoardPlayer bp = new BoardPlayer();
		GameController c = new GameController(bp, p);
	}


	public void start(GameController game) {

		// try to connect to the server
		try {
			socket = new Socket(serverAddress, port);
		}
		catch (Exception ec) {
			display(game, "Błąd połączenia z serwerem" + ec);
		}
		String msg = "połączono z " + socket.getInetAddress() + ":" + socket.getPort();
		display(game, msg);


		try {
			Input = new ObjectInputStream(socket.getInputStream());
			Output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!partnerAwait) {
			sendMessage(new Message(Message.ADD,  game.getPseudo()));
				try {
					message = (Message) Input.readObject();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			if (message.getType() == Message.ACCEPTADD) {
				if (message.isBool()) {
					partnerAwait = true;
					isItYourTurn = true;
				} else {
					partnerAwait = true;
					isItYourTurn = false;
				}
			}
			}
		game.activeFrame();
		while (!opponentFind) {
			sendMessage(new Message(Message.OPPONENT, game.getPseudo()));
			try {
				message = (Message) Input.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			switch (message.getType()) {
			case Message.ACCEPTOPPONENT:
				if (isItYourTurn) {
					display(game, "Grasz z " + message.getMsg() + "\nTwoj ruch");
				} else {
					display(game, "Grasz z " + message.getMsg() + "\nRuch przeciwnika");
				}
				opponentFind = true;
				break;
			case Message.FULLOPPONENT:
				display(game, message.getMsg());
				break;
			}
		}
		game.activeFrame();
		new PlayerThread(socket, Input, Output, game, isItYourTurn).start();
	}


	private void display(GameController c, String msg) {
		c.displayText(msg);
	}


	public void sendMessage(Message msg) {
		try {
			Output.writeObject(msg);
			Output.flush();
		} catch (IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}

	public void sendHit(int posX, int posY) {
		sendMessage(new Message(Message.SHOOT, posX, posY));
		isItYourTurn = false;
	}



	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getAddress() {
		return serverAddress;
	}

	public void setAddress(String ip) {
		this.serverAddress = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isPartnerAwait() {
		return partnerAwait;
	}

	public void setPartnerAwait(boolean partnerAwait) {
		this.partnerAwait = partnerAwait;
	}

	public boolean isOpponentFind() {
		return opponentFind;
	}

	public void setOpponentFind(boolean opponentFind) {
		this.opponentFind = opponentFind;
	}

	public static boolean isItYourTurn() {
		return isItYourTurn;
	}

	public static void setItYourTurn(boolean isItYourTurn) {
		Player.isItYourTurn = isItYourTurn;
	}
}
