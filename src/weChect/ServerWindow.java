package weChect;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerWindow {

	private JFrame frame;
	Server s;
	static JTextArea textRec;
	static JTextArea textSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Server");
		frame.setBounds(700, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textRec = new JTextArea();
		frame.getContentPane().add(textRec, BorderLayout.CENTER);

		textRec.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				s.sendAll("d"+e.getKeyChar());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				s.sendAll("u"+e.getKeyChar());
			}
		});
		
		
		textSend = new JTextArea();
		frame.getContentPane().add(textSend, BorderLayout.SOUTH);

		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.sendAll();
			}
		});
		frame.getContentPane().add(btnSend, BorderLayout.WEST);

		JButton btnConnect = new JButton("Create connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.connect();
			}
		});
		frame.getContentPane().add(btnConnect, BorderLayout.EAST);
		s = new Server(textRec, textSend);

		JButton btnClosestream = new JButton("closeStream");
		btnClosestream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.closeStream();
			}
		});
		frame.getContentPane().add(btnClosestream, BorderLayout.NORTH);
	}

}
