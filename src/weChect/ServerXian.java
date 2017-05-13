package weChect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import javax.swing.JTextArea;

import mySql.Log;
import mySql.SelectQuery;
import mySql.User;
import Mankind.Player;
import aid.ConsWhenConnecting;
import aid.UdpReceiver;
import aid.UdpSender;
import fileSystem.ShowFile;

public class ServerXian implements Runnable{
	private Socket s;
	private JTextArea taRec;

	private SelectQuery sql;
	private RoomSet roomSet;
	String userName;
	int roomId=-1;
	int score;
	public boolean socketClosed;
	Room room;
	int userId;
	
	UdpSender udpSender;
	
	public ServerXian(Socket s, SelectQuery sql, RoomSet roomSet ) {
		this.s = s;
		this.sql = sql;
		this.roomSet = roomSet;
		System.out.println("s.getInetAddress()"+s.getInetAddress().getHostAddress());
		System.out.println("s.getRemoteSocketAddress()"+s.getRemoteSocketAddress().toString());
		System.out.println("s.getPort"+s.getPort()+s.getLocalPort());
		setUdpAddressPort(s.getInetAddress().getHostAddress(),s.getLocalPort());
		
		// TODO Auto-generated constructor stub
		taRec=ServerWindow.textRec;
	}
	private void setUdpAddressPort(String address, final int port) {
		// TODO Auto-generated method stub

		
		udpSender=new UdpSender();
		udpSender.connect(address, port);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String str;
		DataInputStream dis;
		try {
			dis = new DataInputStream(s.getInputStream());
			while(true){
				if ((str = dis.readUTF()) != null)
				if(taRec!=null)taRec.append(str + "\n");
				if(str.startsWith(ConsWhenConnecting.REQUEST_NEW_USER_ID)) {
					userId = sql.makeUserId();
					
					String[] strArr = (str.substring(ConsWhenConnecting.REQUEST_NEW_USER_ID.length())).split(" ");
					User user=new User(userId,strArr[0],Integer.parseInt(strArr[1]));
					sql.insert(user);
					send(ConsWhenConnecting.THIS_IS_NEW_USER_ID+userId);
				}
				else if(str.startsWith(ConsWhenConnecting.REQUEST_PIMING_INFO)) {
					sendPaimingInfo();
				}	
				else if(str.startsWith(ConsWhenConnecting.REQUEST_UPDATE_SCORE)) {
					String[] strArr = (str.substring(ConsWhenConnecting.REQUEST_UPDATE_SCORE.length())).split(" ");
					score = Integer.parseInt(strArr[1]);
					User user=new User(Integer.parseInt(strArr[0]),null,score);
				
					sql.updateScore(user);
				}	
				else if(str.startsWith(ConsWhenConnecting.REQUEST_ONLINE_STAGE)) {
					String stageString=ShowFile.getStageFileString();
					send(ConsWhenConnecting.THIS_IS_ONLINE_STAGE+stageString);
				}
				else if(str.startsWith(ConsWhenConnecting.REQUEST_THIS_ONE_ONLINE_STAGE)) {
					String strRes=str.substring(ConsWhenConnecting.REQUEST_THIS_ONE_ONLINE_STAGE.length());
					send(ConsWhenConnecting.THIS_IS_THE_SELECTED_ONLINE_STAGE+ShowFile.getSelectedFileString(strRes));
				}
					
				else if(str.startsWith(ConsWhenConnecting.REQUEST_UPDATE_NAME)) {
					String[] strArr = (str.substring(ConsWhenConnecting.REQUEST_UPDATE_NAME.length())).split(" ");
					int id=Integer.parseInt(strArr[0]);
					String name=strArr[1];
					User user=new User(id,name,-1);
					sql.updateName(user);
					sendPaimingInfo();
				}		else if(str.startsWith(ConsWhenConnecting.REQUEST_THIS_ROOM)) {
					String strRes=str.substring(ConsWhenConnecting.REQUEST_THIS_ROOM.length());
					int roomId=Integer.parseInt(strRes);
					roomSet.changeRoom(this,roomId);
				}	else if(str.startsWith(ConsWhenConnecting.THIS_IS_USER_ID_AND_NAME)) {
					String strRes=str.substring(ConsWhenConnecting.THIS_IS_USER_ID_AND_NAME.length());
					String[] ss = strRes.split(" ");
					userId=Integer.parseInt(ss[0]);
					userName=ss[1];
					System.out.println("userIDAndName"+userId+userName);
				}else if(str.startsWith(ConsWhenConnecting.REQUEST_START_BATTLE)) {
					String strRes=str.substring(ConsWhenConnecting.REQUEST_START_BATTLE.length());
					int roomId=Integer.parseInt(strRes);
					roomSet.startBattle(roomId,userId);
				}else if(str.startsWith(ConsWhenConnecting.USE_ITEM)) {
					roomSet.sendAllTcp(str);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			closeStream();
			e.printStackTrace();
		}
	}

	private void sendPaimingInfo() {
		String resStr=sql.getPaiming(20);
		Log.i("serverLog+sql.getPaiming(20)"+resStr);
		send(ConsWhenConnecting.THIS_IS_PAIMING+resStr);
	}
	public  void  sendUdp(String str){
		if(udpSender!=null){
			udpSender.send(str);
		}
	}
	public void send(String str) {
		
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeStream();
		}
	}

	public void closeStream() {
		// TODO Auto-generated method stub
		if(s!=null)
			try {
				udpSender.closeStream();
				s.close();
				socketClosed=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
