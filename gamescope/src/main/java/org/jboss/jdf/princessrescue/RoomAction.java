package org.jboss.jdf.princessrescue;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.jdf.princessrescue.xmlbeans.Room;

@Model
public class RoomAction {
		
	@Inject
	CurrentPlayerManager currentPlayerManager;
	
	@Inject
	@Current
	Instance<Room> currentRoomInstance;
	
	public String getDescription() {
		boolean smellsPlayer = false;
		
		Room currentRoom = currentRoomInstance.get();
		
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
	
	private void moveTo(Room room) {
		currentPlayerManager.moveTo(room);
	}
	
	private void shootAt(Room room) {
		if (currentPlayerManager.shootAt(room)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your arrow hit a target!"));
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nothing happens..."));
		}
	}

	@IsAlive
	public void north() {
		moveTo(currentRoomInstance.get().getNorth());
	}
	
	@IsAlive
	public void south() {
		moveTo(currentRoomInstance.get().getSouth());
	}
	
	@IsAlive
	public void east() {
		moveTo(currentRoomInstance.get().getEast());
	}
	
	@IsAlive
	public void west() {
		moveTo(currentRoomInstance.get().getWest());
	}
	
	@IsAlive
	public void shootNorth() {
		shootAt(currentRoomInstance.get().getNorth());
	}
	
	@IsAlive
	public void shootSouth() {
		shootAt(currentRoomInstance.get().getSouth());
	}
	
	@IsAlive
	public void shootEast() {
		shootAt(currentRoomInstance.get().getEast());
	}

	@IsAlive
	public void shootWest() {
		shootAt(currentRoomInstance.get().getWest());
	}
}
