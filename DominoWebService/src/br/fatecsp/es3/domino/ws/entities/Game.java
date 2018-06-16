package br.fatecsp.es3.domino.ws.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Game {
	
	private int id;
	private Player player1;
	private Player player2;
	private Board  board; 
	private int lastOneToPlayId;
	private boolean isFirstMove = true;
	private Random randomGenerator;
	private int pieceDeadEnd = -1;
	private int pieceExtreme = -1;
	
	public Game(int id, Player player1, Player player2) {
		this.id = id;
		this.player1 = player1;
		this.player2 = player2;
		this.board = new Board();
		this.lastOneToPlayId = player1.getId();
		this.randomGenerator = new Random();
	}
	
	public int getPieceDeadEnd() {
		return this.pieceDeadEnd;
	}
	
	public int getPieceExtreme() {
		return this.pieceExtreme;
	}
	
	public boolean play(int playerId, String extremeSide, int pieceDeadEnd, int pieceExtreme) {
		if(!isFirstMove) {
			//only allows to play if the extreme matches - if first move this is not verified
			if(extremeSide.equals("A")) {
				if (pieceExtreme != board.getExtremeA()) {
					return false;
				}
			}
			if(extremeSide.equals("B")) {
				if (pieceExtreme != board.getExtremeB()) { 
					return false;
				}
			}
		}
		//remove the piece from the pieces' list
		this.removePiece(playerId, pieceDeadEnd, pieceExtreme);	
		this.setLastOneToPlayId(playerId);
		this.updateExtremes(extremeSide, pieceExtreme, pieceDeadEnd);
		this.pieceExtreme = pieceExtreme;
		this.pieceDeadEnd = pieceDeadEnd;
		return true;
	}
	
	public boolean canPlay(int playerId) {
		if(!isFirstMove)
		{
			int lastPlayer = this.getLastOneToPlayId();
			
			return lastPlayer != playerId;
		}
		
		return true;
	}
	
	public Piece buyPiece(int playerId) {
		if(this.board.remainingPieces.isEmpty()) return null;
		
		int position = randomGenerator.nextInt(this.board.remainingPieces.size());
		Piece piece = this.board.remainingPieces.get(position);
		this.board.remainingPieces.remove(position);
		
		if(playerId==this.player1.getId()) {
			this.board.player1Pieces.add(piece);
		}else if (playerId==this.player2.getId()) {
			this.board.player2Pieces.add(piece);
		}
		
		return piece;
	}
	
	public boolean isWinner(int playerId) {
		if(playerId == player1.getId()) {
			return this.board.player1Pieces.isEmpty();
		}
		else {
			return this.board.player2Pieces.isEmpty();
		}
	}
	
	private void removePieceFromHashSet(HashSet<Piece> pieces, Piece piece) {
		Iterator<Piece> iterator = pieces.iterator();
		while (iterator.hasNext()) {
			Piece p = iterator.next();
		    if (p.equals(piece)) {
		        iterator.remove();
		        break;
		      }
		}
	}
	
	private void removePiece(int playerId, int valueDeadEnd, int valueExtreme) {
		Piece piece = new Piece(valueDeadEnd, valueExtreme);
		if(playerId == player1.getId()) {
			this.removePieceFromHashSet(this.board.player1Pieces, piece);
		}
		else {
			this.removePieceFromHashSet(this.board.player2Pieces, piece);
		}
	}
	
	private void updateExtremes(String extremeSide, int valueExtreme, int valueDeadEnd) {
		if(this.isFirstMove) { 
			isFirstMove = false; 
			this.board.setExtremeA(valueExtreme);
			this.board.setExtremeB(valueDeadEnd);
		}
		else {
			if(extremeSide.equals("A")) {
				this.board.setExtremeA(valueExtreme);
			}else {
				this.board.setExtremeB(valueExtreme);
			}
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
	public boolean isFirstMove() {
		return isFirstMove;
	}
}
