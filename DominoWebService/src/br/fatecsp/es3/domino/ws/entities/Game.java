package br.fatecsp.es3.domino.ws.entities;

public class Game {
	
	int id;
	Player player1;
	Player player2;
	Board  board; 
	int lastOneToPlayId;
	
	public Game(int id, Player player1, Player player2) {
		this.id = id;
		this.player1 = player1;
		this.player2 = player2;
		this.board = new Board();
		this.lastOneToPlayId = player1.getId();
	}
		
	public boolean play(int playerId, String extremeSide, int pieceDeadEnd, int pieceExtreme) {
		this.removePiece(playerId, pieceDeadEnd, pieceExtreme);
		this.setLastOneToPlayId(playerId);
		this.updateExtreme(extremeSide, pieceExtreme);
		return isWinner(playerId);
	}
	
	//TODO complement with other criteria and logic
	public boolean isWinner(int playerId) {
		if(playerId == player1.getId()) {
			return this.board.player1Pieces.isEmpty();
		}
		else {
			return this.board.player2Pieces.isEmpty();
		}
	}
	
	private void removePiece(int playerId, int valueDeadEnd, int valueExtreme) {
		Piece piece = new Piece(valueDeadEnd, valueExtreme);
		if(playerId == player1.getId()) {
			this.board.player1Pieces.remove(piece);
		}
		else {
			this.board.player2Pieces.remove(piece);
		}
	}
	
	private void updateExtreme(String extremeSide, int valueExtreme) {
		if(extremeSide.equals("A")) {
			this.board.setExtremeA(valueExtreme);
		}else {
			this.board.setExtremeB(valueExtreme);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + id;
		result = prime * result + lastOneToPlayId;
		result = prime * result + ((player1 == null) ? 0 : player1.hashCode());
		result = prime * result + ((player2 == null) ? 0 : player2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (id != other.id)
			return false;
		if (lastOneToPlayId != other.lastOneToPlayId)
			return false;
		if (player1 == null) {
			if (other.player1 != null)
				return false;
		} else if (!player1.equals(other.player1))
			return false;
		if (player2 == null) {
			if (other.player2 != null)
				return false;
		} else if (!player2.equals(other.player2))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Player getPlayer1() {
		return player1;
	}
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public int getLastOneToPlayId() {
		return lastOneToPlayId;
	}
	public void setLastOneToPlayId(int lastOneToPlayId) {
		this.lastOneToPlayId = lastOneToPlayId;
	} 	
}
