package it.polito.tdp.meteo.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		for(Rilevamento r: m.getUmiditaMedia(12)) {
    		System.out.print(r.toString());
    	}
		
		
		//m.trovaRilevamenti(3);
		
		for(Rilevamento r: m.trovaSequenza(11)) {
    		System.out.println(r.getLocalita()+" "+r.getData()+" "+r.getUmidita());
    	}
		System.out.println(""+m.getBestCosto());
		
		
		//System.out.println(m.getUmiditaMedia(12));
		
		//System.out.println(m.trovaSequenza(5));
		//System.out.print(m.getUmiditaMedia(12));

	}

}
