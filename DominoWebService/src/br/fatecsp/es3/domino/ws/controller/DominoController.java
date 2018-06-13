package br.fatecsp.es3.domino.ws.controller;

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

import br.fatecsp.es3.domino.ws.entities.Game;
import br.fatecsp.es3.domino.ws.entities.Piece;
import br.fatecsp.es3.domino.ws.entities.Player;

@Controller
@RequestMapping("/domino")
public class DominoController {
	
	private static int NEXTPLAYERID = 1;
	private static int NEXTGAMEID = 1;
	private static Map<Integer,Player> playersMap = new HashMap<Integer,Player>();
	private static Map<Integer,Game> gamesMap = new HashMap<Integer,Game>();
	private static Map<Integer,Player> freePlayersMap= new HashMap<Integer,Player>();
	
	/**
	 * @param player player object
	 */
	private void doConnectRoutine(Player player) {
		playersMap.put(new Integer(NEXTPLAYERID), player);
		freePlayersMap.put(NEXTPLAYERID, player);
		NEXTPLAYERID++;
	}
	
	/**
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@RequestMapping("/connect")
	public @ResponseBody int connectUserToGame(HttpServletRequest request){
		Player player = new Player(NEXTPLAYERID); 
		this.doConnectRoutine(player);
		return player.getId();
	}
	
	/**
	 * @param playerName the player name
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@RequestMapping("/connect/{name-player}")
	public @ResponseBody int connectUserToGame(@PathVariable("name-player") String playerName, HttpServletRequest request){
		Player player = new Player(NEXTPLAYERID, playerName); 
		this.doConnectRoutine(player);
		return player.getId();
	}
	
	/**
	 * @param playerName the player name
	 * @param playerEmail the player email address
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@RequestMapping("/connect/{name-player}/{email-player}")
	public @ResponseBody int connectUserToGame(@PathVariable("name-player") String playerName, 
			@PathVariable("email-player") String playerEmail,
			HttpServletRequest request){
		Player player = new Player(NEXTPLAYERID, playerName, playerEmail); 
		this.doConnectRoutine(player);
		return player.getId();
	}
	
	/**
	 * 
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return
	 */
	@RequestMapping("/get-free-players/")
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
	@RequestMapping("/newgame/{id-player1}/{id-player2}")
	public @ResponseBody int startNewGame(@PathVariable("id-player1") int player1, 
			@PathVariable("id-player2") int player2,
			HttpServletRequest request){
		Game game = new Game(NEXTGAMEID, playersMap.get(player1), playersMap.get(player2));
		gamesMap.put(game.getId(), game);
		freePlayersMap.remove(player1);
		freePlayersMap.remove(player2);
		NEXTGAMEID++;
		return game.getId();
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
