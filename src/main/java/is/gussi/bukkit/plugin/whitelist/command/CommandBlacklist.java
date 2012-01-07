package is.gussi.bukkit.plugin.whitelist.command;

import java.util.Date;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Util;
import is.gussi.bukkit.plugin.whitelist.Util.CommandAction;
import is.gussi.bukkit.plugin.whitelist.Whitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

// TODO: Make abstract parent class for this and CommandWhitelist, with common methods
public class CommandBlacklist implements CommandExecutor {

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
				data.setExpire(new Date(time + new Date().getTime()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		data.setType("blacklist");
		switch(action) {
			case ADD:
				if(Whitelist.plugin.ds.add(data)) {
					cs.sendMessage("Blacklisted...");
					return true;
				}
				cs.sendMessage("Unable to blacklist");
				break;
			case REMOVE:
				if(Whitelist.plugin.ds.remove(data)) {
					cs.sendMessage("Remove blacklisted entry...");
					return true;
				}
				cs.sendMessage("Unable to remove blacklisted entry");
				break;
			case CHECK:
				// TODO: Check
				break;
		}

		return false;
	}
}
