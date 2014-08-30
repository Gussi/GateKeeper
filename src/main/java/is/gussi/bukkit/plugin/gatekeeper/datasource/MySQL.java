package is.gussi.bukkit.plugin.gatekeeper.datasource;

import is.gussi.bukkit.plugin.gatekeeper.Data;
import is.gussi.bukkit.plugin.gatekeeper.Datasource;
import is.gussi.bukkit.plugin.gatekeeper.GateKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MySQL implements Datasource {

	public MySQL() {
		try {
			this.db().prepareStatement(MySQL.getSQL("gatekeeper.sql")).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * Attempt for sane SQL insertion
	 * @param sql			SQL Query
	 * @param params		Bound parameters
	 * @return				TRUE on success, FALSE otherwise
	 */
	private boolean preparedStatement(String sql, String...params) {
		PreparedStatement ps = null;
		try {
			ps = this.db().prepareStatement(sql);
			int n = 0;
			for (String param : params) {
				ps.setString(++n, param);
			}
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

	public boolean add(Data data) {
		String sql	= "REPLACE INTO `gatekeeper_player` ("
					+ "		`type`,"
					+ "		`content_type`,"
					+ "		`content`,"
					+ "		`source`"
					+ ") VALUES (?, ?, ?, ?)";
		return this.preparedStatement(sql
			, data.getType().name().toLowerCase()
			, data.getContentType().name().toLowerCase()
			, data.getContent()
			, data.getSource()
		);
	}

	public boolean remove(Data data) {
		String sql	= "DELETE FROM `gatekeeper`"
					+ "WHERE `type` = ?"
					+ "AND `content_type` = ?"
					+ "AND `content ` = ?";
		return this.preparedStatement(sql
			, data.getType().name().toLowerCase()
			, data.getContentType().name().toLowerCase()
			, data.getContent()
			, data.getSource()
		);
	}

	public ArrayList<Data> check(Player player) {
		// TODO Auto-generated method stub
		return null;
	}
}
