package br.fatecsp.es3.domino.ws.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.fatecsp.es3.domino.ws.data.GameDataManager;
import br.fatecsp.es3.domino.ws.data.PlayerDataManager;
import br.fatecsp.es3.domino.ws.data.RankingDataManager;
import br.fatecsp.es3.domino.ws.entities.Game;
import br.fatecsp.es3.domino.ws.entities.Piece;
import br.fatecsp.es3.domino.ws.entities.Player;
import br.fatecsp.es3.domino.ws.entities.Ranking;

@RestController
@RequestMapping("/domino")
public class DominoController {
	
	private static Map<Integer,Player> playersMap = new HashMap<Integer,Player>();
	private static Map<Integer,Game> gamesMap = new HashMap<Integer,Game>();
	private static Map<Integer,Player> freePlayersMap= new HashMap<Integer,Player>();
	private ObjectMapper objectMapper = new ObjectMapper();
	private String json = "";
	
	/**
	 * @param player player object
	 */
	private void doConnectRoutine(Player player, int id) {
		playersMap.put(id, player);
		freePlayersMap.put(id, player);
	}
	
	/**
	 * @param playerName the player name
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@CrossOrigin
	@RequestMapping("/connect/{id-player}/{name-player}")
	public @ResponseBody boolean connectUserToGame(
			@PathVariable("id-player") int playerId,
			@PathVariable("name-player") String playerName,
			HttpServletRequest request){
		try {
			Player player = new Player(playerId, playerName);
			if (!PlayerDataManager.checkPlayerExistence(player))
				return false;
			this.doConnectRoutine(player, playerId);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @param playerName the player name
	 * @param playerEmail the player email address
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@CrossOrigin
	@RequestMapping("/connect/{id-player}/{email-player}/{name-player}")
	public @ResponseBody boolean connectUserToGame(
			@PathVariable("id-player") int playerId,
			@PathVariable("email-player") String playerEmail,
			@PathVariable("name-player") String playerName,
			HttpServletRequest request){try {
				Player player = new Player(playerId, playerName, playerEmail);
				if (!PlayerDataManager.checkPlayerExistence(player))
					return false;
				this.doConnectRoutine(player, playerId);
				return true;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	
	/**
	 * 
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/get-free-players")
	public @ResponseBody String getFreePlayers(HttpServletRequest request){
		try {
			json = objectMapper.writeValueAsString(freePlayersMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * @param player1
	 * @param player2
	 * @param request
	 * @return the game's id, that will be necessary for all the game transactions
	 */
	@CrossOrigin
	@RequestMapping("/newgame/{id-game}/{id-player1}/{id-player2}")
	public @ResponseBody boolean startNewGame(
			@PathVariable("id-game") int gameId,
			@PathVariable("id-player1") int player1, 
			@PathVariable("id-player2") int player2,
			HttpServletRequest request){
		try {
			Game game = new Game(gameId, playersMap.get(player1), playersMap.get(player2));
			if (!GameDataManager.checkGameExistence(game))
				return false;
			gamesMap.put(game.getId(), game);
			freePlayersMap.remove(player1);
			freePlayersMap.remove(player2);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * @param player1
	 * @param player2
	 * @param request
	 * @return the game's id, that will be necessary for all the game transactions
	 */
	@CrossOrigin
	@RequestMapping("/newgame/{id-player1}/{id-player2}")
	public @ResponseBody boolean startNewGame(
			@PathVariable("id-player1") int player1, 
			@PathVariable("id-player2") int player2,
			HttpServletRequest request){
		try {
			Game game = new Game(playersMap.get(player1), playersMap.get(player2));
			if (!GameDataManager.checkGameExistence(game))
				return false;
			gamesMap.put(game.getId(), game);
			freePlayersMap.remove(player1);
			freePlayersMap.remove(player2);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@CrossOrigin
	@RequestMapping("/endgame/{id-game}")
	public @ResponseBody boolean endGame(
			@PathVariable("id-game") int gameId,
			HttpServletRequest request){
		try {
			Game game = gamesMap.get(gameId);
			
			if (game == null) {
				return false;
			}
			
			Player player1 = game.getPlayer1();
			Player player2 = game.getPlayer2();
			gamesMap.remove(game.getId());
			freePlayersMap.put(player1.getId(), player1);
			freePlayersMap.put(player2.getId(), player2);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @param player1
	 * @param player2
	 * @param request
	 */
	@CrossOrigin
	@RequestMapping("/get-pieces/{id-game}/{id-player}")
	public @ResponseBody String getPieces(@PathVariable("id-game") int gameId, @PathVariable("id-player") int playerId, 
			HttpServletRequest request){
		HashSet<Piece> playerPieces = new HashSet<Piece>();
		Game game = gamesMap.get(gameId);
		
		if (game != null) {
			if(playerId == game.getPlayer1().getId()) {
				playerPieces = game.getBoard().getPlayer1Pieces();
			}else if(playerId == game.getPlayer2().getId()) {
				playerPieces = game.getBoard().getPlayer2Pieces();
			}
		}
		
		try {
			json = objectMapper.writeValueAsString(playerPieces);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * @param request
	 */
	@CrossOrigin
	@RequestMapping("/get-last-played-piece/{id-game}")
	public @ResponseBody String getLastPlayedPiece(@PathVariable("id-game") int gameId, 
			HttpServletRequest request){
		Piece piece;
		Game game = gamesMap.get(gameId);
		
		if (game != null) {
			piece = game.getLastPlayedPiece();
		} else {
			piece = new Piece(-1, -1);
		}
		
		try {
			json = objectMapper.writeValueAsString(piece);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * @param request
	 */
	@CrossOrigin
	@RequestMapping("/get-last-extreme-side/{id-game}")
	public @ResponseBody String getLastExtremeSide(@PathVariable("id-game") int gameId, 
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		String lastExtremeSide;
		
		if (game != null) {
			lastExtremeSide = game.getLastExtremeSide();
		} else {
			lastExtremeSide = "";
		}
				
		if (lastExtremeSide == null) {
			lastExtremeSide = "";
		}
		
		try {
			json = objectMapper.writeValueAsString(lastExtremeSide);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;		
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return pega o numero de peças do jogador adversario
	 */
	@CrossOrigin
	@RequestMapping("/get-num-tiles-enemy/{id-game}/{id-player}")
	public @ResponseBody int getNumTilesEnemy(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			return game.getNumPieceEnemy(playerId);
		} else {
			return -1;
		}
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param extremeSide
	 * @param valueDeadEnd
	 * @param valueExtreme
	 * @param request
	 * @return se a jogada foi bem sucedida
	 */
	@CrossOrigin
	@RequestMapping("/play/{id-game}/{id-player}/{value-dead-end}/{value-extreme}/{extreme-side}")
	public @ResponseBody boolean play(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			@PathVariable("value-dead-end") int valueDeadEnd,
			@PathVariable("value-extreme") int valueExtreme,
			@PathVariable("extreme-side") String extremeSide,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			return game.canPlay(playerId, valueDeadEnd, valueExtreme) && 
					game.play(playerId, extremeSide, valueDeadEnd, valueExtreme);
		} else {
			return false;
		}
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return se o jogador ja pode jogar, mesmo que comprando novas pecas
	 */
	@CrossOrigin
	@RequestMapping("/can-play-buying/{id-game}/{id-player}")
	public @ResponseBody boolean canPlayBuying(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			return game.canPlayBuying(playerId);
		} else {
			return false;
		}
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return se o jogador j� pode jogar, sem comprar novas pe�as
	 */
	@CrossOrigin
	@RequestMapping("/can-play/{id-game}/{id-player}")
	public @ResponseBody boolean canPlay(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			return game.canPlay(playerId);
		} else {
			return false;
		}
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/buy/{id-game}/{id-player}")
	public @ResponseBody String buy(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			Piece piece = game.buyPiece(playerId);
			
			try {
				json = objectMapper.writeValueAsString(piece);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return json;
		} else {
			return "";
		}
		
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/is-winner/{id-game}/{id-player}")
	public @ResponseBody boolean isWinner(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			return game.isWinner(playerId);
		} else {
			return false;
		}
	}

	/**
	 * @param request
	 */
	@CrossOrigin
	@RequestMapping("/get-all-rankings")
	public @ResponseBody String getAllRankings(HttpServletRequest request){
		List<Ranking> lista;
		
		try {
			lista = RankingDataManager.getAllRankings();
			json = objectMapper.writeValueAsString(lista);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;		
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/update-ranking/{id-game}/{id-player}")
	public @ResponseBody boolean updateRanking(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		if (game != null) {
			boolean win = game.isWinner(playerId);
			try {
				if (RankingDataManager.checkRanking(playerId)) {
					List<Ranking> li = RankingDataManager.getAllRankings();
					Ranking nr = null;
					for (Ranking r : li) {
						if (r.getPlayer() == playerId) {
							nr = r;
							break;
						}
					}
					if (nr == null) {
						return false;
					}
					nr.setPartidas_jogadas(nr.getPartidas_jogadas() + 1);
					if (win) {
						nr.setVitorias(nr.getVitorias() + 1);
					}
					RankingDataManager.updateRanking(nr);
				} else {
					Ranking r = new Ranking();
					r.setPlayer(playerId);
					r.setPartidas_jogadas(1);
					if (win) {
						r.setVitorias(1);
					} else {
						r.setVitorias(0);
					}
					RankingDataManager.insertRanking(r);
				}
				return true;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
			return false;
		} else {
			return false;
		}
	}
}
