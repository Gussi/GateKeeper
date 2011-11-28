package is.gussi.bukkit.plugin.whitelist;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractData {
	protected Map<String, DataAddress> whitelistMap = new HashMap<String, DataAddress>();

	public Map<String, DataAddress> getWhitelistMap() {
		return whitelistMap;
	}

	public final void add(String address, String info) throws Exception {
		this.whitelistMap.put(address, new DataAddress(address, info));
	}

	public final void remove(String address, String description) {
		this.whitelistMap.remove(address);
	}

	abstract public void load();
	abstract public void save();
}
