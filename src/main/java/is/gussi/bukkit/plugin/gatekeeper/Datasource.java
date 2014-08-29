package is.gussi.bukkit.plugin.gatekeeper;

import java.util.Set;

import org.bukkit.entity.Player;

public interface Datasource {
	// Add data entry
	public boolean add(Data data);
	
	// Remove data entry
	public boolean remove(Data data);
	
	// Return matching dataset
	public Set<Data> check(Player player);
}
