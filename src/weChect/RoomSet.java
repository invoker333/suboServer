package weChect;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import aid.Client;
import aid.ConsWhenConnecting;

public class RoomSet {
	LinkedList<Room>roomList=new LinkedList<>();
	int id=1;
	public RoomSet(int j) {
		// TODO Auto-generated constructor stub
		roomList.clear();
		id=1;
		for(int i=0;i<j;i++) {
			roomList.add(new Room(id++));
		}
	}
	String getAllRoomMes(){
		String rstr=ConsWhenConnecting.THIS_IS_ROOMSET_MESSAGE;
		for (Room r:roomList) {
			rstr+=" "+r.roomId+" "+r.getSize();
		}
		return rstr;
	}
	public void startBattle(int roomId, int userId) {
		// TODO Auto-generated method stub
		for (Room room:roomList) {
			if(room.roomId==roomId){
				room.startGame(userId);
				break;
			}
		}
	}
	public void sendRoomMessage() {
		// TODO Auto-generated method stub
		for (Room room:roomList) {
			room.sendRoomMessage();
		}
	}
	public void changeRoom(ServerXian serverXian, int roomId) {
		// TODO Auto-generated method stub
		if(roomId<0||serverXian.roomId==roomId)return;
		if(serverXian.roomId>=0){
			for (Room r:roomList) {
				if(r.roomId==serverXian.roomId)
					r.removeUser(serverXian);
			}
		}
		
		{
			for (Room room:roomList) {
				if(room.roomId==roomId){
					room.addUser(serverXian);
				}
			}
		}
	}
	
	void checkSocketClosed(){
		for (Room room:roomList) {
			room.checkSocketClosed();
		}
	}
	public void handleBattleMessage(DatagramPacket dp, String str) {
		// TODO Auto-generated method stub
		if(str.startsWith(ConsWhenConnecting.THIS_IS_BATTLE_MESSAGE)){
			String[] strArr = (str.substring(ConsWhenConnecting.THIS_IS_BATTLE_MESSAGE.length())).split(" ");
			int roomId=Integer.parseInt(strArr[1]);
			int userId=Integer.parseInt(strArr[0]);
			for(Room r:roomList){
				if(r.roomId==roomId){
					r.handleBattleMessage(dp,userId, str);
				}
			}
		}
	}
	public void sendBattleMessage() {
		// TODO Auto-generated method stub
		for(Room r:roomList){
			r.sendBattleMessage();
		}
	}
	public void addRoom() {
		// TODO Auto-generated method stub
		if(!stack.isEmpty()){
			int roomId=stack.peek();//头部元素
			stack.pop();//去掉头部元素
			roomList.add(new Room(roomId));
		}
		else roomList.add(new Room(id++));
		Collections.sort(roomList,new Comparator<Room>(){
			@Override
			public int compare(Room r1, Room r2) {
				// TODO Auto-generated method stub
				return r1.roomId-r2.roomId;
			}
		});
	}
//	LinkedList <Integer>removedList=new LinkedList<Integer>();
	Stack<Integer> stack=new Stack<Integer>();
	public void reduceRoom() {
		for (Iterator<Room> iterator = roomList.descendingIterator(); iterator.hasNext();) {
			Room r = iterator.next();
			if(r.sxList.isEmpty()){
				roomList.remove(r);//iterator.remove();
//				removedList.add(r.roomId);
				stack.push((Integer)r.roomId);
				return;//only reduce one
			}
		}
	}
	public void sendAllTcp(String str) {
		// TODO Auto-generated method stub
		for(Room r:roomList){
			r.sendAllTcp(str);
		}
	}

}
