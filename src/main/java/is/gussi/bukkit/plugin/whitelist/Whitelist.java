package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.command.CommandBlacklist;
import is.gussi.bukkit.plugin.whitelist.command.CommandWhitelist;
import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.datasource.DatasourceMySQL;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Whitelist extends JavaPlugin {
	public static Whitelist plugin;
	public final static Logger log = Logger.getLogger("Minecraft.Whitelist");
	public Datasource ds;

	private Listener playerListener = new WhitelistPlayerListener();

	@Override
	public void onDisable() {
		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		Whitelist.plugin = this;
		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
		this.ds = new DatasourceMySQL();
		
		this.registerCommands();
		this.registerEvents();
		
		this.test();
	}
	
	private void registerCommands() {
		getCommand("whitelist").setExecutor(new CommandWhitelist());
		getCommand("blacklist").setExecutor(new CommandBlacklist());
	}
	
	private void registerEvents() {
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this.playerListener , Event.Priority.Highest, this);
	}
	
	private void test() {
		// temp test stuff, remove
		Whitelist.log.info("Brace for test...");
		DataCIDR cidr = new DataCIDR().setEnd(1).setStart(2);
		this.ds.add(cidr);
	}
}
