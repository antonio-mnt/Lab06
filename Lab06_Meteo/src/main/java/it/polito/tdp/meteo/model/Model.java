package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO mdao;
	private List<Rilevamento> rilevamenti;
	private List<Rilevamento> soluzione ;
	private int bestCosto;

	public Model() {
		mdao = new MeteoDAO();
		bestCosto = 1000000000;
		soluzione = new ArrayList<>();
		rilevamenti = new ArrayList<>();
	}
	
	public void trovaRilevamenti(int mese) {
		
		rilevamenti = new ArrayList<>();
		for (String loc: mdao.getAllLocalita()) {
			for(Rilevamento r: mdao.getAllRilevamenti()) {
				if(r.getLocalita().equals(loc) && r.getData().getMonth()==mese-1 && r.getData().getDate()>=1 && r.getData().getDate()<=NUMERO_GIORNI_TOTALI) {
					rilevamenti.add(r);
					//System.out.println(r.getLocalita()+" "+r.getData()+" "+r.getUmidita());
				}
			}
			
		}
		
	}
	


	// of course you can change the String output with what you think works best
	public List<Rilevamento> getUmiditaMedia(int mese) {
		return mdao.getAllRilevamentiLocalitaMese(mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		
		trovaRilevamenti(mese);
		soluzione = new ArrayList<>();
		bestCosto = 1000000000;
		
		List<Rilevamento> parziale = new ArrayList<>();
		
		cerca(parziale,1);
		
		
		return soluzione;
	}

	private void cerca(List<Rilevamento> parziale, int livello) {
		
		if(calcolaGiorni(parziale)==false) {
			return;
		}
		
		if(calcolagiorniMin(parziale)==false) {
			return;
		}
		
		
		if(parziale.size() == NUMERO_GIORNI_TOTALI) {
			int costo = calcolaCosto(parziale);
			if(costo < bestCosto) {
				soluzione = new ArrayList<>(parziale);
				bestCosto = costo;
			}
			
		}
		
		
		List<Rilevamento> sottoProblema = get(livello);
		
		for(Rilevamento r: sottoProblema) {
			parziale.add(r);
			cerca(parziale,livello+1);
			parziale.remove(parziale.size()-1);
		}
		
		
	}
	
	public List<Rilevamento> get(int livello){
		
		List<Rilevamento> sottoProblema = new ArrayList<>();
		
		for(Rilevamento r: rilevamenti) {
			if(r.getData().getDate()==livello) {
				sottoProblema.add(r);
			}
		}
		
		return sottoProblema;
	}
	
	private int calcolaCosto(List<Rilevamento> parziale) {
		
		String loc = parziale.get(0).getLocalita();
		boolean x = false;
		int costo = 0;
		
		for(int i = 0; i<parziale.size(); i++) {
			costo += parziale.get(i).getUmidita(); 
			if(x == false) {
				if(parziale.get(i).getLocalita().equals(loc)) {
				}else {
					x = true;
				}
			}
			if(x == true) {
				loc = parziale.get(i).getLocalita();
				costo += 100;
				x = false;
			}
		}
		
		return costo;
	}

	private boolean calcolaGiorni(List<Rilevamento> parziale) {
		
		int count = 0;
		
		for(String loc: mdao.getAllLocalita()) {
			count = 0;
			for(Rilevamento r: parziale) {
				if(r.getLocalita().equals(loc)) {
					count++;
				}
			}
			if(count>6) {
				return false;
			}
		}
		
		return true;
	}

	public boolean calcolagiorniMin(List<Rilevamento> parziale) {
		
		String loc = "";
		int count = 0;
		boolean x = true;
		
		for(int i = 0; i<parziale.size(); i++) {
			if(x==false) {
				if(parziale.get(i).getLocalita().equals(loc)) {
					count++;
				}else {
					x = true;
					if(count<3) {
						return false;
					}
				}
			}
			if(x == true) {
				loc = parziale.get(i).getLocalita();
				count = 1;
				x = false;
			}
		}
		if(parziale.size()==NUMERO_GIORNI_TOTALI && count<3) {
			return false;
		}
		
		
		return true;
	}
	
	public int getBestCosto() {
		return bestCosto;
	}
	

}
