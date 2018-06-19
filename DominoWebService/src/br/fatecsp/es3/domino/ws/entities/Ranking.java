package br.fatecsp.es3.domino.ws.entities;

public class Ranking {
	int player;
	int vitorias;
	int partidas_jogadas;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + partidas_jogadas;
		result = prime * result + player;
		result = prime * result + vitorias;
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
		Ranking other = (Ranking) obj;
		if (partidas_jogadas != other.partidas_jogadas)
			return false;
		if (player != other.player)
			return false;
		if (vitorias != other.vitorias)
			return false;
		return true;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public int getVitorias() {
		return vitorias;
	}
	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}
	public int getPartidas_jogadas() {
		return partidas_jogadas;
	}
	public void setPartidas_jogadas(int partidas_jogadas) {
		this.partidas_jogadas = partidas_jogadas;
	}
}
