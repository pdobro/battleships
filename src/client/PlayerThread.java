package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.GameController;
import command.Message;

public class PlayerThread extends Thread {

	private final Socket socket;
	private final ObjectInputStream Input; // to read from the socket
	private final ObjectOutputStream Output; // to  write on the socket
	private Message message;

	private boolean isDead = false;
	private final GameController game;
	private boolean isItYourTurn;

	public PlayerThread(Socket socket, ObjectInputStream Input, ObjectOutputStream Output, GameController game,
			boolean isItYourTurn) {
		this.socket = socket;
		this.Input = Input;
		this.Output = Output;
		this.game = game;
		this.setItYourTurn(isItYourTurn);
	}

	public void run() {
		System.out.println("client " + socket.getLocalAddress() + " polaczenie");
		while (!isDead) {
			try {
				message = (Message) Input.readObject();
			} catch (ClassNotFoundException | IOException e) {
				//koiec gry
				e.printStackTrace();
			}
			switch (message.getType()) {
			case Message.SHOOT:
				String strAsciiTab = Character.toString((char) (message.getPosX() + 65));
				game.displayText("Strzal  "  + message.getPosY());
				boolean hitted = game.checkIfHit(message.getPosX(), message.getPosY());
				if (hitted) {
					game.updateOpponentHit(message.getPosX(), message.getPosY());
					if (game.checkGameEnd()) {
						sendMessage(new Message(Message.WIN, message.getPosX(), message.getPosY()));
						game.displayText("Koniec gry (przegrana)");
						isDead=true;
						break;						
					} else {
						sendHitted(message.getPosX(), message.getPosY());
						game.displayText("Strzał  " + message.getPosY() + "\nDodatkowy ruch przeciwnika");
					}
				} else {
					Player.setItYourTurn(true);
					game.displayText("Strzal  w "+  message.getPosY() + "\nTwoj ruch");
					sendMissed(message.getPosX(), message.getPosY());
					game.updateOpponentMiss(message.getPosX(), message.getPosY());
				}
				break;
			case Message.MISS:
				game.updatePlayerMiss(message.getPosX(), message.getPosY());
				System.out.println("Pudło");
				game.displayText("Spudłowałeś");
				game.activeFrame();
				break;
			case Message.HIT:
				Player.setItYourTurn(true);
				game.updatePlayerHit(message.getPosX(), message.getPosY());
				System.out.println("Trafiony");
				game.displayText("Trafiłeś, masz dodatkowy ruch");
				game.activeFrame();
				break;
			case Message.WIN:
				game.updatePlayerHit(message.getPosX(), message.getPosY());
				System.out.println("Trafiony zatopiony, koniec gry");
				game.displayText("WYGRANA!");
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//end game
				isDead = true;
				break;
			}
		}
	}

	/*
	 * To send a message to the server
	 */
	public void sendMessage(Message msg) {
		try {
			Output.writeObject(msg);
		} catch (IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}


	private void sendHitted(int posX, int posY) {
		sendMessage(new Message(Message.HIT, posX, posY));
		setItYourTurn(false);
	}

	private void sendMissed(int posX, int posY) {
		sendMessage(new Message(Message.MISS, posX, posY));
		setItYourTurn(false);
	}

	public boolean isItYourTurn() {
		return isItYourTurn;
	}

	public void setItYourTurn(boolean isItYourTurn) {
		this.isItYourTurn = isItYourTurn;
	}

}
