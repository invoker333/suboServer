package fileSystem;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import mySql.Log;


public class ShowFile {
	private static final String fileAddressOndeskTop = "C:\\Users\\Administrator\\Desktop\\onlineStage\\";
	private static final String battleFileAddressOndeskTop = "C:\\Users\\Administrator\\Desktop\\battleStage\\";
//	private static final String fileAddressOndeskTop = "/home/onlineStages";

	private static String stageFileString;

	public static void main(String args[]){
		
		 String[] strArr = "1 1 1 1 1       ".split(" ");
		System.out.print(strArr.length);
		
		Frame frame=new Frame();
		JFileChooser fc=new JFileChooser("C:\\Users\\Administrator\\Desktop\\");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.showOpenDialog(frame);
		File f = fc.getSelectedFile();
		stageFileString = ListFiles(f);
		Log.i(stageFileString);
		
		getSelectedFileString("文本.txt");
		System.exit(0);
	}

	public static String ListFiles(File dir){
		if(!dir.exists()||!dir.isDirectory())return "";
		String fileStr="";
		String[]files=dir.list();
		for(int i=0;i<files.length;i++){
			File file=new File(dir,files[i]);
			if(file.isFile()){
				fileStr+=file.getName()+" ";
			}else{}
		}
		
		return fileStr;
	}

	public static String getStageFileString() {
		// TODO Auto-generated method stub
		return ListFiles(new File(fileAddressOndeskTop));
	}

	public static String getSelectedFileString(String title) {
		return getFileString(fileAddressOndeskTop,title);
	}
	public static String getFileString(String path,String title) {
		// TODO Auto-generated method stub
		String selectedString ="";
		File f=new File(path,title);
		BufferedReader br = null;
		 try {
			br = new BufferedReader(new FileReader(f));
			String rl="";
			while((rl=br.readLine())!=null){
				selectedString+=rl+"\r";
			}
			Log.i(selectedString);
			if(br!=null)br.close();
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedString;
	}

	public static String getSelectedBattleFileString(String title) {
		// TODO Auto-generated method stub
		return getFileString(battleFileAddressOndeskTop,title);
		
	}
}