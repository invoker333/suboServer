package weChect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

import mySql.Log;
import mySql.SelectQuery;

public class Server implements Runnable{
	ServerSocket ss;
	private ArrayList<ServerXian> socketList = new ArrayList<>();
	private JTextArea taRec;
	private JTextArea taSend;
	private boolean connected;
	SelectQuery sql=new SelectQuery();

	public static void main(String[] args) {
		 Server s = new Server(null, null);
		 s.connect();
	}
	
	Server(JTextArea taRec, JTextArea taSend) {
		this.taRec = taRec;
		this.taSend = taSend;
	}

	void connect() {
		if (!connected) {
			connected=true;
			 new Thread(this).start();//
			 Log.i("已建立连接");
			new Thread() {
				public void run() {
					try {
						ss = new ServerSocket(8888);
						while (true) {
							Socket s = ss.accept();
							if(taRec!=null)taRec.append("一个用户连接了" + "\n");
							
							ServerXian sx;
							socketList.add(sx=new ServerXian(s,sql));
							new Thread(sx).start();// 一个服务器接收消息会阻塞
							// 所以多线程
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
		}
	}

	public void run() {
		JTextArea texRec = ServerWindow.textRec;
		while(connected){
			if(texRec!=null&&texRec.getText().length()>300)
				texRec.setText("");
		}
	}

	void send(String str) {
		ServerXian ss = null;
		try {
			for (int i=0;i<socketList.size();i++) {
				ss=socketList.get(i);						
				ss.send(str);
			}
			if(taSend!=null)taSend.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			socketList.remove(ss);
		}
	}

	void send() {
		if(taSend!=null)send(taSend.getText());
	}
	void sendAll(String str){
		send(str);
	}
	void sendAll(){
		send();
	}

	void closeStream() {
		try {
			for (ServerXian s : socketList) {
				s.closeStream();
			}
			connected = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
