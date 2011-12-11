package is.gussi.bukkit.plugin.whitelist.command;

import java.util.Date;

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

		if(args.length > 2) {
			try {
				long time = Util.getTime(args[2]);
				data.setExpire(new Date(time*1000 + new Date().getTime()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
				break;
		}
		return false;
	}
}
