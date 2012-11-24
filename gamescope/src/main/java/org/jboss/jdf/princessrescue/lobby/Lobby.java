package org.jboss.jdf.princessrescue.lobby;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.Current;
import org.jboss.jdf.princessrescue.Player;
import org.jboss.jdf.princessrescue.PlayerLoginEvent;
import org.jboss.jdf.princessrescue.PlayerLogoffEvent;

@Named
@ApplicationScoped
public class Lobby {
	private List<Player> players = new LinkedList<Player> ();
	
	public void onPlayerLogin(@Observes PlayerLoginEvent event, @Current Player player) throws PlayerExistsException {
		for (Player p : players) {
			if (p.getName().equals(player.getName())) {
				throw new PlayerExistsException();
			}
		}
		
		players.add(player);
	}
	
	public void onPlayerLogoff(@Observes PlayerLogoffEvent event, @Current Player player) {
		Iterator<Player> i = players.iterator();
		while (i.hasNext()) {
			Player p = i.next();
			if (p.getName().equals(player.getName())) {
				i.remove();
			}
		}
	}
	
	public List<Player> getPlayers() {
		return players;
	}
}
