package is.gussi.bukkit.plugin.whitelist;

import java.text.SimpleDateFormat;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class WhitelistPlayerListener extends PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Set<Data> matches = Whitelist.plugin.ds.check(player);
		for(Data d : matches) {
			Whitelist.log.info("Match: " + d.toString()); // Debug message
			Whitelist.log.info("Type: " + d.getType());
			if(d.getType().equals("blacklist")) {
				Whitelist.log.info("Blacklisted brah! Get out!");
				event.setJoinMessage(null);
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				player.kickPlayer(ChatColor.RED + "Blacklisted til " + ChatColor.GOLD + formatter.format(d.getExpire()) + ChatColor.RED + " vegna " + ChatColor.GOLD + d.getComment());
				return;
			} else if(d.getType().equals("whitelist")) {
				return;
			}
		}
		event.setJoinMessage(null);
		player.kickPlayer(ChatColor.GREEN + "Ekki á whitelist - http://forum.minecraft.is/");
	}
}
