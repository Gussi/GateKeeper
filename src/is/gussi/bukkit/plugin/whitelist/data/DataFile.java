package is.gussi.bukkit.plugin.whitelist.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import is.gussi.bukkit.plugin.whitelist.AbstractData;
import is.gussi.bukkit.plugin.whitelist.DataAddress;
import is.gussi.bukkit.plugin.whitelist.Whitelist;

public class DataFile extends AbstractData {

	@Override
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Whitelist.plugin.getDataFolder() + "/whitelist.txt"));
			String line;
			try {
				int i = 0;
				while((line = reader.readLine()) != null) {
					++i;
					String[] part = line.split(";");
					try {
						this.add(part[0], part[1]);
					} catch (Exception e) {
						Whitelist.log.warning("Unable to load entry on line #" + i + " in whitelist.txt");
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				Whitelist.log.severe("Unable to read whitelist.txt");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			Whitelist.log.severe("Unable to find whitelist.txt");
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			Writer writer = new BufferedWriter(new FileWriter(Whitelist.plugin.getDataFolder() + "/whitelist.txt"));
			for (DataAddress da : this.whitelistMap.values()) {
				writer.append(da.address + ";" + da.info + "\n");
			}
			writer.close();
		} catch (IOException e) {
			Whitelist.log.severe("Unable to open whitelist.txt, can't save.");
			e.printStackTrace();
		}
	}
}
