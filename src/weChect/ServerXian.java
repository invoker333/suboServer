package weChect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

import fileSystem.ShowFile;
import mySql.Log;
import mySql.SelectQuery;
import mySql.User;
import aid.ConsWhenConnecting;

public class ServerXian implements Runnable{
	private Socket s;
	private JTextArea taRec;

	private SelectQuery sql;
	
	public ServerXian(Socket s, SelectQuery sql) {
		this.s = s;
		this.sql = sql;
		// TODO Auto-generated constructor stub
		taRec=ServerWindow.textRec;
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
					int userId = sql.makeUserId();
					
					String[] strArr = (str.substring(ConsWhenConnecting.REQUEST_NEW_USER_ID.length())).split(" ");
					User user=new User(userId,strArr[0],Integer.parseInt(strArr[1]));
					sql.insert(user);
					send(ConsWhenConnecting.THIS_IS_NEW_USER_ID+userId);
				}
				else if(str.startsWith(ConsWhenConnecting.REQUEST_PIMING_INFO)) {
					String resStr=sql.getPaiming(20);
					Log.i("serverLog+sql.getPaiming(20)"+resStr);
					send(ConsWhenConnecting.THIS_IS_PAIMING+resStr);
				}	
				else if(str.startsWith(ConsWhenConnecting.REQUEST_UPDATE_SCORE)) {
					String[] strArr = (str.substring(ConsWhenConnecting.REQUEST_UPDATE_SCORE.length())).split(" ");
					int score=Integer.parseInt(strArr[1]);
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
				}	
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			closeStream();
			e.printStackTrace();
		}
	}

	public void send(String str) {
		// TODO Auto-generated method stub
		
		
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeStream() {
		// TODO Auto-generated method stub
		if(s!=null)
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
