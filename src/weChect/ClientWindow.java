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

public class ClientWindow {

	private JFrame client;
	TestClient c;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.client.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		client = new JFrame("Client");
	
		client.setBounds(100, 100, 450, 300);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea textRec = new JTextArea();
		client.getContentPane().add(textRec, BorderLayout.CENTER);
		
		JTextArea textSend = new JTextArea();
		client.getContentPane().add(textSend, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.send();
			}
		});
		client.getContentPane().add(btnNewButton, BorderLayout.WEST);
		
		JButton btnConnect = new JButton("connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.connect();
			}
		});
		client.getContentPane().add(btnConnect, BorderLayout.EAST);
		c=new TestClient(textRec,textSend);
		
		JButton btnClose = new JButton("closeStream");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			c.closeStream();
			}
		});
		client.getContentPane().add(btnClose, BorderLayout.NORTH);
		
	}
	public static void timeRush(){
		timeRush(1000/10);
	}
	public static void timeRush(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
