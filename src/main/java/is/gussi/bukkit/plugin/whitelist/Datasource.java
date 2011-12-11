package is.gussi.bukkit.plugin.whitelist;

import java.util.Set;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.data.DataPlayer;

public abstract class Datasource {
	abstract public boolean add(DataCIDR data);
	abstract public boolean add(DataPlayer data);
	public boolean add(Data data) {
		if(data instanceof DataCIDR) {
			return this.add((DataCIDR)data);
		} else if(data instanceof DataPlayer) {
			return this.add((DataPlayer)data);
		}
		return false;
	}

	abstract public boolean remove(DataCIDR data);
	abstract public boolean remove(DataPlayer data);
	public boolean remove(Data data) {
		if(data instanceof DataCIDR) {
			return this.remove((DataCIDR)data);
		} else if(data instanceof DataPlayer) {
			return this.remove((DataPlayer)data);
		}
		return false;
	}

	abstract public Set<Data> check(Player player);
}
