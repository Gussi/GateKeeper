package is.gussi.bukkit.plugin.gatekeeper;

import is.gussi.bukkit.plugin.gatekeeper.command.*;
import is.gussi.bukkit.plugin.gatekeeper.data.DataCIDR;
import is.gussi.bukkit.plugin.gatekeeper.datasource.*;

import java.io.InputStream;
import java.net.URL;
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
	public static Logger log;
	public Datasource ds;

	@Override
	public void onDisable() {
		GateKeeper.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		GateKeeper.plugin = this;
		GateKeeper.server = getServer();
		GateKeeper.log = getLogger();
		this.getServer().getPluginManager().registerEvents(this, this);

		try {
			this.initializeConfig();
			this.registerDatasource();
			this.registerCommands();
			this.loadIsNet();
			this.loadPrivateNetwork();
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
		this.ds = new MySQL();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void checkPlayer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Set<Data> matches = this.ds.check(player);
		for(Data d : matches) {
			GateKeeper.log.info("[Whitelist] Match: " + d.toString());
		}
		if(matches.size() > 0) {
			return;
		}
		event.setJoinMessage(null);
		player.kickPlayer(ChatColor.GREEN + "Ekki á whitelist - http://forum.minecraft.is/");
		GateKeeper.log.info("[Whitelist] Player " + player.getName() + " not whitelisted, rejected entry");
	}
	
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
				data.setSource("isnet");
				GateKeeper.plugin.ds.add(data);
			}
			s.close();
		}
	}
	
	private void loadPrivateNetwork() {
		GateKeeper.plugin.ds.add(new DataCIDR("10.0.0.0/8"));
		GateKeeper.plugin.ds.add(new DataCIDR("172.16.0.0/12"));
		GateKeeper.plugin.ds.add(new DataCIDR("192.168.0.0/16"));
	}
}
