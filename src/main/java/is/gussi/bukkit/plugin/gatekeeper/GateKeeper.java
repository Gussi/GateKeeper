package is.gussi.bukkit.plugin.gatekeeper;

import is.gussi.bukkit.plugin.gatekeeper.command.*;
import is.gussi.bukkit.plugin.gatekeeper.data.DataCIDR;
import is.gussi.bukkit.plugin.gatekeeper.datasource.*;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GateKeeper extends JavaPlugin implements Listener {
	public static GateKeeper plugin;
	public static Server server;
	public static final GateKeeperLogger log = new GateKeeperLogger();
	public Datasource ds;

	@Override
	public void onDisable() {
		GateKeeper.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		GateKeeper.plugin = this;
		GateKeeper.server = getServer();

		try {
			this.initializeConfig();
			this.registerDatasource();
			this.registerCommands();
			this.loadIsNet();
		} catch(Exception e) {
			GateKeeper.log.severe("Error while enabling " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		GateKeeper.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
	}
	
	private void initializeConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	private void registerCommands() {
		getCommand("gatekeeper").setExecutor(new GateKeeperCommand());
	}

	private void registerDatasource() {
		this.ds = new DatasourceMySQL();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void checkPlayer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Set<Data> matches = this.ds.check(player);
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

	public static class GateKeeperLogger {
		private final Logger log = Logger.getLogger("is.gussi.gatekeeper");
		protected String prefix = "[GateKeeper] ";

		public void info(String msg) {
			this.log.info(this.prefix + msg);
		}

		public void warning(String msg) {
			this.log.warning(this.prefix + msg);
		}

		public void severe(String msg) {
			this.log.severe(this.prefix + msg);
		}
	};
	
	// TODO: Move this to somewhere else
	private void loadIsNet() {
		URL url;
		InputStream stream = null;
		try {
			url = new URL("http://www.rix.is/is-net.txt");
			stream = url.openStream();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(stream != null) {
			Scanner s = new Scanner(stream);
			while(s.hasNextLine()) {
				DataCIDR data = new DataCIDR(s.nextLine());
				data.setType("whitelist");
				data.setSource("isnet");
				GateKeeper.plugin.ds.add(data);
			}
		}
	}
}
