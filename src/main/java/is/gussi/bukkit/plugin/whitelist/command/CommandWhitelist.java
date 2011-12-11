package is.gussi.bukkit.plugin.whitelist.command;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Util;
import is.gussi.bukkit.plugin.whitelist.Util.*;
import is.gussi.bukkit.plugin.whitelist.Whitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandWhitelist implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command command, String alias, String[] args) {
		if(args.length < 2) {
			return false;
		}

		CommandAction action = Util.getCommandAction(args[0]);
		Data data = Util.getCommandData(args[1]);

		if(action == null || data == null) {
			return false;
		}
		data.setType("whitelist");

		switch(action) {
			case ADD:
				if(Whitelist.plugin.ds.add(data)) {
					cs.sendMessage("Added");
					return true;
				} else {
					cs.sendMessage("Unable to add...");
				}
				break;
			case REMOVE:
				if(Whitelist.plugin.ds.remove(data)) {
					cs.sendMessage("Removed");
					return true;
				} else {
					cs.sendMessage("Unable to remove...");
				}
				break;
			case CHECK:
				// TODO: Check
				break;
		}
		return false;
	}
}
