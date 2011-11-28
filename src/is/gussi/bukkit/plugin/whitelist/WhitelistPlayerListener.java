package is.gussi.bukkit.plugin.whitelist;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class WhitelistPlayerListener extends PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int addr = DataAddress.ip2long(player.getAddress().getAddress().getHostAddress());
		
		for (DataAddress da : Whitelist.plugin.data.getWhitelistMap().values()) {
			if (da.inRange(addr)) {
				return;
			}
		}
		
		event.setJoinMessage(null);
		player.kickPlayer(ChatColor.RED + "Einungis íslenskar IP tölur eru á whitelist. Láttu Gussa vita á http://irc.minecraft.is/ :)");
	}
}
