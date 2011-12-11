package is.gussi.bukkit.plugin.whitelist.data;

import java.util.Date;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Whitelist;

public class DataCIDR extends Data {
	private String cidr = "0.0.0.0/0";
	private long start = 0;
	private long end = 0;
	private String comment;
	private String type;
	private Date expire;

	public DataCIDR(String cidr, long start, long end, String comment, String type, Date expire) {
		this.cidr = cidr;
		this.start = start;
		this.end = end;
		this.comment = comment;
		this.type = type;
		this.expire = expire;
	}

	public DataCIDR(String cidr) {
		this.cidr = cidr;
		String[] ip = cidr.split("/");
		this.start = ip2long(ip[0]);
		Whitelist.log.info("Math1: " + Integer.toString(32-Integer.parseInt(ip[1])));
		Whitelist.log.info("Math2: " + Integer.toString((int)Math.pow(2, 32-Integer.parseInt(ip[1]))));
		this.end = this.start + (int)Math.pow(2, 32-Integer.parseInt(ip[1]));
		Whitelist.log.info("result: " + this.end);
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
		if(!(obj instanceof Player)) {
			return false;
		}
		Player player = (Player)obj;
		long ip = ip2long(player.getAddress().getAddress().getHostAddress());
		if(start <= ip && ip <= end) {
			return true;
		}
		return false;
	}

	public static long ip2long(String ip) {
		String[] part = ip.split("\\.");
		long num = 0;
		for (int i=0;i<part.length;i++) {
            int power = 3-i;
            num += ((Integer.parseInt(part[i])%256 * Math.pow(256,power)));
        }
		return num;
	}

	@Override
	public String toString() {
		return "DataCIDR [cidr=" + cidr + ", start=" + start + ", end=" + end
				+ ", comment=" + comment + ", type=" + type + ", expire="
				+ expire + "]";
	}
}
