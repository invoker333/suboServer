package fileSystem;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.io.*;
import java.nio.channels.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JPasswordField;

import java.awt.Canvas;

public class CopyTest {

	private JFrame frame;
	private File a;
	private File b;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CopyTest window = new CopyTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CopyTest() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JFileChooser fc = new JFileChooser();
		JButton button = new JButton(
				"\u9009\u62E9\u8981\u590D\u5236\u7684\u6587\u4EF6");

		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				fc.showOpenDialog(frame);
				a = fc.getSelectedFile();
			}
		});
		frame.getContentPane().add(button, BorderLayout.WEST);

		JButton button_1 = new JButton("\u9009\u62E9\u76EE\u7684\u5730");
		button_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				fc.showSaveDialog(frame);
				b = fc.getSelectedFile();
			}
		});
		frame.getContentPane().add(button_1, BorderLayout.EAST);

		JButton button_2 = new JButton("执行");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extracted();
			}
		});
		frame.getContentPane().add(button_2, BorderLayout.CENTER);

	}

	private void extracted() {
		ChanelCopyer.copy(a, b);
	}

}
