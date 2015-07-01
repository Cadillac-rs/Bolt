package server.game.player;

public class Account {
	
	private final String username;
	private final String password;
	
	private final Authority authority;

	public Account(String username, String password, Authority authority) {
		this.username = username;
		this.password = password;
		this.authority = authority;
	}

}
