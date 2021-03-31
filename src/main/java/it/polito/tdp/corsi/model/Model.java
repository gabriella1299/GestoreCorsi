package it.polito.tdp.corsi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.DB.CorsoDAO;

public class Model {
	
	private CorsoDAO corsoDao;
	
	public Model() {
		corsoDao=new CorsoDAO();
	}
	
	public List<Corso> getCorsiByPeriodo(Integer pd){
		return corsoDao.getCorsiByPeriodo(pd);
	}
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer periodo){
		return corsoDao.getIscrittiByPeriodo(periodo);
	}
	
	public List<Studente> getStudentiByCorso(String codice){
		return corsoDao.getStudentiByCorso(new Corso(codice, null, null, null));
	}
	/*			//classe temporanea con due attributi: una stringa e un intero
	public List<DivisioneCDS> getDivisioneCDS (String codice) {
		//dato il corso con codice ABC
		//GEST->50
		//INF->40
		//MEC->30
	}*/
	public Map<String,Integer> getDivisioneCDS (String codice) {
		
		//SOLUZIONE 1-->interna
		/*Map<String,Integer> divisione=new HashMap<String,Integer>();
		List<Studente> studenti=this.getStudentiByCorso(codice);
		
		for(Studente s:studenti) {
			if(s.getCds()!=null && !s.getCds().equals("")) {
				if(divisione.get(s.getCds())==null) { //non e' ancora presente l'insegnamento
					divisione.put(s.getCds(), 1);
				} else {
					divisione.put(s.getCds(), divisione.get(s.getCds())+1); //l'insegnamento e' gia presente: sovrascrivo il valore
				}
			}
		}
		return divisione;
		*/
		
		//SOLUZIONE 2-->passo direttamente dal db, chiedo a lui di darmi la divisione
		return corsoDao.getDivisioneCDS(new Corso(codice, null, null, null)); //per usare il parametro corso nei metodi
	
	}
	
	
	public boolean esisteCorso(String codice) {
		//approccio topdown--> vado in corsoDao
		return corsoDao.esisteCorso(new Corso(codice, null, null, null));
	}
}
