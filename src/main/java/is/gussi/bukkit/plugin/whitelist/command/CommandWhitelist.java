package is.gussi.bukkit.plugin.whitelist.command;

import java.util.regex.Pattern;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Whitelist;
import is.gussi.bukkit.plugin.whitelist.data.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandWhitelist implements CommandExecutor {
	private enum Action {
		ADD, REMOVE, CHECK
	}

	@Override
	public boolean onCommand(CommandSender cs, Command command, String alias, String[] args) {
		if(args.length != 2) {
			return false;
		}

		Action action = this.getAction(args[0]);
		Data data = this.getData(args[1]);

		if(action == null || data == null) {
			return false;
		}

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
					cs.sendMessage("removed");
					return true;
				} else {
					cs.sendMessage("Unable to remove...");
				}
				break;
			case CHECK:
				// Whitelist.plugin.ds.check(data);
				break;
		}
		return false;
	}

	public Action getAction(String action) {
		if(action.equals("add") || action.equals("a")) {
			return Action.ADD;
		}
		if(action.equals("remove") || action.equals("rem") || action.equals("rm") || action.equals("r") || action.equals("d")) {
			return Action.REMOVE;
		}
		if(action.equals("check") || action.equals("info") || action.equals("c") || action.equals("i")) {
			return Action.CHECK;
		}
		return null;
	}

	public Data getData(String data) {
		if(data.contains(".")) {
			// Assuming it's an IP
			if(!data.contains("/")) {
				data = data + "/32";
			}
			// Validate
			Pattern validate = Pattern.compile("((2[0-5]{2}|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(2[0-5]{2}|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)(/(3[012]|[12]\\d|\\d))?");
			if(!validate.matcher(data).matches()) {
				return null;
			}
			return new DataCIDR(data);
		}
		return null;
	}
}
