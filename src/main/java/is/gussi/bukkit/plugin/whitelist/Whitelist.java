package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.command.*;
import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.datasource.*;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Whitelist extends JavaPlugin {
	public static Whitelist plugin;
	public static Server server;
	public final static Logger log = Logger.getLogger("Minecraft.Whitelist");
	public Datasource ds;

	private Listener playerListener = new WhitelistPlayerListener();

	@Override
	public void onDisable() {
		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		Whitelist.plugin = this;
		Whitelist.server = getServer();

		try {
			this.initializeConfig();
			this.registerDatasource();
			this.registerCommands();
			this.registerEvents();
			this.loadIsNet();
		} catch(Exception e) {
			Whitelist.log.severe("Error while enabling " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " : " + e.toString());
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
	}
	
	private void initializeConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	private void registerCommands() {
		getCommand("whitelist").setExecutor(new CommandWhitelist());
		getCommand("blacklist").setExecutor(new CommandBlacklist());
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this.playerListener , Event.Priority.Highest, this);
	}

	private void registerDatasource() {
		this.ds = new DatasourceMySQL();
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
				data.setType("whitelist");
				data.setSource("isnet");
				Whitelist.plugin.ds.add(data);
			}
		}
	}
}
