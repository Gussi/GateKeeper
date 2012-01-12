package is.gussi.bukkit.plugin.gatekeeper;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

abstract public class Command implements CommandExecutor {
	private String permission;
	protected CommandSender sender;
	protected org.bukkit.command.Command command;
	protected String label;
	protected String[] args;

	public boolean checkPermission(CommandSender sender) {
		if ((this.permission == null) || (this.permission.length() == 0) || (sender.hasPermission(this.permission))) {
				return true;
		}
		sender.sendMessage(ChatColor.RED + "Insufficient permissions");
		sender.sendMessage(ChatColor.RED + "Requires " + permission);
		return false;
	}

	protected String getPermission() {
		return permission;
	}

	protected void setPermission(String permission) {
		this.permission = permission;
	}
}
