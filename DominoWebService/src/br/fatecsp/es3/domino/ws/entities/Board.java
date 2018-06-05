package br.fatecsp.es3.domino.ws.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Board {

	HashSet<Piece> player1Pieces = new HashSet<Piece>();
	HashSet<Piece> player2Pieces = new HashSet<Piece>();
	ArrayList<Piece> remainingPieces = new ArrayList<Piece>();
	int extremeA; //value of one of the extremes of the game
	int extremeB; //value of another of the extremes of the game
	
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
	 * @param player1Piece
	 * @param player2Pieces
	 */
	private void assignPieces(HashSet<Piece> player1Piece, HashSet<Piece> player2Pieces) {
		List<Piece> allPiecesList = generateBoardPieces();
		boolean[] usedPieceList = generateUsedPieceList();
		int i=0; 
		//PLAYER 1
		while(i<7) { //vamos sortear 7 pecas para o jogador 1
			Random random = new Random();
			int numeroSorteado = random.nextInt(28); //sorteia valores de 0 a 27
			if(!usedPieceList[numeroSorteado]){
				player1Piece.add(allPiecesList.get(numeroSorteado)); //adiciona peca sorteada à lista do player1
				usedPieceList[numeroSorteado]=true; //marca o numero sorteado como indisponivel
				i++; //increment number of pieces player 1 already have
			}
		}
		//PLAYER 2
		while(i<14) { //vamos sortear 7 pecas para o jogador2
			Random random = new Random();
			int numeroSorteado = random.nextInt(28); //sorteia valores de 0 a 27
			if(!usedPieceList[numeroSorteado]){
				player2Pieces.add(allPiecesList.get(numeroSorteado)); //adiciona peca sorteada à lista do player2
				usedPieceList[numeroSorteado]=true; //marca o numero sorteado como indisponivel
				i++; //increment number of pieces player 2 already have
			}
		}
		//Peças restantes vao para o monte - onde podem ser compradas.
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
}
