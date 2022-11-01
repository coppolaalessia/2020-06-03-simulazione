package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getVertici(int goal){
		String sql = "SELECT distinct p.PlayerID AS id, p.Name AS nome "
				+ "FROM players as p, actions AS a "
				+ "WHERE a.PlayerID = p.PlayerID "
				+ "GROUP BY P.PlayerID, P.Name "
				+ "HAVING AVG(a.Goals) > ?";
		
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, goal);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("id"), res.getString("nome"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenza(int goal){
		String sql = "SELECT  p1.PlayerID, p1.Name, p2.PlayerID, p2.Name,(SUM(a1.TimePlayed)-SUM(a2.TimePlayed)) AS delta1_2 "
				+ "FROM actions a1, actions a2 "
				+ "WHERE 	a1.TeamID!= a2.TeamID AND "
				+ "			a1.MatchID=a2.MatchID AND "
				+ "			a1.`Starts`=1 AND a2.`Starts`=1 "
				+ "GROUP BY a1.PlayerID, a2.PlayerID";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player p1 =new Player(res.getInt("p1.PlayerID"), res.getString("p1.Name"));
				Player p2= new Player(res.getInt("p2.PlayerID"),res.getString("p2.Name"));
				int delta= res.getInt("delta1_2");
				
				if(delta!=0) {
					Adiacenza adiacenza = new Adiacenza(p1,p2,delta);
					result.add(adiacenza);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
