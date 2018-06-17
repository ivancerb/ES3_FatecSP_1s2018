package br.fatecsp.es3.domino.ws.entities;

public class Piece {
	int faceA;
	int faceB;
	
	public Piece() {
		
	}
	
	public Piece(int valueFaceA, int valueFaceB) {
		this.faceA = valueFaceA;
		this.faceB = valueFaceB;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + faceA;
		result = prime * result + faceB;
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
		Piece other = (Piece) obj;
		if (faceA != other.faceA || faceB != other.faceB) {
			return faceA == other.faceB && faceB == other.faceA;
		}
			
		return true;
	}

	public int getFaceA() {
		return faceA;
	}
	public void setFaceA(int faceA) {
		this.faceA = faceA;
	}
	public int getFaceB() {
		return faceB;
	}
	public void setFaceB(int faceB) {
		this.faceB = faceB;
	}
}
