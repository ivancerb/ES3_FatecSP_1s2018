package br.fatecsp.es3.domino.ws.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Board {

	HashSet<Piece> player1Pieces = new HashSet<Piece>();
	HashSet<Piece> player2Pieces = new HashSet<Piece>();
	ArrayList<Piece> remainingPieces = new ArrayList<Piece>();
	int extremeA = -1; //value of one of the extremes of the game
	int extremeB = -1; //value of another of the extremes of the game
	
	public Board() {
		assignPieces(player1Pieces, player2Pieces);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + extremeA;
		result = prime * result + extremeB;
		result = prime * result + ((player1Pieces == null) ? 0 : player1Pieces.hashCode());
		result = prime * result + ((player2Pieces == null) ? 0 : player2Pieces.hashCode());
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
		Board other = (Board) obj;
		if (extremeA != other.extremeA)
			return false;
		if (extremeB != other.extremeB)
			return false;
		if (player1Pieces == null) {
			if (other.player1Pieces != null)
				return false;
		} else if (!player1Pieces.equals(other.player1Pieces))
			return false;
		if (player2Pieces == null) {
			if (other.player2Pieces != null)
				return false;
		} else if (!player2Pieces.equals(other.player2Pieces))
			return false;
		return true;
	}

	public HashSet<Piece> getPlayer1Pieces() {
		return player1Pieces;
	}
	public void setPlayer1Pieces(HashSet<Piece> player1Pieces) {
		this.player1Pieces = player1Pieces;
	}
	public HashSet<Piece> getPlayer2Pieces() {
		return player2Pieces;
	}
	public void setPlayer2Pieces(HashSet<Piece> player2Pieces) {
		this.player2Pieces = player2Pieces;
	}
	public int getExtremeA() {
		return extremeA;
	}
	public void setExtremeA(int extremeA) {
		this.extremeA = extremeA;
	}
	public int getExtremeB() {
		return extremeB;
	}
	public void setExtremeB(int extremeB) {
		this.extremeB = extremeB;
	}
	
	/**
	 * Sorteia as pecas entre os players 1 e 2, sendo 14 pra cada
	 * @param player1Pieces
	 * @param player2Pieces
	 */
	private void assignPieces(HashSet<Piece> player1Pieces, HashSet<Piece> player2Pieces) {
		List<Piece> allPiecesList = generateBoardPieces();
		boolean[] usedPieceList = generateUsedPieceList();
		int i=0;
		Random random = new Random();
		
		//PLAYER 1
		while(i<7) { //vamos sortear 7 pecas para o jogador 1
			int numeroSorteado = random.nextInt(28); //sorteia valores de 0 a 27
			if(!usedPieceList[numeroSorteado]){
				player1Pieces.add(allPiecesList.get(numeroSorteado)); //adiciona peca sorteada Ã  lista do player1
				usedPieceList[numeroSorteado]=true; //marca o numero sorteado como indisponivel
				i++; //increment number of pieces player 1 already have
			}
		}
		//PLAYER 2
		while(i<14) { //vamos sortear 7 pecas para o jogador2
			int numeroSorteado = random.nextInt(28); //sorteia valores de 0 a 27
			if(!usedPieceList[numeroSorteado]){
				player2Pieces.add(allPiecesList.get(numeroSorteado)); //adiciona peca sorteada Ã  lista do player2
				usedPieceList[numeroSorteado]=true; //marca o numero sorteado como indisponivel
				i++; //increment number of pieces player 2 already have
			}
		}
		//PeÃ§as restantes vao para o monte - onde podem ser compradas.
		for(int j=0;j<28;j++){ //vamos atribuir as 14 pecas restantes para o monte
			if(!usedPieceList[j]) {
				this.remainingPieces.add(allPiecesList.get(j));
			}
		}
	}
	
	/**
	 * Generate all board domino pieces
	 * @return
	 */
	private List<Piece> generateBoardPieces(){
		List<Piece> allPiecesList = new ArrayList<Piece>();
		for(int i=0; i<=6; i++) {
			for(int j=0; j<=6; j++) {
				Piece piece = new Piece(i,j);
				allPiecesList.add(piece);
			}
		}
		return allPiecesList;
	}
	
	private boolean[] generateUsedPieceList() {
		boolean[] usedPiecesList = new boolean[28];
		for(int i=0;i<28;i++) { usedPiecesList[i]=false; }
		return usedPiecesList;
	}
	
	/**
	 * @param player Vale 1 para o player1 e 2 para o player2
	 * @return Pega a peça de valores iguais e que tenha o maior valor para um jogador!
	 * 	
	 */
	public int getMaxEqualValuePiece(int player) {
		int maxPlayer = -1;
		if (player == 1) {
			for (Piece p : player1Pieces)
			{
				if (p.faceA == p.faceB && p.faceA > maxPlayer) {
					maxPlayer = p.faceA;
				}
			}
		}
		else if (player == 2) {
			for (Piece p : player2Pieces)
			{
				if (p.faceA == p.faceB && p.faceA > maxPlayer) {
					maxPlayer = p.faceA;
				}
			}
		}
		
		return maxPlayer;
	}
	
	/**
	 * @param player Vale 1 para o player1 e 2 para o player2
	 * @return Pega a soma dos pontos da peça de maior valor para um jogador!
	 * 	
	 */
	public int getMaxSummedValuePiece(int player) {
		int maxPlayer = -1;
		if (player == 1) {
			for (Piece p : player1Pieces)
			{
				if (p.faceA + p.faceB > maxPlayer) {
					maxPlayer = p.faceA + p.faceB;
				}
			}
		}
		else if (player == 2) {
			for (Piece p : player2Pieces)
			{
				if (p.faceA + p.faceB > maxPlayer) {
					maxPlayer = p.faceA + p.faceB;
				}
			}
		}
		
		return maxPlayer;
	}
	
	public boolean canPlayer1PlayAtFirst() {
		int equal1 = this.getMaxEqualValuePiece(1);
		int equal2 = this.getMaxEqualValuePiece(2);
		
		if (equal1 > -1 || equal2 > -1) {
			return  equal1 > equal2;
		} else {
			int unsame1 = this.getMaxSummedValuePiece(1);
			int unsame2 = this.getMaxSummedValuePiece(2);
			return unsame1 > unsame2;
		}
		 
	}
	
	public boolean canPlayerPlayNow(int player) {
		if (this.extremeA == -1) {
			return true;
		}
		
		if (player == 1) {
			for (Piece p : player1Pieces)
			{
				if( Arrays.asList(this.extremeA,this.extremeB).contains(p.faceA) ||
						Arrays.asList(this.extremeA,this.extremeB).contains(p.faceB) ) {
					return true;
				}
			}			
		}
		else if (player == 2) {
			for (Piece p : player2Pieces)
			{
				if( Arrays.asList(this.extremeA,this.extremeB).contains(p.faceA) ||
						Arrays.asList(this.extremeA,this.extremeB).contains(p.faceB) ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @param player Vale 1 para o player1 e 2 para o player2
	 * @return Verifica se um jogador consegue jogar a rodada atual!
	 * 	
	 */
	public boolean canPlayerPlayNowBuying(int player) {
		if (this.canPlayerPlayNow(player)) {
			return true;
		}
		
		for (Piece p : remainingPieces)
		{
			if( Arrays.asList(this.extremeA,this.extremeB).contains(p.faceA) ||
					Arrays.asList(this.extremeA,this.extremeB).contains(p.faceB) ) {
				return true;
			}
		}
		
		return false;
	}
}
