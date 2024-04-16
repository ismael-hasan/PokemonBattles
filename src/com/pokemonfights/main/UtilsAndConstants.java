package com.pokemonfights.main;

import java.util.Map;
import java.util.Scanner;

import com.pokemonfights.pokemons.Pokemon;

/**
 * Some convenience methods and constants, simple. For instance, in the App a
 * list of pokemons if often printed, so a function that receives the list and
 * prints it is included here
 * 
 * @author ismael
 *
 */
public class UtilsAndConstants {
	// Types of pokemons and attacks. All pokemon are considered generic and another
	// one.
	public final static int TYPE_ELECTRIC = 0;
	public final static int TYPE_WATER = 1;
	public final static int TYPE_EARTH = 2;
	public final static int TYPE_FIRE = 3;
	public final static int TYPE_PLANT = 4;
	public final static int TYPE_WIND = 5;
	public final static int TYPE_GENERIC = 6;

	// This defines the amount of XP needed to advance a level. First threshold to
	// advance to level 2, second threshold to level 3...
	// The array size determines the max level that pokemons can reach: an array
	// with 9 elements implies that level 10 is the maximum.
	public final static Integer[] LEVELING_THRESHOLD = { 1000, 3000, 6000, 10000, 15000, 21000, 28000, 36000, 45000 };

	// It defines when new skills can be learned. For instance, 3 means that new
	// skillcan be learnt at level 3, 6, 9, 12...
	public final static int LEVELING_SKILL_GAIN = 3;
	
	// Defeating a pokemons rewards 1000xp per level.
	public final static int LEVELING_XP_LEVEL_MULTIPLIER = 700;
	
	public final static int PERCENTAGE_CHANCE_GETTING_POKEBALL = 20;
	public final static int PERCENTAGE_CHANCE_GETTING_POTION = 40;
	
	public static final int BATTLE_RESULT_UNFINISHED = 0;
	public static final int BATTLE_RESULT_VICTORY = 1;
	public static final int BATTLE_RESULT_CAPTURED = 2;
	public static final int BATTLE_RESULT_RETREAT = 3;
	//Actually, defeated is never used. The reason is that we handle that with an exception of "All defeated". 
	public static final int BATTLE_RESULT_DEFEATED = 4;

	public final static String getTypeText(int type) {
		switch (type) {
		case TYPE_ELECTRIC:
			return "eléctrico";
		case TYPE_WATER:
			return "agua";
		case TYPE_EARTH:
			return "piedra";
		case TYPE_FIRE:
			return "fuego";
		case TYPE_PLANT:
			return "planta";
		case TYPE_WIND:
			return "viento";
		case TYPE_GENERIC:
			return "genérico";
		default:
			return "desconocido";
		}
	}
	
	
	public final static void printPokemons(Map<Integer, Pokemon> pokemons) {
		for (Pokemon pokemon : pokemons.values()) {
			System.out.println(pokemon.toString());
		}
		
	}

	/**
	 * It reads an int from the keyboard. If the number is negative, it makes it
	 * positive. It returns -1 on any reading error. It adds a new line at the end.
	 * 
	 * @return The number read, or -1 if there was any error.
	 */
	public static int readIntFromKeyboard() {
		Scanner scanner = new Scanner(System.in);
		int result;
		try {
			result = Math.abs(scanner.nextInt());
		} catch (Exception e) {
			result = -1;
		}
		System.out.println();
		return result;
	}

	public final static String FILE_POKEMON_LIST = "PokemonList.csv";
	public final static String FILE_SKILLS_LIST = "SkillList.csv";

}
