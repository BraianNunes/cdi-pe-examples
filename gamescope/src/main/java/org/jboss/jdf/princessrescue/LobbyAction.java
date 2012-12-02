package org.jboss.jdf.princessrescue;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.jboss.jdf.princessrescue.scope.GamesManager;

@Model
public class LobbyAction {
	@Inject
	CurrentPlayerManager currentPlayerManager;
	
	@Inject
	GamesManager gamesManager;
	
	public String login() {
		currentPlayerManager.login();
		return "lobby";
	}
	
	public String createNewGame() {
		int gameId = gamesManager.createNewGame().getId();
		currentPlayerManager.joinGame(gameId);
		
		return "room";
	}
	
	public String joinGame(Integer gid) {
		currentPlayerManager.joinGame(gid);

		return "room";
	}
	
	public String leaveGame() {
		currentPlayerManager.leaveGame();
		
		return "lobby";
	}
}
