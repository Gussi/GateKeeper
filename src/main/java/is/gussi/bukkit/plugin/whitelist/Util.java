package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.data.DataPlayer;

import java.util.regex.Pattern;

public final class Util {
	public static enum CommandAction {
		ADD, REMOVE, CHECK
	}

	/**
	 * Convert dotted IP to long
	 * 
	 * @param ip
	 * @return long
	 */
	public static long ip2long(String ip) {
		String[] part = ip.split("\\.");
		long num = 0;
		for (int i=0;i<part.length;i++) {
            int power = 3-i;
            num += ((Integer.parseInt(part[i])%256 * Math.pow(256,power)));
        }
		return num;
	}

	/**
	 * Get command action
	 * 
	 * @param action
	 * @return CommandAction
	 */
	public static CommandAction getCommandAction(String action) {
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
	public static Data getCommandData(String data) {
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
	 * Convert something like 1Y2M3w4d5h6m7s to seconds
	 *
	 * @param time
	 * @return
	 * @throws Exception Invalid time format
	 */
	public static long getTime(String time) throws Exception {
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

				throw new Exception("Invalid time format");
			}
		}
		return seconds;
	}
}
