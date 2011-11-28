package is.gussi.bukkit.plugin.whitelist;

public final class DataAddress {
	public int start;
	public int end;
	public int mask;

	public String address;
	public String info;
	
	public boolean inRange(int address) {
		return this.start <= address && address <= this.end;
	}
	
	@Override
	public String toString() {
		return "DataAddress [start=" + start + ", end=" + end + ", mask="
				+ mask + ", address=" + address + ", info=" + info + "]";
	}

	public DataAddress(String address, String info) throws Exception {
		if (!address.contains("/")) {
			address = address + "/32";
		}
		// https://libbits.wordpress.com/2011/05/17/check-if-ip-is-within-range-specified-in-cidr-in-java/
		this.address = address;
		this.info = info;
		String[] parts = address.split("/");
		int addr = DataAddress.ip2long(parts[0]);
		int mask = (-1) << (32 - Integer.parseInt(parts[1]));
		this.start = addr & mask;
		this.end = this.start + (~mask);
	}
	
	public static int ip2long(String addr) {
		String ip[] = addr.split("\\.");
		return (( Integer.parseInt(ip[0]) << 24 ) & 0xFF000000) | (( Integer.parseInt(ip[1]) << 16 ) & 0xFF0000) | (( Integer.parseInt(ip[2]) << 8 ) & 0xFF00) | ( Integer.parseInt(ip[3]) & 0xFF);
	}
}
