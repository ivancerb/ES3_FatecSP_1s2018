package br.fatecsp.es3.domino.ws.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.fatecsp.es3.domino.ws.entities.Game;

public class GameDataManager {
	public static boolean checkGameExistence(Game game) throws SQLException, ClassNotFoundException {
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql = "SELECT COUNT(*) FROM dominoeng3.dbo.partida WHERE ID = ? AND player1 = ? AND player2 = ?";
			
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, game.getId());
				ps.setInt(2, game.getPlayer1().getId());
				ps.setInt(3, game.getPlayer2().getId());

				try (ResultSet rs = ps.executeQuery();) {
					while (rs.next()) {
					    if (rs.getInt(1) == 1) {
					        rs.close();
					        return true;
					    }
					}
				}
				
				return false;
			}
		}
	}
}
