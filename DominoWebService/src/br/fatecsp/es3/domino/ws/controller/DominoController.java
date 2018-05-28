package br.fatecsp.es3.domino.ws.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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


	/**
	 * @param request
	 * @return the player's id, that will be necessary for all the future transactions
	 */
	@RequestMapping("/connect")
	public @ResponseBody int connectUserToGame(HttpServletRequest request){
		Player player = new Player(NEXTPLAYERID); 
		playersMap.put(new Integer(NEXTPLAYERID), player);
		NEXTPLAYERID++;
		return player.getId();
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
		NEXTGAMEID++;
		return game.getId();
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param extremeSide
	 * @param valueDeadEnd
	 * @param valueExtreme
	 * @param request
	 * @return if the player is the winner of the game after this run or not
	 */
	@RequestMapping("/play/{id-game}/{id-player}/{extreme-side}/{value-dead-end}/{value-extreme}")
	public @ResponseBody boolean play(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			@PathVariable("extreme-side") String extremeSide,
			@PathVariable("value-dead-end") int valueDeadEnd,
			@PathVariable("value-extreme") int valueExtreme,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		game.play(playerId, extremeSide, valueDeadEnd, valueExtreme);
		return game.isWinner(playerId);
	}
	
	/**
	 * @param gameId
	 * @param playerId
	 * @param request
	 * @return the piece that was bought from the remaining pieces stack
	 */
	@RequestMapping("/play/{id-game}/{id-player}")
	public @ResponseBody Piece buy(@PathVariable("id-game") int gameId,
			@PathVariable("id-player") int playerId,
			HttpServletRequest request){
		Game game = gamesMap.get(gameId);
		return game.buyPiece(playerId);
	}
	
}
