package is.gussi.bukkit.plugin.whitelist.command;

import java.util.Date;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Util;
import is.gussi.bukkit.plugin.whitelist.Util.CommandAction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
				// TODO: Add, if successful check all players and kick
				break;
			case REMOVE:
				// TODO: Remove
				break;
			case CHECK:
				// TODO: Check
				break;
		}

		return false;
	}
}
