package mySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import com.mingli.toms.UserName;

public class SelectQuery {
	static Connection conn;

	// 创建一个返回值为Connection的方法
	public static Connection getConnection() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=gameInfo_Toms";
		String userName = "sa";
		String userPwd = "1234567890";
		try
		{
			Class.forName(driverName);
			Log.i("驱动加载成功");
			conn = DriverManager.getConnection(dbURL, userName, userPwd);
			Log.i("连接数据库成功");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.print("连接失败");
		}
		return conn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SelectQuery getcon = new SelectQuery();
		Connection conn = getcon.getConnection();

		getAll();
		User user=new User((int) (100*Math.random()),UserName.randomName(),(int) (100*Math.random()));
		insert(user);
		user=new User((int) (100*Math.random()),UserName.randomName(),(int) (100*Math.random()));
		updateName(user);
		user=new User((int) (100*Math.random()),UserName.randomName(),(int) (100*Math.random()));
		updateScore(user);
		getPaiming(5);
//		delete(0);
	}
	public static int insert(User user) {
		Log.i(user.getUserId()+user.getUserName()+user.getUserScore());
	    Connection conn = getConnection();
	    int i = 0;
	    String sql = "insert into idAndName (userId,userName,userScore) values(?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, user.getUserId());
	        pstmt.setString(2, user.getUserName());
	        pstmt.setInt(3, user.getUserScore());
	        i = pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	public static int updateScore(User student) {
		 Connection conn = getConnection();
	    int i = 0;
	    String sql = "update idAndName set userScore='" + student.getUserScore() + "' where userId='" + student.getUserId() + "'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        Log.i("resutl: " + i);
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	private static int updateName(User student) {
		 Connection conn = getConnection();
	    int i = 0;
	    String sql = "update idAndName set userName='" + student.getUserName() + "' where userId='" + student.getUserId() + "'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        Log.i("resutl: " + i);
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	 
	public static String getPaiming(int count){
		String sql="select top "+count+" *,rank() over(order by userScore desc) paiming from idAndName";
		return get(sql);
	}
	static String getAll(){
		String sql = "select * from idAndName";
		return get(sql);
	}
	
	private static String get(String sql) {
		 Connection conn = getConnection();
	    PreparedStatement pstmt;
	    String resStr="";
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        int col = rs.getMetaData().getColumnCount();
	        Log.i("============================");
	        while (rs.next()) {
	            for (int i = 1; i <= col; i++) {
	            	resStr+=rs.getString(i)+" ";
	                Log.print(rs.getString(i) + "\t");
	                if ((i == 2) && (rs.getString(i).length() < 8)) {
	                    Log.print("\t");
	                }
	             }
	            Log.i("");
	        }
	            Log.i("============================");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		Log.i("serverLog+sql.getPaiming(20)"+resStr);
	    return resStr;

	}
	private static int delete(int	userId) {
	    Connection conn = getConnection();
	    int i = 0;
	    String sql = "delete from idAndName where userId='" + userId + "'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        i = pstmt.executeUpdate();
	        Log.i("resutl: " + i);
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}

	public int makeUserId() {
		// TODO Auto-generated method stub
		return (int) (1000000000*Math.random());
	}
}

