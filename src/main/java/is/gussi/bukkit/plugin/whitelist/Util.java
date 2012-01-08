package is.gussi.bukkit.plugin.whitelist;

public final class Util {
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
}
