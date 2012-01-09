package is.gussi.bukkit.plugin.whitelist.data;

import java.util.Date;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Util;

public class DataCIDR extends Data {
	private String cidr = "0.0.0.0/0";
	private long start = 0;
	private long end = 0;

	public DataCIDR(String cidr, long start, long end, String comment, String type, Date expire, String source) {
		this.cidr = cidr;
		this.start = start;
		this.end = end;
		this.comment = comment;
		this.type = type;
		this.expire = expire;
		this.source = source;
	}

	public DataCIDR(String cidr) {
		this.cidr = cidr;
		String[] ip = cidr.split("/");
		this.start = Util.ip2long(ip[0]);
		this.end = this.start + (int)Math.pow(2, 32-Integer.parseInt(ip[1]));
	}

	public String getCidr() {
		return cidr;
	}

	public DataCIDR setCidr(String cidr) {
		this.cidr = cidr;
		return this;
	}

	public long getStart() {
		return start;
	}

	public DataCIDR setStart(int start) {
		this.start = start;
		return this;
	}

	public long getEnd() {
		return end;
	}

	public DataCIDR setEnd(int end) {
		this.end = end;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player) {
			Player player = (Player)obj;
			long ip = Util.ip2long(player.getAddress().getAddress().getHostAddress());
			if(start <= ip && ip <= end) {
				return true;
			}
		}
		if(obj instanceof DataCIDR) {
			DataCIDR data = (DataCIDR)obj;
			if(data.getCidr() == this.cidr) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "DataCIDR [cidr=" + cidr + ", start=" + start + ", end=" + end
				+ ", comment=" + comment + ", type=" + type + ", expire="
				+ expire + "]";
	}
}
