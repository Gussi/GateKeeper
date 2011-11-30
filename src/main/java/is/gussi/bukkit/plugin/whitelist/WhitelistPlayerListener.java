package is.gussi.bukkit.plugin.whitelist;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class WhitelistPlayerListener extends PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Set<Data> matches = Whitelist.plugin.ds.check(player);
		for(Data d : matches) {
			Whitelist.log.info("Match: " + d.toString());
		}
	}
}
