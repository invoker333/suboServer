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
		// String sqlDriverName =
		// "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		// String sqlDbURL=
		// "jdbc:sqlserver://localhost:1433;DatabaseName=gameInfo_Toms";
		// String sqlUserName = "sa";
		// String sqlUserPwd = "1234567890";

		String mySqlDriverName = "com.mysql.jdbc.Driver";
		String mySqlDbURL = "jdbc:mysql://localhost:3306/gameInfo_Toms?characterEncoding=utf8";
		String mySqlUserName = "root";
		String mySqlUserPwd = "";

		try {
			Class.forName(mySqlDriverName);
			Log.i("驱动加载成功");
			conn = DriverManager.getConnection(mySqlDbURL, mySqlUserName,
					mySqlUserPwd);
			Log.i("连接数据库成功");
		} catch (Exception e) {
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
		User user = new User((int) (100 * Math.random()),
				UserName.randomName(), (int) (100 * Math.random()));
		insert(user);Log.i("insert test");
		user = new User((int) (100 * Math.random()), UserName.randomName(),
				(int) (100 * Math.random()));
		updateName(user);
		user = new User((int) (100 * Math.random()), UserName.randomName(),
				(int) (100 * Math.random()));
		updateScore(user);Log.i("UpdateScore test");
		getPaiming(20);
		makeUserId();
		// delete(0);
	}

	public static int insert(User user) {
		Log.i(user.getUserId() + user.getUserName() + user.getUserScore());
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
		String sql = "update idAndName set userScore='"+ student.getUserScore() + "' where userId='"
				+ student.getUserId() + "'";
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

	public static int updateName(User student) {
		Connection conn = getConnection();
		int i = 0;
		String sql = "update idAndName set userName='" + student.getUserName()
				+ "' where userId='" + student.getUserId() + "'";
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
//		String sql="select top "+count+" *,rank() over(order by userScore desc) paiming from idAndName";
		String sql="SELECT obj.userName, obj.userScore, CASE	WHEN @rowtotal = obj.userScore THEN @rownum WHEN @rowtotal := obj.userScore THEN   @rownum :=@rownum + 1 WHEN @rowtotal = 0 THEN @rownum :=@rownum + 1 END AS rownum FROM   (SELECT 	userName,	userScore	FROM	gameinfo_toms.idandname	ORDER BY	userScore DESC	) AS obj, (SELECT @rownum := 0 ,@rowtotal := NULL) r";
		return get(sql);
	}

	static String getAll() {
		String sql = "select * from idAndName";
		return get(sql);
	}

	private static String get(String sql) {
		Connection conn = getConnection();
		PreparedStatement pstmt;
		String resStr = "";
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			Log.i("============================");
			while (rs.next()) {
				for (int i = 1; i <= col; i++) {
					resStr += rs.getString(i) + " ";
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
		return resStr;

	}

	private static int delete(int userId) {
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

	public static int makeUserId() {
		// TODO Auto-generated method stub
		String sql="SELECT gameinfo_toms.idandname.userId FROM	gameinfo_toms.idandname ORDER BY	userId DESC";   
		String res=get(sql);
		Log.i("makeUserId"+res);
		
		int userId=Integer.parseInt(res.split(" ")[0])+1+(int)(10*Math.random());
		final int million=999999999;
		if(res.split(" ")[0].length()<(""+million).length()) {
			userId=(int) (million*Math.random());
		}
		Log.i("realUserId"+userId);
		return userId;
	}
}
