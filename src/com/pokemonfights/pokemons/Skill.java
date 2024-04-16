package com.pokemonfights.pokemons;

import com.pokemonfights.main.UtilsAndConstants;

public class Skill {

	
	private int id; 
	// One of @PokemonConstants
	private int type;

	private String name;

	
	/**
	 * Skills damage is a multiplier over the pokemon base damage.
	 */
	private double powerMultiplier;
	
	private int cost;

	public Skill(int id, int type, String name, double powerMultiplier, int cost) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.powerMultiplier = powerMultiplier;
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "("+ id+ ") " + name + ", multiplicador de ataque: " + powerMultiplier + ", coste: " + cost + ", tipo:" + UtilsAndConstants.getTypeText(type);
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	public double getPowerMultiplier() {
		return powerMultiplier;
	}

	public int getCost() {
		return cost;
	}

	public int getId() {
		return id;
	}

}
