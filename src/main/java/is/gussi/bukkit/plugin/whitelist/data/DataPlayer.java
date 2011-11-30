package is.gussi.bukkit.plugin.whitelist.data;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.Data;

public class DataPlayer extends Data {
	private String player;

	public String getPlayer() {
		return player;
	}

	public DataPlayer setPlayer(String player) {
		this.player = player;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Player)) {
			return false;
		}
		Player player = (Player)obj;
		if(player.getName().equals(this.player)) {
			return true;
		}
		return false;
	}
}
