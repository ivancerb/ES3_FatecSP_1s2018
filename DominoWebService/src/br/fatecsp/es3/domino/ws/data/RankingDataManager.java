package br.fatecsp.es3.domino.ws.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.fatecsp.es3.domino.ws.entities.Game;
import br.fatecsp.es3.domino.ws.entities.Ranking;

public class RankingDataManager {
	public static List<Ranking> getAllRankings() throws SQLException, ClassNotFoundException {
		List<Ranking> list = new ArrayList<Ranking>();
		
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql = "SELECT ID, player, partidas_jogadas, vitórias\n" +
					"FROM dominoeng3.dbo.ranking ORDER BY vitórias DESC, partidas_jogadas ASC;";
			
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				
				try (ResultSet rs = ps.executeQuery();) {
					while (rs.next()) {
						Ranking r = new Ranking();
						r.setPlayer(rs.getInt(2));
						r.setPartidas_jogadas(rs.getInt(3));
						r.setVitorias(rs.getInt(4));
						list.add(r);
					}
				}
			}
		}
		
		return list;
	}
	
	public static void insertRanking(Ranking r) throws SQLException, ClassNotFoundException {
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql = "INSERT INTO dominoeng3.dbo.ranking\r\n" + 
					"(player, partidas_jogadas, vitórias)\r\n" + 
					"VALUES(?, ?, ?);";
			
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, r.getPlayer());
				ps.setInt(2, r.getPartidas_jogadas());
				ps.setInt(3, r.getVitorias());
				ps.executeUpdate();
			}
		}
	}
	
	public static void updateRanking(Ranking r) throws SQLException, ClassNotFoundException {
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql = "UPDATE dominoeng3.dbo.ranking\r\n" + 
					"SET partidas_jogadas=?, vitórias=?\r\n" + 
					"WHERE player=?;\r\n";
			
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, r.getPartidas_jogadas());
				ps.setInt(2, r.getVitorias());
				ps.setInt(3, r.getPlayer());
				ps.executeUpdate();
			}
		}
	}
	
	public static boolean checkRanking(int idPlayer) throws SQLException, ClassNotFoundException {
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql = "SELECT COUNT(*) FROM dominoeng3.dbo.ranking WHERE player = ?;";
			
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, idPlayer);
				
				try (ResultSet rs = ps.executeQuery();) {
					while (rs.next()) {
						if (rs.getInt(1) == 1) {
					        return true;
					    }
					}
				}
			}
		}
		
		return false;
	}
	
}
