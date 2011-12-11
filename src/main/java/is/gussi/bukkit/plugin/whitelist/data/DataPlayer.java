package is.gussi.bukkit.plugin.whitelist.data;

import java.util.Date;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.Data;

public class DataPlayer extends Data {
	private String player;
	
	public DataPlayer(String player) {
		this.player = player;
	}

	public DataPlayer(String player, String comment, String type, Date expire) {
		this.player = player;
		this.comment = comment;
		this.type = type;
		this.expire = expire;
	}

	public String getPlayer() {
		return player;
	}

	public DataPlayer setPlayer(String player) {
		this.player = player;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player) {
			Player player = (Player)obj;
			if(player.getName().equals(this.player)) {
				return true;
			}
		}
		if(obj instanceof DataPlayer) {
			DataPlayer data = (DataPlayer)obj;
			if(data.getPlayer().equals(this.player)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "DataPlayer [player=" + player + ", comment=" + comment
				+ ", type=" + type + ", expire=" + expire + "]";
	}

}
