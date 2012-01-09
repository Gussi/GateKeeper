package is.gussi.bukkit.plugin.gatekeeper;

import java.text.SimpleDateFormat;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class GateKeeperPlayerListener extends PlayerListener {
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Set<Data> matches = GateKeeper.plugin.ds.check(player);
		for(Data d : matches) {
			GateKeeper.log.info("[Whitelist] Match: " + d.toString());
			if(d.getType().equals("blacklist")) {
				GateKeeper.log.info("[Witelist] Player " + player.getName() + " blacklisted, rejected entry");
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
		GateKeeper.log.info("[Whitelist] Player " + player.getName() + " not whitelisted, rejected entry");
	}
}
