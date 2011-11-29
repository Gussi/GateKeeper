package is.gussi.bukkit.plugin.whitelist;

import is.gussi.bukkit.plugin.whitelist.data.CidrData;
import is.gussi.bukkit.plugin.whitelist.data.PlayerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

public class Data {

	public Data() {
		try {
			this.db().prepareStatement(Data.getSQL("whitelist_cidr.sql")).execute();
			this.db().prepareStatement(Data.getSQL("whitelist_player.sql")).execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean add(CidrData data) {
		// TODO: Add to cidr table
		return false;
	}
	
	public boolean add(PlayerData data) {
		// TODO: Add to player table
		return false;
	}
	
	public boolean remove(CidrData data) {
		// TODO: Remove from cidr table
		return false;
	}
	
	public boolean remove(PlayerData data) {
		// TODO: Remove from player table
		return false;
	}

	public Connection db() {
		FileConfiguration fc = Whitelist.plugin.getConfig();
		try {
			return DriverManager.getConnection("jdbc:mysql://" + fc.getString("mysql.host") + ":" + fc.getString("mysql.port") + "/" + fc.getString("mysql.database") + "?autoReconnect=true&user=" + fc.getString("mysql.user") + "&password=" + fc.getString("mysql.pass"));
		} catch (SQLException ex) {
			Whitelist.log.severe("Unable to connect to database!");
			Whitelist.plugin.getServer().getPluginManager().disablePlugin(Whitelist.plugin);
		}
		return null;
	}
	
	private static String getSQL(String table) {
		InputStream is = null;
		BufferedReader br = null;
		String line;
		ArrayList<String> list = new ArrayList<String>();

		try { 
			is = Data.class.getResourceAsStream("/sql/" + table);
			br = new BufferedReader(new InputStreamReader(is));
			while (null != (line = br.readLine())) {
				list.add(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null) br.close();
				if (is != null) is.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list.toString();
	}
}