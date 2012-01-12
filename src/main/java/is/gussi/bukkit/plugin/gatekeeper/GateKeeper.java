package is.gussi.bukkit.plugin.gatekeeper;

import is.gussi.bukkit.plugin.gatekeeper.command.*;
import is.gussi.bukkit.plugin.gatekeeper.data.DataCIDR;
import is.gussi.bukkit.plugin.gatekeeper.datasource.*;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GateKeeper extends JavaPlugin {
	public static GateKeeper plugin;
	public static Server server;
	public static final GateKeeperLogger log = new GateKeeperLogger();
	public Datasource ds;
	private Listener playerListener = new GateKeeperPlayerListener();

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
			this.registerEvents();
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

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this.playerListener , Event.Priority.Highest, this);
	}

	private void registerDatasource() {
		this.ds = new DatasourceMySQL();
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
