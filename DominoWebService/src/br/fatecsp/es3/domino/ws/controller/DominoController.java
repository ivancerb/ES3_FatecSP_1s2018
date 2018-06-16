package br.fatecsp.es3.domino.ws.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.fatecsp.es3.domino.ws.data.GameDataManager;
import br.fatecsp.es3.domino.ws.data.PlayerDataManager;
import br.fatecsp.es3.domino.ws.entities.Game;
import br.fatecsp.es3.domino.ws.entities.Piece;
import br.fatecsp.es3.domino.ws.entities.Player;

@Controller
@RequestMapping("/domino")
public class DominoController {
	
	private static Map<Integer,Player> playersMap = new HashMap<Integer,Player>();
	private static Map<Integer,Game> gamesMap = new HashMap<Integer,Game>();
	private static Map<Integer,Player> freePlayersMap= new HashMap<Integer,Player>();
	
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
	@RequestMapping("/get-free-players")
	public @ResponseBody String getFreePlayers(HttpServletRequest request){
		ObjectMapper objectMapper = new ObjectMapper();
		String json="";
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
	
	@RequestMapping("/endgame/{id-game}")
	public @ResponseBody boolean endGame(
			@PathVariable("id-game") int gameId,
			HttpServletRequest request){
		try {
			Game game = gamesMap.get(gameId);
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
	@RequestMapping("/get-pieces/{id-game}/{id-player}")
	public @ResponseBody String getPieces(@PathVariable("id-game") int gameId, @PathVariable("id-player") int playerId, 
			HttpServletRequest request){
		HashSet<Piece> playerPieces = new HashSet<Piece>();
		Game game = gamesMap.get(gameId);
		if(playerId == game.getPlayer1().getId()) {
			playerPieces = game.getBoard().getPlayer1Pieces();
		}else if(playerId == game.getPlayer2().getId()) {
			playerPieces = game.getBoard().getPlayer2Pieces();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String json="";
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
	@RequestMapping("/get-piece-extreme/{id-game}")
	public @ResponseBody int getPieceExtreme(@PathVariable("id-game") int gameId, 
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.getPieceExtreme();
	}
	
	/**
	 * @param request
	 */
	@RequestMapping("/get-piece-dead-end/{id-game}")
	public @ResponseBody int getPieceDeadEnd(@PathVariable("id-game") int gameId, 
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.getPieceDeadEnd();
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
	@RequestMapping("/play/{id-game}/{id-player}/{value-dead-end}/{value-extreme}/{extreme-side}")
	public @ResponseBody boolean play(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			@PathVariable("value-dead-end") int valueDeadEnd,
			@PathVariable("value-extreme") int valueExtreme,
			@PathVariable("extreme-side") String extremeSide,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.play(playerId, extremeSide, valueDeadEnd, valueExtreme);
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return se o jogador já pode jogar
	 */
	@RequestMapping("/can-play/{id-game}/{id-player}")
	public @ResponseBody boolean canPlay(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.canPlay(playerId);
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@RequestMapping("/buy/{id-game}/{id-player}")
	public @ResponseBody String buy(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		Piece piece = game.buyPiece(playerId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json="";
		try {
			json = objectMapper.writeValueAsString(piece);
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
	@RequestMapping("/is-winner/{id-game}/{id-player}")
	public @ResponseBody boolean isWinner(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.isWinner(playerId);
	}

}
