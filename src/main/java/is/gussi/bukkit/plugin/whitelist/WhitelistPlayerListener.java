package is.gussi.bukkit.plugin.whitelist;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class WhitelistPlayerListener extends PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		// TODO: Check CIDR blacklist
		
		// TODO: Check Player blacklist
		
		// TODO: Check CIDR whitelist
		
		// TODO: Check Player whitelist
	}
}
