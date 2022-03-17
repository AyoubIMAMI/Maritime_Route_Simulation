package fr.unice.polytech.si3.qgl.royal_fortune.dao;

import java.util.List;

import fr.unice.polytech.si3.qgl.royal_fortune.environment.SeaEntities;
import fr.unice.polytech.si3.qgl.royal_fortune.target.Goal;
import fr.unice.polytech.si3.qgl.royal_fortune.captain.Crewmates.Sailor;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.Wind;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Ship;

public class InitGameDAO {
	Goal goal;
	Ship ship;
	List<Sailor> sailors;
	int shipCount;
	Wind wind;
	List<SeaEntities> seaEntities;
	
	public InitGameDAO() {}
	public InitGameDAO(Goal goal, Ship ship, List<Sailor> sailors, int shipCount, Wind wind) {
		super();
		this.goal = goal;
		this.ship = ship;
		this.sailors = sailors;
		this.shipCount = shipCount;
		this.wind = wind;
	}
	
	public Goal getGoal() {
		return goal;
	}
	public Ship getShip() {
		return ship;
	}
	public List<Sailor> getSailors() {
		return sailors;
	}
	public int getShipCount() {
		return shipCount;
	}

	public Wind getWind() {
		return wind;
	}

	public List<SeaEntities> getSeaEntities() {
		return seaEntities;
	}

	public void setSailors(List<Sailor> sailorList){
		this.sailors = sailorList;
	}
}
