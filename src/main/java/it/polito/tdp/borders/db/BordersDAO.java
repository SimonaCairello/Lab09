package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> stati) {
		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				stati.put(rs.getInt("ccode"),
						new Country(rs.getString("StateAbb"), rs.getInt("ccode"), rs.getString("StateNme")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> states) {
		String sql = "SELECT state1no, state2no, year " + "FROM contiguity " + "WHERE YEAR<=? AND conttype=1";
		List<Border> coppie = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				coppie.add(new Border(states.get(rs.getInt("state1no")), states.get(rs.getInt("state2no")),
						rs.getInt("year")));
			}

			conn.close();
			return coppie;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
