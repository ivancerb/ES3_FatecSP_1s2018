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
	private String lastExtremeSide;
	
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
	
	public String getLastExtremeSide()
	{
		return this.lastExtremeSide;
	}
	
	public int getNumPieceEnemy(int playerId) {
		if (playerId == this.player1.getId()) {
			return this.board.getPlayer2Pieces().size();
		} else if (playerId == this.player2.getId()) {
			return this.board.getPlayer1Pieces().size();
		}
		
		return 0;
	}
	
	public boolean play(int playerId, String extremeSide, int pieceDeadEnd, int pieceExtreme) {
		if(!isFirstMove) {
			//only allows to play if the extreme matches - if first move this is not verified
			if(extremeSide.equals("A")) {
				if (pieceDeadEnd != board.getExtremeA()) {
					return false;
				}
			}
			if(extremeSide.equals("B")) {
				if (pieceDeadEnd != board.getExtremeB()) { 
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
	
	public boolean canPlayBuying(int playerId) {
		if(!isFirstMove) {
			int lastPlayer = this.getLastOneToPlayId();
			
			if (lastPlayer != playerId) {
				if (playerId == this.player1.id) {
					return this.board.canPlayerPlayNowBuying(1);
				} else if (playerId == this.player2.id) {
					return this.board.canPlayerPlayNowBuying(2);
				}
			} else {
				if (playerId == this.player1.id) {
					return !this.board.canPlayerPlayNowBuying(2) &&
							this.board.canPlayerPlayNowBuying(1);
				} else if (playerId == this.player2.id) {
					return !this.board.canPlayerPlayNowBuying(1) &&
							this.board.canPlayerPlayNowBuying(2);
				}
			}
			
			return false;
		} else {
			boolean player1CanPlay = this.board.canPlayer1PlayAtFirst();
			
			if (player1CanPlay) {
				return playerId == this.player1.id;
			}
			else
			{
				return playerId == this.player2.id;
			}
		}
	}
	
	public boolean canPlay(int playerId) {
		if(!isFirstMove) {
			if (playerId == this.player1.id) {
				return this.board.canPlayerPlayNow(1);
			} else if (playerId == this.player2.id) {
				return this.board.canPlayerPlayNow(2);
			}
			
			return false;
		} else {
			boolean player1CanPlay = this.board.canPlayer1PlayAtFirst();
			
			if (player1CanPlay) {
				return playerId == this.player1.id;
			}
			else
			{
				return playerId == this.player2.id;
			}
		}
	}
	
	public boolean canPlay(int playerId, int pieceDeadEnd, int pieceExtreme) {
		Piece piece = new Piece(pieceDeadEnd, pieceExtreme);
		return this.canPlay(playerId, piece);
	}
	
	private boolean canPlay(int playerId, Piece piece) {
		if(!isFirstMove) {
			return this.canPlayBuying(playerId);
		} else {
			if (canPlayBuying(playerId)) {
				if (playerId == this.player1.id) {
					int equal = this.board.getMaxEqualValuePiece(1);
					if (equal > -1) {
						return this.board.getMaxEqualValuePiece(1) == piece.faceA;
					} else {
						return this.board.getMaxSummedValuePiece(1) == piece.faceA + piece.faceB;
					}
				} else if (playerId == this.player2.id) {
					int equal = this.board.getMaxEqualValuePiece(2);
					if (equal > -1) {
						return this.board.getMaxEqualValuePiece(2) == piece.faceA;
					} else {
						return this.board.getMaxSummedValuePiece(2) == piece.faceA + piece.faceB;
					}
				}
			}
			
			return false;
		}
	}
	
	public Piece buyPiece(int playerId) {
		if(this.board.remainingPieces.isEmpty()) {
			return null;
		}
		
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
		if (!this.board.canPlayerPlayNowBuying(1) &&
				!this.board.canPlayerPlayNowBuying(2)) {
			if(playerId == player1.getId()) {
				return this.board.player1Pieces.size() < this.board.player2Pieces.size();
			}
			else {
				return this.board.player1Pieces.size() > this.board.player2Pieces.size();
			}
		} else {
			if(playerId == player1.getId()) {
				return this.board.player1Pieces.isEmpty();
			}
			else {
				return this.board.player2Pieces.isEmpty();
			}
		}
	}
	
	private boolean removePieceFromHashSet(HashSet<Piece> pieces, Piece piece) {
		Iterator<Piece> iterator = pieces.iterator();
		while (iterator.hasNext()) {
			Piece p = iterator.next();
		    if (p.equals(piece)) {
		        iterator.remove();
		        return true;
		      }
		}
		return false;
	}
	
	public Piece getLastPlayedPiece() {
		Piece piece = new Piece(this.pieceDeadEnd, this.pieceExtreme);
		return piece;
	}
 	
	private boolean removePiece(int playerId, int valueDeadEnd, int valueExtreme) {
		Piece piece = new Piece(valueDeadEnd, valueExtreme);
		if(playerId == player1.getId()) {
			return this.removePieceFromHashSet(this.board.player1Pieces, piece);
		}
		else if(playerId == player2.getId()) {
			return this.removePieceFromHashSet(this.board.player2Pieces, piece);
		}
		return false;
	}
	
	private void updateExtremes(String extremeSide, int valueExtreme, int valueDeadEnd) {
		if(this.isFirstMove) { 
			isFirstMove = false; 
			this.board.setExtremeA(valueExtreme);
			this.board.setExtremeB(valueDeadEnd);
			this.lastExtremeSide = "AB";
		}
		else {
			if(extremeSide.equals("A")) {
				this.board.setExtremeA(valueExtreme);
				this.lastExtremeSide = "A";
			}else {
				this.board.setExtremeB(valueExtreme);
				this.lastExtremeSide = "B";
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
