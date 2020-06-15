package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.GameController;

public class GamePanel {

	private GameController controller;
	private JFrame frame;
	private final int WINDOW_WIDTH = 1000, WINDOWS_HEIGHT = 600;
	private JTextField fieldIP;
	private JTextField fieldPort;
	private JPanel panel;
	private JTextField fieldNickname;
	private JTextArea console;
	private JPanel boardOpponent;
	private JPanel boardPlayer;
	private JButton buttonConnect;

	public GamePanel(GameController controller) {
		this.controller = controller;
		buildFrame();
	}

	private void buildFrame() {

		frame = new JFrame();

		// Centering the window and choosing the size of the window
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOWS_HEIGHT));
		int windowLeftPosition = screenSize.width / 2 - WINDOW_WIDTH / 2;
		int windowRightPosition = screenSize.height / 2 - WINDOWS_HEIGHT / 2;
		frame.setLocation(windowLeftPosition, windowRightPosition);
		frame.setResizable(false);

		// Prevents the window from being resized
		frame.setTitle("Statki");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png")));

		// Saving the EXIT_ON_CLOSE option when closing the window (stopping the
		// process)
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Add an absolute layout
		frame.getContentPane().setLayout(null);



		// Add a panel that will contain everything
		panel = new JPanel();
		panel.setBounds(0, 0, 994, 571);
		panel.setLayout(null);
		frame.setContentPane(panel);

		// Add a Label for the ip TextField
		JLabel lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Courier New", Font.BOLD, 14));
		lblIp.setBounds(550, 35, 27, 27);
		panel.add(lblIp);

		fieldIP = new JTextField("localhost");
		fieldIP.setColumns(10);
		fieldIP.setBounds(610, 37, 146, 27);
		panel.add(fieldIP);

		// Add a Label for the port TextField
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Courier New", Font.PLAIN, 14));
		lblPort.setBounds(550, 76, 40, 21);
		panel.add(lblPort);

		fieldPort = new JTextField("1050");
		fieldPort.setBounds(610, 75, 146, 27);
		panel.add(fieldPort);
		fieldPort.setColumns(10);

		// Add a Label for the pseudo TextField
		JLabel lblPseudo = new JLabel("Nick:");
		lblPseudo.setFont(new Font("Courier New", Font.PLAIN, 14));
		lblPseudo.setBounds(550, 120, 63, 20);
		panel.add(lblPseudo);

		// Add a TextField to set the pseudo
		fieldNickname = new JTextField("gracz");
		fieldNickname.setColumns(10);
		fieldNickname.setBounds(610, 113, 146, 27);
		panel.add(fieldNickname);


		buttonConnect = new JButton("Połącz");
		buttonConnect.setBounds(830, 66, 123, 38);
		buttonConnect.addMouseListener(controller);
		panel.add(buttonConnect);

		// Drawing of the left grid
		boardPlayer = new JPanel();
		boardPlayer.setBounds(50, 180, 350, 350);
		boardPlayer.setLayout(new GridLayout(10, 10));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton b = new JButton();
				b.setBackground(Color.white);
				b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				b.setCursor(new Cursor(Cursor.HAND_CURSOR));
				b.setPreferredSize(new java.awt.Dimension(35, 35));
				b.addMouseListener(controller);
				boardPlayer.add(b);
			}
		}
		panel.add(boardPlayer);

		// Drawing of the right grid
		boardOpponent = new JPanel();
		boardOpponent.setBounds(550, 180, 350, 350);
		boardOpponent.setLayout(new GridLayout(10, 10));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton b = new JButton();
				b.setBackground(Color.white);
				b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				b.setCursor(new Cursor(Cursor.HAND_CURSOR));
				b.setPreferredSize(new java.awt.Dimension(35, 35));
				b.addMouseListener(controller);
				boardOpponent.add(b);
			}
		}
		panel.add(boardOpponent);

		this.console = new JTextArea();
		this.console.setBounds(50, 40, 350, 60);
		this.console.setEditable(false);
		console.setText(" Umieść statek o rozmiarze 5!\n By umieścić statek horyzontalnie, uzżj prawego przycisku myszy.");
		panel.add(this.console);

		// Adding a Background
		JLabel lblFond = new JLabel("fond");
		lblFond.setIcon(new ImageIcon(getClass().getResource("/images/background.jpg")));
		lblFond.setBounds(0, 0, 994, 571);
		panel.add(lblFond);

		// Packing and displaying the JFrame
		frame.pack();
		frame.setVisible(true);
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}

	public JPanel getBoardOpponent() {
		return boardOpponent;
	}

	public void setBoardOpponent(JPanel boardOpponent) {
		this.boardOpponent = boardOpponent;
	}

	public JPanel getBoardPlayer() {
		return boardPlayer;
	}

	public void setBoardPlayer(JPanel boardPlayer) {
		this.boardPlayer = boardPlayer;
	}

	public JTextField getFieldNickname() {
		return fieldNickname;
	}

	public void setFieldNickname(JTextField fieldNickname) {
		this.fieldNickname = fieldNickname;
	}

	public JTextField getFieldIP() {
		return fieldIP;
	}

	public void setFieldIP(JTextField fieldIP) {
		this.fieldIP = fieldIP;
	}

	public JTextField getFieldPort() {
		return fieldPort;
	}

	public void setFieldPort(JTextField fieldPort) {
		this.fieldPort = fieldPort;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextArea getConsole() {
		return console;
	}

	public void setConsole(JTextArea console) {
		this.console = console;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JButton getButtonConnect() {
		return buttonConnect;
	}

	public void setButtonConnect(JButton buttonConnect) {
		this.buttonConnect = buttonConnect;
	}
}
