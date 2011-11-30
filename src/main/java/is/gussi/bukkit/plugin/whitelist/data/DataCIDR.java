package is.gussi.bukkit.plugin.whitelist.data;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.Data;

public class DataCIDR extends Data {
	private String cidr = "0.0.0.0/0";
	private int start = 0;
	private int end = 0;

	public String getCidr() {
		return cidr;
	}

	public DataCIDR setCidr(String cidr) {
		this.cidr = cidr;
		return this;
	}

	public int getStart() {
		return start;
	}

	public DataCIDR setStart(int start) {
		this.start = start;
		return this;
	}

	public int getEnd() {
		return end;
	}

	public DataCIDR setEnd(int end) {
		this.end = end;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Player)) {
			return false;
		}
		Player player = (Player)obj;
		int ip = this.ip2long(player.getAddress().getAddress().getHostAddress());
		if(start <= ip && ip <= end) {
			return true;
		}
		return false;
	}
	
	private int ip2long(String ip) {
		// TODO ip to long
		return 0;
	}
}
