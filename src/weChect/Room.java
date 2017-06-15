package weChect;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import fileSystem.ShowFile;
import aid.ConsWhenConnecting;

public class Room {
	int roomId = 1;

	Room(int id) {
		roomId = id;
	}

	LinkedList<ServerXian> sxList = new LinkedList<>();
	LinkedList<ServerXian> blueList = new LinkedList<>();
	LinkedList<ServerXian> redList = new LinkedList<>();
	private boolean gameStarted;

	void addUser(ServerXian sx) {
		if(sxList.contains(sx))return;
		sxList.add(sx);
		sx.room=this;
		sx.roomId=this.roomId;
		
		if(blueList.size()>redList.size()){
			redList.add(sx);
		}else blueList.add(sx);
		
	}

	void removeUser(ServerXian sx) {
		sxList.remove(sx);
		blueList.remove(sx);
		redList.remove(sx);
		sx.roomId=-111;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return sxList.size();
	}

	public void sendRoomMessage() {
		// TODO Auto-generated method stub
		String rstr = ConsWhenConnecting.THIS_IS_SELECTED_ROOM_MESSAGE;
		for (ServerXian r:sxList) {
			rstr += " " + r.userName + " " + r.score;
			r.send(rstr);
		}
	}

	public void checkSocketClosed() {
		for (Iterator<ServerXian> iterator = sxList.iterator(); iterator
				.hasNext();) {
			ServerXian sx = iterator.next();
			if (sx.socketClosed || sx.roomId < 0) {
				sxList.remove(sx);
				break;
			}
		}
	}

	public void startGame(int userId) {
		// TODO Auto-generated method stub
		
		
		for (ServerXian sx : sxList) {//send foece message
			for (ServerXian blue : blueList) {
				sx.send(ConsWhenConnecting.THIS_ID_IS_BLUE_TEAM+blue.userId);
			}
			for (ServerXian red : redList) {
				sx.send(ConsWhenConnecting.THIS_ID_IS_RED_TEAM+red.userId);
			}
		}
		for (ServerXian sx : sxList) {
			if(sx.userId==userId){
				sx.send(ConsWhenConnecting.THIS_IS_THE_SELECTED_ONLINE_STAGE
						+ ShowFile.getSelectedBattleFileString("battle.txt"));
			}
			
		}

		gameStarted = true;
	}
	LinkedList<BattleMes2> battleMesList=new LinkedList<BattleMes2>();
	public  void handleBattleMessage(DatagramPacket dp, int userId, String strRes) {
		battleMesList.offer(new BattleMes2(dp,userId, strRes));
	}

	public void sendBattleMessage() {
		// TODO Auto-generated method stub
		LinkedList<BattleMes2> battleMesList=(LinkedList<BattleMes2>) this.battleMesList.clone();
		for (ServerXian r:sxList) {
			
			for(BattleMes2 bm:battleMesList){
				int userId=bm.userId;
				String strRes=bm.mes;
				if (r.userId != userId) {
					r.sendUdp(bm.dp,strRes);
				}
			}
		}
		
		this.battleMesList.clear();
		
	}

	public void sendAllTcp(String str) {
		// TODO Auto-generated method stub
		for(ServerXian sx:sxList){
			if(!sx.s.isClosed())
			sx.send(str);
		}
	}
}
class BattleMes2{
	int userId;
	String mes;
	 DatagramPacket dp;
	public BattleMes2(DatagramPacket dp, int userId,	String mes) {
		this.dp = dp;
		this.userId = userId;
		this.mes = mes;
	}
	
}