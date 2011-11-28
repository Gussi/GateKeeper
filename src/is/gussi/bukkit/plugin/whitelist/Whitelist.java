package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.command.CommandBlacklist;
import is.gussi.bukkit.plugin.whitelist.command.CommandWhitelist;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Whitelist extends JavaPlugin {
	public static Whitelist plugin;
	public final static Logger log = Logger.getLogger("Minecraft.Whitelist");

	private Listener playerListener = new WhitelistPlayerListener();

	@Override
	public void onDisable() {
		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		Whitelist.plugin = this;
		Whitelist.log.info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");
		
		this.registerCommands();
		this.registerEvents();
	}
	
	private void registerCommands() {
		getCommand("whitelist").setExecutor(new CommandWhitelist());
		getCommand("blacklist").setExecutor(new CommandBlacklist());
	}
	
	private void registerEvents() {
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this.playerListener , Event.Priority.Highest, this);
	}
}
