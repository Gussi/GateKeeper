package is.gussi.bukkit.plugin.gatekeeper.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import is.gussi.bukkit.plugin.gatekeeper.Command;
import is.gussi.bukkit.plugin.gatekeeper.Data;
import is.gussi.bukkit.plugin.gatekeeper.GateKeeper;
import is.gussi.bukkit.plugin.gatekeeper.data.DataCIDR;
import is.gussi.bukkit.plugin.gatekeeper.data.DataPlayer;

public class GateKeeperCommand extends Command {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		// Sender requested help (or requested nothing)
		if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
			return this.cmdHelp(sender, command, label, args);
		}
		
		// Sender used /gk (/gatekeeper)
		if(label.equalsIgnoreCase("gk") || label.equalsIgnoreCase("gatekeeper")) {
			
			// Set the correct label according to the first argument
			if(args[0].equalsIgnoreCase("wl") || args[0].equalsIgnoreCase("whitelist")) {
				label = "wl";
			} else if(args[0].equalsIgnoreCase("bl") || args[0].equalsIgnoreCase("blacklist")) {
				label = "bl";
			} else {
				// Or get sender some help
				return this.cmdHelp(sender, command, label, args);
			}
			
			// Then shift one element off array, quite the hassle..
			List<String> tmp_args = new ArrayList<String>(Arrays.asList(args));
			tmp_args.remove(0);
			args = tmp_args.toArray(new String[0]);
		}

		if(args.length < 2) {
			return this.cmdHelp(sender, command, label, args);
		}

		// Get defined action
		CommandAction action = this.parseAction(args[0]);
		
		// Create data object
		Data data = this.parseData(args[1]);

		// Process whitelist command
		if(label.equalsIgnoreCase("wl") || label.equalsIgnoreCase("whitelist")) {
			data.setType("whitelist");
			
			switch(action) {
				case ADD:
					if(!sender.hasPermission("gatekeeper.whitelist.add")) {
						return false;
					}
					if(GateKeeper.plugin.ds.add(data)) {
						sender.sendMessage("GateKeeper: " + args[1] + " has been whitelisted");
						return true;
					}
					sender.sendMessage("Unable to whitelist " + args[1]);
					break;
				case REMOVE:
					if(!sender.hasPermission("gatekeeper.whitelist.remove")) {
						return false;
					}
					if(GateKeeper.plugin.ds.remove(data)) {
						sender.sendMessage("GateKeeper: " + args[1] + " has been removed from the whitelist");
						return true;
					}
					sender.sendMessage("Unable to remove " + args[1] + " from the whitelist");
					break;
				case CHECK:
					// TODO: Check
					break;
			}
		}

		return true;
	}
	
	private boolean cmdHelp(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.GRAY + "USAGE: /" + label + (label.equalsIgnoreCase("gk") || label.equalsIgnoreCase("gatekeeper") ? " <wl/bl>" : "") + " <add/rem> <user/ip/cidr> [expire]");
		return true;
	}
	
	private static enum CommandAction {
		ADD, REMOVE, CHECK
	}

	/**
	 * Get command action
	 * 
	 * @param action
	 * @return CommandAction
	 */
	private CommandAction parseAction(String action) {
		if(action.equals("add") || action.equals("a")) {
			return CommandAction.ADD;
		}
		if(action.equals("remove") || action.equals("rem") || action.equals("rm") || action.equals("r") || action.equals("d")) {
			return CommandAction.REMOVE;
		}
		if(action.equals("check") || action.equals("info") || action.equals("c") || action.equals("i")) {
			return CommandAction.CHECK;
		}
		return null;
	}

	/**
	 * Get command data
	 * 
	 * @param data
	 * @return Data
	 */
	private Data parseData(String data) {
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
		} else {
			// Assuming it's a player, validate
			Pattern validate = Pattern.compile("[A-Za-z0-9_]{1,16}");
			if(!validate.matcher(data).matches()) {
				return null;
			}
			return new DataPlayer(data);
		}
	}
}