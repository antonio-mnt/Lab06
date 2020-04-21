package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	private List<Rilevamento> allRilevamenti = new ArrayList<>();
	private List<String> allLocalita = new ArrayList<>();
	
	public MeteoDAO() {
		allRilevamenti = new ArrayList<Rilevamento>(riempiAllRilevamenti());
		allLocalita = new ArrayList<String>(riempiAllLocalita());
	}
	
	public List<Rilevamento> getAllRilevamenti() {
		return allRilevamenti;
	}

	public List<String> getAllLocalita() {
		return allLocalita;
	}

	private List<Rilevamento> riempiAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		//List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				allRilevamenti.add(r);
			}

			conn.close();
			return allRilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private List<String> riempiAllLocalita() {

		final String sql = "SELECT distinct Localita FROM situazione";

		//List<String> localita = new ArrayList<String>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				allLocalita.add(rs.getString("Localita"));
			}

			conn.close();
			return allLocalita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese) {
		
		int umidita = 0;
		int somma;
		int n;
		Date data = null;
		
		List<Rilevamento> tempR = new ArrayList<>();
		
		for(String loc: allLocalita) {
			somma = 0;
			n = 0;
			for(Rilevamento r: allRilevamenti) {
				if(r.getLocalita().equals(loc) && r.getData().getMonth()==mese-1) {
					somma += r.getUmidita();
					n++;
				}
			}
			umidita = somma/n;
			tempR.add(new Rilevamento(loc,data,umidita));
		}
		
		return tempR;
	}


}
