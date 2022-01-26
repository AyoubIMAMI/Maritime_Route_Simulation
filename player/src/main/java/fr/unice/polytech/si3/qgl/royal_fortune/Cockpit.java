package fr.unice.polytech.si3.qgl.royal_fortune;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;

/**
 * @author Bonnet Killian Imami Ayoub Karrakchou Mourad Le Bihan Leo
 *
 */
public class Cockpit implements ICockpit {

	public void initGame(String game) {
		System.out.println("Init game input: " + game);
	}

	public String nextRound(String round) {
		System.out.println("Next round input: " + round);
		return "[]";
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}
}
