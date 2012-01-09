package is.gussi.bukkit.plugin.whitelist.command;

import java.util.Date;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Whitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBlacklist extends AbstractCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command command, String alias, String[] args) {
		if(args.length < 2) {
			return false;
		}

		CommandAction action = this.parseAction(args[0]);
		Data data = this.parseData(args[1]);

		if(action == null || data == null) {
			// TODO: Send error msg to user
			return false;
		}

		if(args.length > 2) {
			try {
				long time = this.parseTime(args[2]);
				data.setExpire(new Date(time + new Date().getTime()));
			} catch (Exception e1) {
				// TODO: Send error msg to user
				e1.printStackTrace();
			}
		}

		data.setType("blacklist");
		data.setSource(cs.getName());
		switch(action) {
			case ADD:
				if(!cs.hasPermission("whitelist.bl.add")) {
					return false;
				}
				if(Whitelist.plugin.ds.add(data)) {
					cs.sendMessage("Blacklisted...");
					return true;
				}
				cs.sendMessage("Unable to blacklist");
				break;
			case REMOVE:
				if(!cs.hasPermission("whitelist.bl.remove")) {
					return false;
				}
				if(Whitelist.plugin.ds.remove(data)) {
					cs.sendMessage("Remove blacklisted entry...");
					return true;
				}
				cs.sendMessage("Unable to remove blacklisted entry");
				break;
			case CHECK:
				if(!cs.hasPermission("whitelist.bl.check")) {
					return false;
				}
				// TODO: Check
				break;
		}

		return false;
	}
}
