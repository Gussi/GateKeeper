package is.gussi.bukkit.plugin.gatekeeper.datasource;

import is.gussi.bukkit.plugin.gatekeeper.Data;
import is.gussi.bukkit.plugin.gatekeeper.Datasource;
import is.gussi.bukkit.plugin.gatekeeper.Util;
import is.gussi.bukkit.plugin.gatekeeper.GateKeeper;
import is.gussi.bukkit.plugin.gatekeeper.data.DataCIDR;
import is.gussi.bukkit.plugin.gatekeeper.data.DataPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DatasourceMySQL extends Datasource {

	public DatasourceMySQL() {
		try {
			this.db().prepareStatement(DatasourceMySQL.getSQL("gatekeeper_cidr.sql")).execute();
			this.db().prepareStatement(DatasourceMySQL.getSQL("gatekeeper_player.sql")).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean add(DataCIDR data) {
		PreparedStatement ps = null;
		try {
			ps = this.db().prepareStatement("REPLACE INTO `gatekeeper_cidr` VALUES (?, ?, ?, ?, ?, ?)");
			ps.setString(1, data.getCidr());
			ps.setLong(2, data.getStart());
			ps.setLong(3, data.getEnd());
			ps.setString(4, data.getComment());
			ps.setString(5, data.getType());
			ps.setString(7, data.getSource());
			return ps.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean add(DataPlayer data) {
		PreparedStatement ps = null;
		try {
			ps = this.db().prepareStatement("REPLACE INTO `gatekeeper_player` VALUES (?, ?, ?, ?)");
			ps.setString(1, data.getPlayer());
			ps.setString(2, data.getComment());
			ps.setString(3, data.getType());
			ps.setString(5, data.getSource());
			return ps.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean remove(DataCIDR data) {
		PreparedStatement ps = null;
		try {
			ps = this.db().prepareStatement("DELETE FROM `gatekeeper_cidr` WHERE `cidr` = ? AND `type` = ?");
			ps.setString(1, data.getCidr());
			ps.setString(2, data.getType());
			return ps.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean remove(DataPlayer data) {
		PreparedStatement ps = null;
		try {
			ps = this.db().prepareStatement("DELETE FROM `gatekeeper_player` WHERE `player` = ? AND `type` = ?");
			ps.setString(1, data.getPlayer());
			ps.setString(2, data.getType());
			return ps.executeUpdate() != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Connection db() {
		FileConfiguration fc = GateKeeper.plugin.getConfig();
		try {
			return DriverManager.getConnection("jdbc:mysql://" + fc.getString("mysql.host") + ":" + Integer.toString(fc.getInt("mysql.port")) + "/" + fc.getString("mysql.database") + "?autoReconnect=true&user=" + fc.getString("mysql.username") + "&password=" + fc.getString("mysql.password"));
		} catch (SQLException ex) {
			ex.printStackTrace();
			GateKeeper.plugin.getServer().getPluginManager().disablePlugin(GateKeeper.plugin);
		}
		return null;
	}

	private static String getSQL(String table) {
		InputStream is = null;
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();

		try { 
			is = GateKeeper.class.getResourceAsStream("/sql/" + table);
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
		PreparedStatement ps = null;

		try {
			ps = this.db().prepareStatement("SELECT * FROM `gatekeeper_player` WHERE `player` = ?");
			ps.setString(1, player.getName());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs.next()) {
				data.add(this.makeDataPlayer(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			ps = this.db().prepareStatement("SELECT * FROM `gatekeeper_cidr` WHERE (`start` <= ? AND ? <= `end`)");
			long ip = Util.ip2long(player.getAddress().getAddress().getHostAddress().toString());
			ps.setLong(1, ip);
			ps.setLong(2, ip);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs.next()) {
				data.add(this.makeDataCIDR(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				result.getString(4)
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
				result.getLong(2),
				result.getLong(3),
				result.getString(4),
				result.getString(5),
				result.getString(6)
			);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}
