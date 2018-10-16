package user;

import database.SQLiteUserControl;

public class User {

	private String username;
	private String password;
	private String name;
	private String type;
	
	public User(String username, String password, String name, String type) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.type = type; //ADMIN or USER
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	
	public void addToDB() {
		SQLiteUserControl.insertUser(this);
	}
	
	public void updatePassword() {
		SQLiteUserControl.updateUser(this);
	}
}
