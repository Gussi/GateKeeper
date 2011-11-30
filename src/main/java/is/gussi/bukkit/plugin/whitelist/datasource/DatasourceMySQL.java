package is.gussi.bukkit.plugin.whitelist.datasource;

import is.gussi.bukkit.plugin.whitelist.Data;
import is.gussi.bukkit.plugin.whitelist.Datasource;
import is.gussi.bukkit.plugin.whitelist.Whitelist;
import is.gussi.bukkit.plugin.whitelist.data.DataCIDR;
import is.gussi.bukkit.plugin.whitelist.data.DataPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DatasourceMySQL extends Datasource {

	public DatasourceMySQL() {
		try {
			this.db().prepareStatement(DatasourceMySQL.getSQL("whitelist_cidr.sql")).execute();
			this.db().prepareStatement(DatasourceMySQL.getSQL("whitelist_player.sql")).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean add(DataCIDR data) {
		try {
			PreparedStatement ps = this.db().prepareStatement("REPLACE INTO `whitelist_cidr` VALUES (?, ?, ?, ?, ?, ?)");
			ps.setString(1, data.getCidr());
			ps.setInt(2, data.getStart());
			ps.setInt(3, data.getEnd());
			ps.setString(4, data.getComment());
			ps.setString(5, data.getType().toString());
			ps.setLong(6, data.getExpire().getTime()/1000);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean add(DataPlayer data) {
		try {
			PreparedStatement ps = this.db().prepareStatement("REPLACE INTO `whitelist_player` VALUES (?, ?, ?, ?)");
			ps.setString(1, data.getPlayer());
			ps.setString(2, data.getComment());
			ps.setString(3, data.getType().toString());
			ps.setLong(4, data.getExpire().getTime()/1000);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(DataCIDR data) {
		try {
			PreparedStatement ps = this.db().prepareStatement("DELETE FROM `whitelist_cidr` WHERE `cidr` = ?");
			ps.setString(1, data.getCidr());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(DataPlayer data) {
		try {
			PreparedStatement ps = this.db().prepareStatement("DELETE FROM `whitelist_player` WHERE `player` = ?");
			ps.setString(1, data.getPlayer());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Connection db() {
		FileConfiguration fc = Whitelist.plugin.getConfig();
		try {
			return DriverManager.getConnection("jdbc:mysql://" + fc.getString("mysql.host") + ":" + Integer.toString(fc.getInt("mysql.port")) + "/" + fc.getString("mysql.database") + "?autoReconnect=true&user=" + fc.getString("mysql.username") + "&password=" + fc.getString("mysql.password"));
		} catch (SQLException ex) {
			ex.printStackTrace();
			Whitelist.plugin.getServer().getPluginManager().disablePlugin(Whitelist.plugin);
		}
		return null;
	}
	
	private static String getSQL(String table) {
		InputStream is = null;
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();

		try { 
			is = Whitelist.class.getResourceAsStream("/sql/" + table);
			br = new BufferedReader(new InputStreamReader(is));
			while (null != (line = br.readLine())) {
				sb.append(line);
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
		return sb.toString();
	}

	@Override
	public Set<Data> check(Player player) {
		Set<Data> data = new HashSet<Data>();

		try {
			PreparedStatement ps = this.db().prepareStatement("SELECT * FROM `whitelist_player` WHERE `player` = ?");
			ps.setString(1, player.getName());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs.next()) {
				data.add(this.makeDataPlayer(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement ps = this.db().prepareStatement("SELECT * FROM `whitelist_cidr` WHERE (`start` <= ? AND `end` <= ?)");
			int ip = 0;
			ps.setInt(1, ip);
			ps.setInt(2, ip);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs.next()) {
				data.add(this.makeDataCIDR(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	private DataPlayer makeDataPlayer(ResultSet result) {
		DataPlayer data = null;
		try {
			data = new DataPlayer(
				result.getString(1),
				result.getString(2),
				result.getString(3),
				new Date(Integer.parseInt(result.getString(4)))
			);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	private DataCIDR makeDataCIDR(ResultSet result) {
		DataCIDR data = null;
		try {
			data = new DataCIDR(
				result.getString(1),
				result.getInt(2),
				result.getInt(3),
				result.getString(4),
				result.getString(5),
				new Date(Integer.parseInt(result.getString(6)))
			);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}
