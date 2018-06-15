package br.fatecsp.es3.domino.ws.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.fatecsp.es3.domino.ws.entities.Player;

public class PlayerDataManager {

	public static boolean checkPlayerExistence(Player player) throws SQLException, ClassNotFoundException {
		try (Connection conn = MSSQLConnectionFactory.getConnection();) {
			String sql;
			if (player.getEmail() == null) {
				sql = "SELECT COUNT(*) FROM dominoeng3.dbo.player WHERE Nome = ? AND ID = ?";
			} else {
				sql = "SELECT COUNT(*) FROM dominoeng3.dbo.player WHERE Nome = ? AND ID = ? AND Email = ?";
			}

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, player.getName());
				ps.setInt(2, player.getId());

				if (player.getEmail() != null) {
				    ps.setString(3, player.getEmail());
				}

				System.out.println("SQL player: " + ps);
				
				try (ResultSet rs = ps.executeQuery();) {
					while (rs.next()) {
					    if (rs.getInt(1) == 1) {
					        return true;
					    }
					}
				}
				
				return false;
			}
		}
	}
}
