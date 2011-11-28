package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.data.DataFile;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Whitelist extends JavaPlugin {
	public static Whitelist plugin;
	public final static Logger log = Logger.getLogger("Whitelist");
	private Listener playerListener = new WhitelistPlayerListener();
	public AbstractData data = new DataFile();

	@Override
	public void onDisable() {
		Whitelist.log.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " is disabled");
	}

	@Override
	public void onEnable() {
		Whitelist.plugin = this;
		
		Whitelist.log.getParent().setLevel(Level.FINEST);
		
		
		Whitelist.log.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " is enabled");
		this.data.load();

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener , Event.Priority.Highest, this);

		getCommand("whitelist").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender commandsender, Command command, String alias, String[] args) {
				if (args.length == 2) {
					if(args[0].equals("add")) {
						try {
							Whitelist.plugin.data.add(args[1], "No info");
						} catch (Exception e) {
							commandsender.sendMessage("Unable to add :<");
							e.printStackTrace();
							return true;
						}
						commandsender.sendMessage("Added " + args[1]);
						return true;
					} else if (args[0].equals("remove")) {
						// Whitelist.plugin.data.remove(args[1]);
						commandsender.sendMessage("Removed " + args[1]);
						return true;
					} else if (args[0].equals("check")) {
						for (DataAddress da : Whitelist.plugin.data.getWhitelistMap().values()) {
							Whitelist.log.fine(da.toString());
						}
						return true;
					} else if (args[0].equals("test")) {
						Whitelist.plugin.data.save();
					}
				} else {
					commandsender.sendMessage("What a n00b :/");
					return true;
				}
				return false;
			}
			
		});
	}
}
