package is.gussi.bukkit.plugin.whitelist;

import java.util.Set;

import org.bukkit.entity.Player;

import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.data.DataPlayer;

public abstract class Datasource {
	abstract public boolean add(DataCIDR data);
	abstract public boolean add(DataPlayer data);

	abstract public boolean remove(DataCIDR data);
	abstract public boolean remove(DataPlayer data);

	abstract public Set<Data> check(Player player);
}
