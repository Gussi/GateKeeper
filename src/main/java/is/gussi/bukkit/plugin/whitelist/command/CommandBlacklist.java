package is.gussi.bukkit.plugin.whitelist.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBlacklist implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command command, String alias, String[] args) {
		return false;
	}
}
