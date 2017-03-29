package mySql;

public class User {
	private int userId;
	private int userScore;
	private String userName;

	public User(int userId, String userName, int userScore) {
		this.userId = userId;
		this.setUserName(userName);
		this.setUserScore(userScore);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserScore() {
		return userScore;
	}

	public void setUserScore(int userScore) {
		this.userScore = userScore;
	}



}
