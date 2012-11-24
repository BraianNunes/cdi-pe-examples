package org.jboss.jdf.princessrescue;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.scope.CurrentGameId;
import org.jboss.jdf.princessrescue.scope.GamesManager;
import org.jboss.jdf.princessrescue.xmlbeans.Room;
import org.jboss.jdf.princessrescue.xmlbeans.RoomName;

@Named
@SessionScoped
public class GameControl implements Serializable {
	
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
	
	public void setCurrentGameId(Integer gameId) {
		currentGameId = gameId;
	}
	
	public String createNewGame() {
		currentGameId = gamesManager.createNewGame().getId();
		
		player.setShot(false);
		
		playerJoinedGameEvent.fire(new PlayerJoinedGameEvent());
		
		return "room";
	}
	
	public String joinGame(Integer gid) {
		currentGameId = gid;
		
		player.setShot(false);
		
		playerJoinedGameEvent.fire(new PlayerJoinedGameEvent());
	
		return "room";
	}
	
	public String leaveGame() {
		playerLeftGameEvent.fire(new PlayerLeftGameEvent());
		
		currentGameId = 0;
		currentRoom = null;
		
		return "lobby";
	}
	
	Room currentRoom;
	
	public void setCurrentRoom(Room room) {
		if (room != null) {
			if (currentRoom != null) {
				currentRoom.removePlayer(player);
			}
			
			currentRoom = room;
			room.addPlayer(player);
		}
	}
	
	private void shootAt(Room room) {
		if (room.shootAt()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your arrow hit a target!"));
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nothing happens..."));
		}
	}
	
	private void moveTo(Room room) {		
		setCurrentRoom(room);
	}
	
	@Produces
	@Current
	@Named
	public Room getCurrentRoom() {
		if (currentRoom == null) {
			setCurrentRoom(initialRoom.get());
		}
		
		return currentRoom;
	}
	
	public String getDescription() {
		boolean smellsPlayer = false;
		
		Room currentRoom = getCurrentRoom();
		
		if (currentRoom.getNorth() != null) {
			if (currentRoom.getNorth().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getSouth() != null) {
			if (currentRoom.getSouth().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getEast() != null) {
			if (currentRoom.getEast().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		if (currentRoom.getWest() != null) {
			if (currentRoom.getWest().getPlayers().size() > 0) {
				smellsPlayer = true;
			}
		}
		
		if (smellsPlayer) {
			return currentRoom.getDescription() + " You smell another player nearby.";
		}
		else {
			return currentRoom.getDescription();
		}
	}
	
	public Player getPlayer() {
		return player; 
	}

	@IsAlive
	public void north() {
		moveTo(currentRoom.getNorth());
	}
	
	@IsAlive
	public void south() {
		moveTo(currentRoom.getSouth());
	}
	
	@IsAlive
	public void east() {
		moveTo(currentRoom.getEast());
	}
	
	@IsAlive
	public void west() {
		moveTo(currentRoom.getWest());
	}
	
	@IsAlive
	public void shootNorth() {
		shootAt(currentRoom.getNorth());
	}
	
	@IsAlive
	public void shootSouth() {
		shootAt(currentRoom.getSouth());
	}
	
	@IsAlive
	public void shootEast() {
		shootAt(currentRoom.getEast());
	}
	
	@IsAlive
	public void shootWest() {
		shootAt(currentRoom.getWest());
	}
	
	public String login() {
		playerLoginEvent.fire(new PlayerLoginEvent());
		
		player.setLoggedIn(true);
		
		return "lobby";
	}
	
	@PreDestroy
	public void preDestroy() {
		playerLeftGameEvent.fire(new PlayerLeftGameEvent());
		playerLogoffEvent.fire(new PlayerLogoffEvent());
		System.out.println("XXX predestroy on " + player.getName());
	}
	
	public void destroy() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
}
