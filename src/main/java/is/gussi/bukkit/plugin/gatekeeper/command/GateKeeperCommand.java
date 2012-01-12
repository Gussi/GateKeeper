package is.gussi.bukkit.plugin.gatekeeper.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
			args = (String[])tmp_args.toArray();
		}
		
		// Process whitelist command
		if(label.equalsIgnoreCase("wl") || label.equalsIgnoreCase("whitelist")) {
			// Get defined action
			CommandAction action = this.parseAction(args[0]);
			if(action == null) {
				return this.cmdHelp(sender, command, label, args);
			}
			
			Data data = this.parseData(args[1]);
			if(args.length > 2) {
				try {
					long time = this.parseTime(args[2]);
					data.setExpire(new Date(time + new Date().getTime()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			switch(action) {
				case ADD:
					if(!sender.hasPermission("whitelist.wl.add")) {
						return false;
					}
					if(GateKeeper.plugin.ds.add(data)) {
						sender.sendMessage("Added");
						return true;
					}
					sender.sendMessage("Unable to add...");
					break;
				case REMOVE:
					if(!sender.hasPermission("gatekeeper.wl.remove")) {
						return false;
					}
					if(GateKeeper.plugin.ds.remove(data)) {
						sender.sendMessage("Removed");
						return true;
					}
					break;
				case CHECK:
					break;
			}
		}
		
		// Process blacklist command
		if(label.equalsIgnoreCase("bl") || label.equalsIgnoreCase("blacklist")) {
			// TODO: All logic
			return false;
		}
		return true;
	}
	
	private boolean cmdHelp(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		// TODO: Help
		sender.sendMessage(ChatColor.GRAY + "I should give you some help right now...");
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
	
	/**
	 * Convert something like 1Y2M3w4d5h6m7s to milliseconds
	 *
	 * @param time
	 * @return
	 * @throws Exception Invalid time format
	 */
	private long parseTime(String time) throws Exception {
		long seconds = 0;
		StringBuilder stack = new StringBuilder();
		for(int i = 0; i < time.length(); ++i) {
			if(Character.isDigit(time.charAt(i))) {
				stack.append(time.charAt(i));
			} else {
				int s = Integer.parseInt(stack.toString());
				stack = new StringBuilder();

				// Seconds
				if(time.charAt(i) == 's') {
					seconds += s;
					continue;
				}

				// Minutes
				s = s * 60;
				if(time.charAt(i) == 'm') {
					seconds += s;
					continue;
				}

				// Hours
				s = s * 60;
				if(time.charAt(i) == 'h') {
					seconds += s;
					continue;
				}

				// Days
				s = s * 24;
				if(time.charAt(i) == 'd') {
					seconds += s;
					continue;
				}

				// Weeks
				s = s * 7;
				if(time.charAt(i) == 'w') {
					seconds += s;
					continue;
				}

				// Months
				s = s * 4;
				if(time.charAt(i) == 'M') {
					seconds += s;
					continue;
				}

				// Years
				s = s * 12;
				if(time.charAt(i) == 'Y') {
					seconds += s;
					continue;
				}
				
				// Permanent
				if(time.charAt(i) == 'P') {
					seconds = 0;
					break;
				}

				return -1;
			}
		}
		return seconds*1000;
	}
}