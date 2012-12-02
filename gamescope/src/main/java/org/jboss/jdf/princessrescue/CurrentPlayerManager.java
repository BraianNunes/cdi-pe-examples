package org.jboss.jdf.princessrescue;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.scope.CurrentGameId;
import org.jboss.jdf.princessrescue.scope.GamesManager;
import org.jboss.jdf.princessrescue.xmlbeans.Room;

@SessionScoped
public class CurrentPlayerManager implements Serializable {
	
	private int currentGameId = 0;
	
	@Inject
	private Player player;
	
	@Inject
	@Random
	private Instance<Room> initialRoom;
	
	@Inject
	Event<PlayerLogoffEvent> playerLogoffEvent;
	
	@Inject
	Event<PlayerLoginEvent> playerLoginEvent;
	
	@Inject
	Event<PlayerJoinedGameEvent> playerJoinedGameEvent;
	
	@Inject
	Event<PlayerLeftGameEvent> playerLeftGameEvent;
	
	@Inject
	GamesManager gamesManager;
	
	private Room currentRoom;
	
	@Produces
	@CurrentGameId
	public Integer getCurrentGameId() {
		return currentGameId;
	}
	
	@Produces
	@Current
	@Named
	public Player getCurrentPlayer() {
		return player;
	}
	
	public void joinGame(int gameId) {
		currentGameId = gameId;
		player.setShot(false);
		playerJoinedGameEvent.fire(new PlayerJoinedGameEvent());
	}
	
	public void leaveGame() {
		playerLeftGameEvent.fire(new PlayerLeftGameEvent());
		
		currentGameId = 0;
		currentRoom = null;
	}
	
	public void moveTo(Room room) {
		if (room != null) {
			if (currentRoom != null) {
				currentRoom.removePlayer(player);
			}
			
			currentRoom = room;
			room.addPlayer(player);
		}
	}
	
	public boolean shootAt(Room room) {
		return room.shootAt();
	}

	
	@Produces
	@Current
	@Named
	public Room getCurrentRoom() {
		if (currentRoom == null) {
			moveTo(initialRoom.get());
		}
		
		return currentRoom;
	}
	
	public void login() {
		playerLoginEvent.fire(new PlayerLoginEvent());
		player.setLoggedIn(true);
	}
	
	@PreDestroy
	public void preDestroy() {
		playerLeftGameEvent.fire(new PlayerLeftGameEvent());
		playerLogoffEvent.fire(new PlayerLogoffEvent());
		System.out.println("XXX predestroy on " + player.getName());
	}
}
