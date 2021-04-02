package it.polito.tdp.corsi.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Studente;

public class CorsoDAO {//chiamata cosi perche query su corsi
	
	//pattern ORN:relazione tra programma e db
	public List<Corso> getCorsiByPeriodo(Integer periodo){
		
		String sql="SELECT * "
				 + "FROM corso "
				 + "WHERE pd=?";//metto ? al posto di 1 perche' e' un input dell'utente
	
		List<Corso> result = new ArrayList<Corso>();
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, periodo);//1=il primo parametro che c'e' nella query
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {
				Corso c=new Corso(rs.getString("codins"),rs.getInt("crediti"),rs.getString("nome"),rs.getInt("pd"));
				result.add(c);
			}
			rs.close();
			st.close();
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer periodo){
		String sql="SELECT c.codins, c.nome, c.crediti, c.pd,COUNT(*) AS tot "
				+ "FROM corso c,iscrizione i "
				+ "WHERE c.codins=i.codins AND c.pd=? "
				+ "GROUP BY c.codins,c.nome,c.crediti,c.pd ";
		Map<Corso,Integer> result = new HashMap<Corso,Integer>();
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, periodo);//1=il primo parametro che c'e' nella query
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {
				Corso c=new Corso(rs.getString("codins"),rs.getInt("crediti"),rs.getString("nome"),rs.getInt("pd"));
				Integer n=rs.getInt("tot");
				result.put(c, n);
			}
			rs.close();
			st.close();
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	public List<Studente> getStudentiByCorso(Corso corso){ //potevamo usare anche String codice
		
		String sql="SELECT s.matricola,s.cognome,s.nome, s.CDS "
				+ "FROM studente s, iscrizione i "
				+ "WHERE s.matricola=i.matricola AND i.codins=?";
		
		List<Studente> result=new LinkedList<Studente>();
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {			//rispetto ordine del cotruttore!!
				Studente s=new Studente(rs.getInt("matricola"),rs.getString("nome"),rs.getString("cognome"),rs.getString("cds"));
				result.add(s);
			}
			
			rs.close();
			st.close();
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public Map<String,Integer> getDivisioneCDS (Corso corso) {
		String sql="SELECT s.CDS, COUNT(*) AS tot "
				+ "FROM studente s,iscrizione i "
				+ "WHERE s.matricola=i.matricola AND i.codins='01OVYPG' AND s.CDS <> '' "
				+ "GROUP BY s.CDS";
		
		Map<String,Integer> result= new HashMap<String,Integer>();
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {			//rispetto ordine del cotruttore!!
				result.put(rs.getString("CDS"),rs.getInt("tot"));
			}
			
			rs.close();
			st.close();
			conn.close();
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
		
		
	}
	public boolean esisteCorso(Corso corso) {
		String sql="SELECT * FROM corso WHERE codins=?"; //abbiamo il dato specificanone la chiave primaria
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			ResultSet rs=st.executeQuery();
			
			if(rs.next()) {//vero se c'e' un prossimo risultato->al massimo ce ne sara 1 (rs punta a 0, se c'e' n'e' uno il next avra' qualcosa!
				rs.close();//REGOLA: chiudo tutto tutte le volte che c'e' un return!
				st.close();
				conn.close();
				return true;
			}
			else {
				rs.close();
				st.close();
				conn.close();
				return false;
			}
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
