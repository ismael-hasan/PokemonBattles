package com.pokemonfights.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pokemonfights.exceptions.AllPokemonsDefeatedException;
import com.pokemonfights.exceptions.NoPokeballsException;
import com.pokemonfights.exceptions.NoPotionsException;
import com.pokemonfights.pokemons.Pokemon;
import com.pokemonfights.pokemons.Skill;

public class ControlCenter {

	private static Random random = new Random(System.currentTimeMillis());

	private static void printMainMenu() {
		System.out.println("\nIntroduce una opción: \n1. Partida nueva \n2. Cargar partida \n0. Salir");
	}

	private static void printGameMenu() {
		System.out.println(
				"\nElige una opción:\n1. Ver estado de mis pokemon\n2. Buscar una batalla aleatoria \n3. Revisar mis items \n4. Curar a un pokemon \n5. Entrar en batalla de torneo \n6. Guardar partida\n0. Salir de la partida");

	}

	/**
	 * It shows the main menu to start a game or exit. It will call to the right
	 * functions to start the game. It does not iterate indefinitely, it just
	 * informs back if it should finish already or not.
	 * 
	 * @return if the game should close.
	 * @throws FileNotFoundException
	 */
	private static boolean doMainMenu() throws FileNotFoundException {
		printMainMenu();
		int option = UtilsAndConstants.readIntFromKeyboard();
		while (option < 0 || option > 2) {
			System.out.println("Opción no válida.\n");
			printMainMenu();
			option = UtilsAndConstants.readIntFromKeyboard();
		}

		// We generate a @GameStatus based on the selected option, or we exit. With that
		// GameStatus we will start playing.

		GameStatus status = null;

		if (option == 0) {
			System.out.println("¡Gracias por jugar!");
			return true;
		} else if (option == 1) {
			Pokemon startingPokemon = (Pokemon) PokemonManager.getAllPokemons().values().toArray()[random
					.nextInt(PokemonManager.getAllPokemons().size())];
			System.out.println(
					"Llegas a la clase del profesor Yisus. Sobre la mesa tiene un pokemon. \n- Tengo tu pokemon preparado. No te doy a elegir, no te pienses que esto es una democracia. Aquí tienes tu "
							+ startingPokemon.getName() + ". Hala, ¡a jugar!");

			Map<Integer, Pokemon> myPokemons = new HashMap<Integer, Pokemon>();
			myPokemons.put(startingPokemon.getId(), startingPokemon);
			status = new GameStatus(0, myPokemons, 3, 3, "Aula 6", startingPokemon);
		} else if (option == 2) {
			// We read a game status.
			System.out.println("Opción no implementada. Elige otra o impleméntala.");
			return false;
		}

		boolean shouldFinish = false;
		while (!shouldFinish) {
			shouldFinish = doGameMenu(status);
		}
		return false;

	}

	/**
	 * Based on the current status, it executes the game - fights, etc. It returns
	 * if the game should finish
	 * 
	 * @param status
	 * @return
	 */
	private static boolean doGameMenu(GameStatus status) {
		printGameMenu();
		int option = UtilsAndConstants.readIntFromKeyboard();
		while (option < 0 || option > 6) {
			System.out.println("Opción no válida.\n");
			printGameMenu();
			option = UtilsAndConstants.readIntFromKeyboard();
		}
		switch (option) {
		case 0:
			return true;
		case 1:
			UtilsAndConstants.printPokemons(status.getCurrentPokemons());
			return false;
		case 2:
			try {
				doBattle(status);
			} catch (FileNotFoundException e) {
				System.out.println("Error haciendo batalla. ¿Están los archivos correctos en su sitio?");
			} catch (AllPokemonsDefeatedException e) {
				System.out.println("No te quedan pokemons. Fin de la partida.");
				return true;
			}
			return false;
		case 3:
			printMyItems(status);
			return false;
		case 4:
			try {
				startHealPokemon(status);
				System.out.println("¡Curado!");
			} catch (NoPotionsException npe) {
				System.out.println("No hay pociones.");
				return false;
			}
			return false;
		case 5:
			System.out.println("Opción no implementada");
			return false;
		case 6:
			System.out.println("Opción no implementada");
			return false;
		default: {
		} // do nothing.
		}

		return false;
	}

	private static void doBattle(GameStatus status) throws FileNotFoundException, AllPokemonsDefeatedException {
		// we will always battle a random pokemon with an XP value between min and max
		// XP of the user pokemons.
		/*
		 * *************************************** START OF CREATING A POKEMON TO BATTLE
		 *****************************************/
		Pokemon toBattle;
		int xpPokemonToBattle = 0;
		int minXp = Integer.MAX_VALUE;
		int maxXp = 0;
		for (Pokemon pokemon : status.getCurrentPokemons().values()) {
			if (pokemon.getXp() > maxXp) {
				maxXp = pokemon.getXp();
			}
			if (pokemon.getXp() < minXp) {
				minXp = pokemon.getXp();
			}
		}

		xpPokemonToBattle = (int) (random.nextDouble(0.5,2) * random.nextInt(minXp, maxXp + 1));
		
		// We create a level 1 pokemon
		Pokemon[] availablePokemon = PokemonManager.getAllPokemons().values().toArray(new Pokemon[] {});
		toBattle = availablePokemon[random.nextInt(availablePokemon.length)];
		
		PokemonManager.giveXp(toBattle, xpPokemonToBattle, false);
		/*
		 * *************************************** END OF CREATING A POKEMON TO BATTLE
		 *****************************************/
		System.out.println("Un " + toBattle.getName() + " salvaje de nivel " + toBattle.getLevel() + " aparece en "
				+ status.getLocation());
		Pokemon myPokemon = selectCurrentPokemonToFight(status);
		status.setCurrentSelectedPokemon(myPokemon);
		// This is stored to see how many rounds each Pokemon played, for XP related
		// purposes - XP is gained proportional to the number of rounds. Note defeated
		// pokemons do not win any XP.
		Map<Pokemon, Integer> roundsThisPokemon = new HashMap<Pokemon, Integer>();
		int roundNumber = 0;

		roundNumber++;
		System.out.println("Comienza la ronda " + roundNumber);
		int roundResult = doBattleRound(status, toBattle);
		if (roundsThisPokemon.containsKey(myPokemon)) {
			roundsThisPokemon.put(myPokemon, roundsThisPokemon.get(myPokemon) + 1);
		} else {
			roundsThisPokemon.put(myPokemon, 1);
		}
		while (roundResult == UtilsAndConstants.BATTLE_RESULT_UNFINISHED) {
			roundNumber++;
			System.out.println("Comienza la ronda " + roundNumber);
			roundResult = doBattleRound(status, toBattle);

			if (roundsThisPokemon.containsKey(myPokemon)) {
				roundsThisPokemon.put(myPokemon, roundsThisPokemon.get(myPokemon) + 1);
			} else {
				roundsThisPokemon.put(myPokemon, 1);
			}
		}
		// If we did not escape or lost, we gain XP.
		if (roundResult != UtilsAndConstants.BATTLE_RESULT_DEFEATED
				&& roundResult != UtilsAndConstants.BATTLE_RESULT_RETREAT) {
			int totalXp = toBattle.getLevel() * UtilsAndConstants.LEVELING_XP_LEVEL_MULTIPLIER;
			System.out.println("El equipo gana " + totalXp + " experiencia.");
			for (Pokemon pokemon : roundsThisPokemon.keySet()) {
				if (pokemon.getCurrentHp() > 0) {
					int rounds = roundsThisPokemon.get(pokemon);
					int xpGained = totalXp * rounds / roundNumber;
					PokemonManager.giveXp(myPokemon, xpGained, true);
				}
			}
		}
	}

	private static int doBattleRound(GameStatus status, Pokemon toBattle) throws AllPokemonsDefeatedException {
		// the battle finishes if all my pokemons are defeated, I retreat, I capture the
		// other pokemon or I defeat the other pokemon

		Pokemon myPokemon = status.getCurrentSelectedPokemon();
		System.out.println("\t" + myPokemon.toString() + "\n========= VS =========\n\t" + toBattle.toString());

		int option = chooseBattleAction(myPokemon);

		/*
		 * *************************************** START OF PLAYERS TURN
		 *****************************************/

		switch (option) {
		case 0: // change Pokemon
			myPokemon = selectCurrentPokemonToFight(status);
			status.setCurrentSelectedPokemon(myPokemon);
			break;
		case 1: // retreat. There is a 50% chance of being successful.
			boolean canRetreat = random.nextBoolean();
			if (!canRetreat) {
				System.out.println("Intentas escapar, ¡pero no lo consigues!");
			} else {
				System.out.println("¡Consigues escapar!");
				return UtilsAndConstants.BATTLE_RESULT_RETREAT;

			}
		case 2: // heal pokemon
			try {
				doHealPokemon(status, myPokemon);
				System.out.println("Consigues curar a " + myPokemon.getName());
			} catch (NoPotionsException e) {
				System.out.println("¡Intentas curar a " + myPokemon.getName()
						+ " pero al rebuscar en la bolsa no te quedan pociones! Una ocasión perdida.");
			}
			break;
		case 3: // throw pokeball
			// The lower the health of the enemy, the more likely it is to catch it.
			System.out.println("¡Tratas de capturar al " + toBattle.getName());
			try {
				boolean captured = throwPokeball(status, toBattle);
				if (captured) {
					// If we capture it, we need to assign a new unused ID to the pokemon, so it
					// does not collide with the ones we already have.
					for (int i = 0; i < Integer.MAX_VALUE; i++) {
						if (!status.getCurrentPokemons().containsKey(i)) {
							toBattle.setId(i);
							break;
						}
					}
					System.out.println("¡Lo has conseguido! Ahora tienes un nuevo " + toBattle.getName());
					status.getCurrentPokemons().put(toBattle.getId(), toBattle);
					return UtilsAndConstants.BATTLE_RESULT_CAPTURED;
				} else {
					System.out.println(toBattle.getName() + " consigue evitar que lo captures...");
				}
			} catch (NoPokeballsException e) {
				System.out.println(
						"¡Al rebuscar en la bolsa te das cuenta de que no te quedan pokeballs! Una ocasión perdida.");
			}
			break;
		default: // some attack
			int skillPosition = option - 4; // remember, positions are shifted in the switch. E.g, skill in position 0
											// got assigned option 4.
			Skill currentSkill = myPokemon.getSkills().get(skillPosition);
			System.out.println("Ordenas a " + myPokemon.getName() + " que lance su técnica " + currentSkill.getName());
			if (myPokemon.getCurrentMp() < currentSkill.getCost()) {
				System.out.println("¡No tiene suficiente energía! Es una ocasión perdida.");
			} else {
				int damage = (int) (myPokemon.getPower() * currentSkill.getPowerMultiplier());
				myPokemon.setCurrentMp(myPokemon.getCurrentMp() - currentSkill.getCost());
				System.out.println(myPokemon.getName() + " hace " + damage + " puntos de daño a " + toBattle.getName());
				toBattle.setCurrentHp(toBattle.getCurrentHp() - damage);
				if (toBattle.getCurrentHp() <= 0) {
					System.out.println("¡Derrotas a " + toBattle.getName() + "!");
					// Rewards...
					boolean pokeballObtained = random.nextInt(0,
							101) < UtilsAndConstants.PERCENTAGE_CHANCE_GETTING_POKEBALL;
					boolean potionObtained = random.nextInt(0,
							101) < UtilsAndConstants.PERCENTAGE_CHANCE_GETTING_POTION;
					if (pokeballObtained) {
						System.out.println("¡Encuentras una pokeball!");
						status.setNumberOfPokeballs(status.getNumberOfPokeballs() + 1);
					}
					if (potionObtained) {
						System.out.println("¡Encuentras una poción curativa!");
						status.setNumberOfHealthPotions(status.getNumberOfHealthPotions() + 1);
					}
					return UtilsAndConstants.BATTLE_RESULT_VICTORY;
				}
			}
		}
		/*
		 * *************************************** END OF PLAYERS TURN
		 *****************************************/

		/*
		 * *************************************** START OF BOT TURN
		 *****************************************/
		System.out.println(toBattle.getName() + " se prepara para atacar... ");
		// We will choose all of the skills for which it has enough MP, and also one
		// extra position for "confussion".
		List<Skill> validSkills = new ArrayList<Skill>();
		for (Skill skill : toBattle.getSkills()) {
			if (toBattle.getCurrentMp() > skill.getCost()) {
				validSkills.add(skill);
			}
		}
		validSkills.add(null);
		Skill selectedSkill = validSkills.get(random.nextInt(validSkills.size()));
		if (selectedSkill == null) {
			System.out.println("¡Pero está confundido!");
		} else {
			int damage = (int) (toBattle.getPower() * selectedSkill.getPowerMultiplier());
			System.out.println("Utiliza " + selectedSkill.getName() + ". " + myPokemon.getName() + " recibe " + damage
					+ " puntos de daño.");
			myPokemon.setCurrentHp(myPokemon.getCurrentHp() - damage);
			if (myPokemon.getCurrentHp() <= 0) {
				System.out.println(myPokemon.getName() + " queda derrotado...");
				myPokemon = selectCurrentPokemonToFight(status);
				// On defeat, if there are no pokemons left... it finishes.
				status.setCurrentSelectedPokemon(myPokemon);
			}
		}

		/*
		 * *************************************** END OF BOT TURN
		 *****************************************/

		System.out.println();
		return UtilsAndConstants.BATTLE_RESULT_UNFINISHED;

	}

	private static int chooseBattleAction(Pokemon myPokemon) {
		System.out.println("Elige una opción: ");
		System.out.println("\t0: Cambiar pokemon");
		System.out.println("\t1: Retirada");
		System.out.println("\t2: Curar pokemon");
		System.out.println("\t3: Lanzar pokeball");
		for (int i = 4; i < myPokemon.getSkills().size() + 4; i++) {
			System.out.println("\t" + i + ": " + myPokemon.getSkills().get(i - 4));
		}
		int option = UtilsAndConstants.readIntFromKeyboard();
		if (option < 0 || option > myPokemon.getSkills().size() + 3) {
			System.out.println("\tOpción no válida: ");
			// This is an example of recursion. Learn more about recursion and its dangers
			// in: https://codegym.cc/es/groups/posts/es.971.recursin-de-java
			option = chooseBattleAction(myPokemon);
		}
		return option;
	}

	private static Pokemon selectCurrentPokemonToFight(GameStatus status) throws AllPokemonsDefeatedException {
		Map<Integer, Pokemon> availablePokemons = new HashMap<Integer, Pokemon>();
		for (Pokemon pokemon : status.getCurrentPokemons().values()) {
			if (pokemon.getCurrentHp() > 0) {
				availablePokemons.put(pokemon.getId(), pokemon);
			}
		}
		if (availablePokemons.isEmpty()) {
			throw new AllPokemonsDefeatedException();
		}
		System.out.println("¡Elige un pokemon para pelear!");
		UtilsAndConstants.printPokemons(availablePokemons);
		System.out.println("¿Qué pokemon eliges?:  ");
		int id = UtilsAndConstants.readIntFromKeyboard();
		while (!status.getCurrentPokemons().containsKey(id)) {
			System.out.println("No tienes ningún pokemon con ese id: " + id);
			UtilsAndConstants.printPokemons(availablePokemons);
			System.out.println("¿Qué pokemon eliges?:  ");
			id = UtilsAndConstants.readIntFromKeyboard();
		}
		Pokemon result = status.getCurrentPokemons().get(id);
		System.out.println("¡Has elegido a " + result.getName());
		return result;
	}

	private static void printMyItems(GameStatus status) {
		System.out.println("\n\tPociones: " + status.getNumberOfHealthPotions() + "\n\tPokeballs: "
				+ status.getNumberOfPokeballs());
	}

	private static void startHealPokemon(GameStatus status) throws NoPotionsException {

		System.out.println("¿Qué Pokemon quieres curar? (introduce el ID)");
		UtilsAndConstants.printPokemons(status.getCurrentPokemons());
		int option = UtilsAndConstants.readIntFromKeyboard();
		Pokemon toHeal = status.getCurrentPokemons().get(option);
		if (toHeal == null) {
			System.out.println("No existe ese pokemon.");
		} else {
			doHealPokemon(status, toHeal);
		}
	}

	private static void doHealPokemon(GameStatus status, Pokemon toHeal) throws NoPotionsException {

		if (status.getNumberOfHealthPotions() < 1) {
			throw new NoPotionsException();
		}
		toHeal.setCurrentHp(toHeal.getMaxHp());
		status.setNumberOfHealthPotions(status.getNumberOfHealthPotions() - 1);
	}

	// the lower the health, the more likely it is to capture the pokemon.
	private static boolean throwPokeball(GameStatus status, Pokemon toCapture) throws NoPokeballsException {
		if (status.getNumberOfPokeballs() <= 0) {
			throw new NoPokeballsException();
		}
		int maxHp = toCapture.getMaxHp();
		int currentHp = toCapture.getCurrentHp();
		int result = random.nextInt(0, maxHp + 1);
		status.setNumberOfPokeballs(status.getNumberOfPokeballs() - 1);
		return result > currentHp;
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Bienvenido al entorno Pokemon del Liceo La Paz.");
		System.out.println(
				"Puedes buscar pokemons en las diferentes aulas; si los derrotas, los pokemon que hayan participado en la batalla podrán ganar experiencia.");
		System.out.println("Cuando sientas que estás preparado, ¡entra al torneo!");
		boolean shouldFinish = false;
		while (!shouldFinish) {
			shouldFinish = doMainMenu();
		}
	}
}
