package weChect;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class TestClient implements Runnable{
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	JTextArea taRec;
	JTextArea taSend;
	boolean connected;
	Thread recThread;
	TestClient(JTextArea taRec,JTextArea taSend){
			this.taRec=taRec;
			this.taSend=taSend;
					
	}
	public  static void main(String[]args){
		
	}
//	String #a=0;
	int $$$$$$$$$$$$$$$$=0;
//	double ~void c(){
//		
//	};
	 private void Client(int b){
			int a;
		}
	static final void Client(){
		int a;
	}
	void connect(){
		if(!connected){
			try {
				s=new Socket("127.0.0.1",8888);
//				s=new Socket("192.168.137.2",8888);
				if(s!=null){
					taRec.append("已连接至服务器"+"\n");
					connected=true;
				}
				dis=new DataInputStream(s.getInputStream());
				dos=new DataOutputStream(s.getOutputStream());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			(recThread=new Thread(this)).start();
			
		}
	}
	public void run(){
		String s;
		while(true){
			try {
				if((s=dis.readUTF())!=null)
				 taRec.append(s+"\n");				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				waitRecThread();
			}
			//ClientWindow.timeRush();//��ʱ����
		}
		
	}
	void send(){
		try {
			dos.writeUTF(taSend.getText());
			taSend.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void closeStream(){
		try {
			s.close();
			dis.close();
			dos.close();
			connected=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void waitRecThread(){
		try {
			recThread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
