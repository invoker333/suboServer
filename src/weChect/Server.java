package weChect;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextArea;

import aid.UdpSender;
import mySql.Log;
import mySql.SelectQuery;

public class Server implements Runnable{
	ServerSocket ss;
	private ArrayList<ServerXian> sxList = new ArrayList<>();
	private JTextArea taRec;
	private JTextArea taSend;
	private boolean connected;
	SelectQuery sql=new SelectQuery();
	private RoomSet roomSet;
	private UdpSender udpSender;

	public static void main(String[] args) {
		 Server s = new Server(null, null);
		 s.connect();
		 Scanner scan=new Scanner(System.in);
		 while(scan.hasNext()){
			 String inline=scan.nextLine();
			 if(inline.equals("quit")){
				 s.closeStream();
				 System.out.println("quit server--mingli");
				 System.exit(0);
			 }
		 }
	}
	
	Server(JTextArea taRec, JTextArea taSend) {
		this.taRec = taRec;
		this.taSend = taSend;
		
		roomSet=new RoomSet(5);
	}
	private void setUdpAddressPort( final int port) {
		// TODO Auto-generated method stub
		udpSender=new UdpSender(){
			protected void handleDatagramPacket(String str) {
//				System.out.println(str);
//				 {
//					if(roomSet!=null)
						roomSet.handleBattleMessage(str);
//				}	
			}
		};
		DatagramSocket ds = null;
		try {
			udpSender.startReceive(ds=new DatagramSocket(port));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("s.getInetAddress()"+ds.getInetAddress().getHostAddress());
		System.out.println("s.getRemoteSocketAddress()"+ds.getRemoteSocketAddress().toString());
		System.out.println("s.getPort"+ds.getPort()+ds.getLocalPort());
	}

	void connect() {
		if (!connected) {
			connected=true;
			 new Thread(this).start();//tcp receive thread
			
			 Log.i("已建立连接");
			new Thread() {//serverThread
				public void run() {
					try {
						int port=8888;
						ss = new ServerSocket(port);
						setUdpAddressPort(port);
						
						while (true) {
							Socket s = ss.accept();
							if(taRec!=null)taRec.append("一个用户连接了" + "\n");
							
							ServerXian sx;
							sxList.add(sx=new ServerXian(s,sql,roomSet, udpSender));
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
			
			roomSet.sendBattleMessage();
			
			if(timerId%30==0){
				if(texRec!=null&&texRec.getText()!=null&&texRec.getText().length()>300)
					texRec.setText("");
				
				send(roomSet.getAllRoomMes());//send all room's message
				roomSet.sendRoomMessage();// send selected message
			}
			if(timerId++>60){
				roomSet.checkSocketClosed();// check closed socket
				timerId=1;
			}
			sleep(1000/60);
		}
		System.out.println("房间线程结束！");
	}
	int timerId=0;


	void send(String str) {
		ServerXian ss = null;
		try {
			for (int i=0;i<sxList.size();i++) {
				ss=sxList.get(i);	
				if(!ss.socketClosed)
				ss.send(str);
			}
			if(taSend!=null)taSend.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sxList.remove(ss);
		}
	}

	void send() {
		if(taSend!=null)send(taSend.getText());
	}
	void sendAll(String str){
		send(str);
	}
	public void sendAllUdp(String str) {
		// TODO Auto-generated method stub
		for (int i=0;i<sxList.size();i++) {
			ServerXian ss=sxList.get(i);	
			if(!ss.socketClosed)
			ss.sendUdp(str);
		}
	}
	void sendAll(){
		send();
	}

	void closeStream() {
		try {
			for (ServerXian s : sxList) {
				s.closeStream();
			}
			
			connected = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addRoom() {
		// TODO Auto-generated method stub
		roomSet.addRoom();
	}

	public void reduceRoom() {
		// TODO Auto-generated method stub
		roomSet.reduceRoom();
	}


}
