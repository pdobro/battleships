package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

import client.Player;
import model.BoardPlayer;
import model.Ship;
import view.GamePanel;

public class GameController implements MouseListener {

	private final GamePanel view;
	private final BoardPlayer boardPlayer;
	private final Player player;

	private int size = 0;
	private  int shipsToPlace = 5;
	private final Ship[] listship = {new Ship(5, true), new Ship(4, true), new Ship(3, true), new Ship(2, true), new Ship(1, true)};
	private boolean isGameSet;

	public GameController(BoardPlayer boardPlayer, Player player) {

		this.boardPlayer = boardPlayer;
		this.player = player;
		this.view = new GamePanel(this);
	}

	public void shipsPlacement(JTextArea console, int col, int row, boolean vertical) {

			if(!vertical) 
				listship[size].setVertical(false);
		
			if (shipsToPlace > 0 && boardPlayer.canPlaceShip(listship[size], col, row) && vertical) {
				boardPlayer.placeShip(listship[size], col, row);
				for(int i = 5; i > size; i-- ) {
					Component c = view.getBoardPlayer().getComponentAt(col * 35, (row + (5 - i)) * 35);
					c.setBackground(Color.blue);
				}
				size++;
				shipsToPlace--;
				console.setText("Umieść statek o rozmiarze " + (shipsToPlace));
		} else if (shipsToPlace > 0 && boardPlayer.canPlaceShip(listship[size], col, row)) {
				boardPlayer.placeShip(listship[size], col, row);
			for(int i = 5; i > size; i-- ) {
				Component c = view.getBoardPlayer().getComponentAt((col + (5 - i)) * 35, row   * 35);
				c.setBackground(Color.blue);
			}
				size++;
				shipsToPlace--;
				console.setText("Umieść statek o rozmiarze " + (shipsToPlace));
		}

		if (size == 5) {
			console.setText("Ustaw parametry sieci i połącz się z serwerem");
			isGameSet = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent a) {
		for (Component c : view.getBoardPlayer().getComponents()) {
			if (a.getSource() == c) {
				int posX = c.getX() / 35 ;
				int posY = c.getY() / 35;
				shipsPlacement(view.getConsole(), posX, posY, a.getButton() == MouseEvent.BUTTON1);
			}
		}

		if (a.getSource() == view.getButtonConnect() && isGameSet) {
			player.setNick(view.getFieldNickname().getText());
			player.setPort(getPort());
			player.setAddress(view.getFieldIP().getText());
			player.start(this);
		}

		for (Component c : view.getBoardOpponent().getComponents()) {
			if (a.getSource() == c && player.isOpponentFind() && Player.isItYourTurn()) {
				int posX = c.getX() / 35;
				int posY = c.getY() / 35;
				player.sendHit(posX, posY);

			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	public boolean checkIfHit(int col, int row) {

		return boardPlayer.shoot(col, row);
	}

	public boolean checkGameEnd(){
		int destroyedShips = 0;
		for (int i = 0; i < 5; i++){
			if(!listship[i].isAlive())
				destroyedShips++;
		}
		return destroyedShips == 5;
	}



	public void activeFrame() {
		this.view.getFrame().requestFocus();
		this.view.getFrame().revalidate();
		this.view.getFrame().repaint();
	}

	public void updateOpponentHit(int col, int row){
		Component c = view.getBoardPlayer().getComponentAt(col * 35, row * 35);
		c.setBackground(Color.RED);

	}
	
	public void updateOpponentMiss(int col, int row){
		Component c = view.getBoardPlayer().getComponentAt(col * 35, row * 35);
		c.setBackground(Color.GRAY);
	}
	
	public void updatePlayerHit(int col, int row){
		Component c = view.getBoardOpponent().getComponentAt(col * 35, row * 35);
		c.setBackground(Color.RED);

	}
	
	public void updatePlayerMiss(int col, int row){
		Component c = view.getBoardOpponent().getComponentAt(col * 35, row * 35);
		c.setBackground(Color.GRAY);
	}
	
	public int getPort() {
		return Integer.parseInt(view.getFieldPort().getText());
	}

	public boolean isGameSet() {
		return isGameSet;
	}

	public void setGameSet(boolean isGameSet) {
		this.isGameSet = isGameSet;
	}

	public String getPseudo() {
		return view.getFieldNickname().getText();
	}

	public void displayText(String msg) {
		this.view.getConsole().setText(msg);
	}
}
