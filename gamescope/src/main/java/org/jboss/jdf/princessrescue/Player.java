package org.jboss.jdf.princessrescue;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.scope.CurrentGameId;
import org.jboss.jdf.princessrescue.xmlbeans.Room;
import org.jboss.jdf.princessrescue.xmlbeans.RoomName;

public class Player implements Serializable {
	
	private String name = "Anonymous";
		
	private boolean shot = false;
	private boolean loggedIn = false;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
