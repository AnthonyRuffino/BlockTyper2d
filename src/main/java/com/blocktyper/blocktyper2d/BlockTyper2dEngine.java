package com.blocktyper.blocktyper2d;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.blocktyper.blocktyper2d.keylisteners.FullScreenListener;

public class BlockTyper2dEngine extends JPanel implements KeyListener, MouseListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 201611041902L;

	private static final String NEWLINE = System.getProperty("line.separator");
	private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(450, 450);

	private static GraphicsDevice DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	private static final boolean FULLSCREEN_SUPPORTED = DEVICE.isFullScreenSupported();

	// BEGIN PRIVATE MEMBER VARIABLES
	private String nameOfGame = "BlockTyper2dGame";
	private JTextArea textArea;
	private FullScreenListener fullScreenListener;
	private int fullScreenFontSize = 18;
	private int minFontSize = 7;
	private double fullScreenTargetWidth = 1880.0;
	private GameCanvas gameCanvas;
	// END PRIVATE MEMBER VARIABLES

	int x;
	int y;

	int dx = 5;
	int dy = 5;

	public BlockTyper2dEngine(String nameOfGame) {
		super(new BorderLayout());
		this.nameOfGame = nameOfGame;
	}

	public void startEngine() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				startUI();
			}
		});
	}

	private void startUI() {

		JFrame frame = new JFrame(nameOfGame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// enable full screen
		if (FULLSCREEN_SUPPORTED) {
			System.out.println("Fullscreen supported");
			fullScreenListener = new FullScreenListener(KeyEvent.VK_F11, frame, DEVICE);
			addKeyListener(fullScreenListener);
		} else {
			System.out.println("Fullscreen not supported");
		}

		// configure panel
		setOpaque(true);
		setPreferredSize(DEFAULT_WINDOW_SIZE);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// add this game to the current frame and show
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);

		// add canvas
		gameCanvas = new GameCanvas();
		gameCanvas.addMouseListener(this);
		add(gameCanvas);

		// set up chat area
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.addMouseListener(this);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, 75));
		add(scrollPane, BorderLayout.SOUTH);

		// get focus
		setFocusable(true);
		requestFocusInWindow();

		// configure input listeners
		addMouseListener(this);
		addKeyListener(this);

		Timer timer = new Timer(16, this);
		timer.start();

	}

	void eventOutput(String eventDescription, MouseEvent e) {
		textArea.append(eventDescription + " detected on " + e.getComponent().getClass().getName() + "." + NEWLINE);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public void mousePressed(MouseEvent e) {
		eventOutput("Mouse pressed (# of clicks: " + e.getClickCount() + ")", e);
		requestFocusInWindow();
		System.out.println("Focus set");
	}

	public void mouseReleased(MouseEvent e) {
		eventOutput("Mouse released (# of clicks: " + e.getClickCount() + ")", e);
	}

	public void mouseClicked(MouseEvent e) {
		requestFocusInWindow();
		eventOutput("Mouse clicked (# of clicks: " + e.getClickCount() + ")", e);

		dy = -dy;
		dx = -dx;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
	}

	class GameCanvas extends Canvas {

		/**
		 * 
		 */
		private static final long serialVersionUID = 201611041902L;

		@Override
		public void paint(Graphics g) {
			Double fontSize = fullScreenFontSize * (this.getWidth() / fullScreenTargetWidth);
			fontSize = fontSize < minFontSize ? minFontSize : fontSize;

			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize.intValue()));
			g.drawString("Test String", 10, 20);
			g.drawString(fullScreenListener != null && fullScreenListener.isFullScreen() ? "Fullscreen mode!"
					: "Windowed mode", 10, 40);
			g.drawString((new Date()).getTime() + "", 10, 60);

			g.drawString("fontSize: " + fontSize, 10, 80);

			g.drawString("this.getWidth(): " + this.getWidth(), 10, 100);

			g.drawOval(x, y, 50, 50);

			x += dx;
			y += dy;

			if (x > gameCanvas.getWidth() - 50 && dx > 0) {
				dx = -dx;
			}
			if (y > gameCanvas.getHeight() - 50 && dy > 0) {
				dy = -dy;
			}

			if (x < 0 && dx < 0) {
				dx = -dx;
			}
			if (y < 0 && dy < 0) {
				dy = -dy;
			}

			gameCanvas.getToolkit().sync();

		}
	}

	public void keyPressed(KeyEvent e) {
		keyEventOutput("keyPressed", e);
	}

	public void keyReleased(KeyEvent e) {
		keyEventOutput("keyReleased", e);
	}

	public void keyTyped(KeyEvent e) {
		keyEventOutput("keyTyped", e);
	}

	void keyEventOutput(String eventDescription, KeyEvent e) {
		KeyStroke stroke = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
		textArea.append(eventDescription + "[" + stroke + "] detected on " + e.getComponent().getClass().getName() + "."
				+ NEWLINE);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public void actionPerformed(ActionEvent arg0) {
		gameCanvas.repaint();

	}

}