package com.pokemonfights.main;

import java.util.List;
import java.util.Map;

import com.pokemonfights.pokemons.Pokemon;

/**
 * 
 * It represents the status of the game. Pokemons, items and current level in
 * the tournament are stored here. Each Pokemon is assigned an unique ID (so, we can have 
 * 2 Pikachus with different IDs).
 * 
 * @author ismael
 * 
 */
public class GameStatus {

	private int currentTournamentLevel;
	private Map<Integer, Pokemon> currentPokemons;
	private int numberOfPokeball;
	private int numberOfHealthPotions;
	private String location;
	private Pokemon currentSelectedPokemon;

	public GameStatus(int currentTournamentLevel, Map<Integer, Pokemon> currentPokemons, int numberOfPokeball,
			int numberOfHealthPotions, String location, Pokemon currentSelectedPokemon ) {
		super();
		this.currentTournamentLevel = currentTournamentLevel;
		this.currentPokemons = currentPokemons;
		this.numberOfPokeball = numberOfPokeball;
		this.numberOfHealthPotions = numberOfHealthPotions;
		this.location = location;
		this.currentSelectedPokemon = currentSelectedPokemon;
	}

	public int getCurrentTournamentLevel() {
		return currentTournamentLevel;
	}

	public void setCurrentTournamentLevel(int currentTournamentLevel) {
		this.currentTournamentLevel = currentTournamentLevel;
	}

	public Map<Integer, Pokemon> getCurrentPokemons() {
		return currentPokemons;
	}

	public int getNumberOfPokeballs() {
		return numberOfPokeball;
	}

	public void setNumberOfPokeballs(int numberOfPokeball) {
		this.numberOfPokeball = numberOfPokeball;
	}

	public int getNumberOfHealthPotions() {
		return numberOfHealthPotions;
	}

	public void setNumberOfHealthPotions(int numberOfHealthPotions) {
		this.numberOfHealthPotions = numberOfHealthPotions;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public Pokemon getCurrentSelectedPokemon() {
		return currentSelectedPokemon;
	}

	public void setCurrentSelectedPokemon(Pokemon currentSelectedPokemon) {
		this.currentSelectedPokemon = currentSelectedPokemon;
	}
	
	

}
