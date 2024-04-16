package com.pokemonfights.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import com.pokemonfights.pokemons.Pokemon;
import com.pokemonfights.pokemons.Skill;

/**
 * This class offers methods for general management of Pokemon from the point of
 * view of the application. For instance, it is the one in charge of reading the
 * files containing lists of pokemons and skills, leveling up pokemons...
 * 
 * @author ismael
 *
 */
public class PokemonManager {

	private static Map<Integer, Pokemon> allPokemons = null;
	private static Map<Integer, Skill> allSkills = null;

	/**
	 * Pokemons level up when they reach one of the levels in
	 * {@link UtilsAndConstants}. They learn skills according to the
	 * <b>LEVELING_<b> constants defined in there.
	 * 
	 * @param pokemon     pokemon to improve
	 * @param xp          XP gained by the pokemon.
	 * @param userPokemon if true, it prints info on the leveling up for the user
	 *                    and lets the user to decide. When set to false, it prints
	 *                    nothing, and takes random decisions - it is used to create
	 *                    pokemons so the user can fight them.
	 * @return if it leveled up.
	 */
	public static boolean giveXp(Pokemon pokemon, int xp, boolean userPokemon) {
		// At 1000XP it reaches level 2, at 2000XP it reaches level 3... Max level is
		// 10.
		boolean leveledUp = false;
		int currentLevel = pokemon.getLevel();
		int currentXp = pokemon.getXp();
		int newXp = xp + currentXp;
		int newLevel = 1;
		for (int i = 0; i < UtilsAndConstants.LEVELING_THRESHOLD.length; i++) {
			if (newXp >= UtilsAndConstants.LEVELING_THRESHOLD[i]) {
				newLevel++;
			}
		}
		// we raise levels one by one.
		Random multiplier = new Random();
		leveledUp = newLevel > currentLevel;
		for (int i = currentLevel+1; i <= newLevel; i++) {
			int newHp = (int) (multiplier.nextDouble(1.1, 1.2) * pokemon.getMaxHp());
			int newMp = (int) (multiplier.nextDouble(1.1, 1.2) * pokemon.getMaxMp());
			int newPower = (int) (multiplier.nextDouble(1.1, 1.2) * pokemon.getPower());
			if (userPokemon) {
				System.out.println("¡(" + pokemon.getId() + ") " + pokemon.getName() + " sube a nivel " + i + "!");
				System.out.println("\thp: " + pokemon.getMaxHp() + " -> " + newHp);
				System.out.println("\tmp: " + pokemon.getMaxMp() + " -> " + newMp);
				System.out.println("\tpower: " + pokemon.getPower() + " -> " + newPower);
			}
			pokemon.setMaxHp(newHp);
			pokemon.setMaxMp(newMp);
			pokemon.setCurrentHp(pokemon.getMaxHp());
			pokemon.setCurrentMp(pokemon.getMaxMp());
			pokemon.setPower(newPower);
			if ((i) % UtilsAndConstants.LEVELING_SKILL_GAIN == 0) {
				learnSkill(pokemon, userPokemon);
			}
		}

		pokemon.setXp(newXp);
		pokemon.setLevel(newLevel);
		if (userPokemon && leveledUp) {
			System.out.println("¡" + pokemon.getName() + " ha alcanzado el nivel " + newLevel
					+ " con una experiencia de " + newXp + "!");
		}
		return leveledUp;

	}

	/**
	 * Internal method used when a pokemon can gain a new skill.
	 * 
	 * @param pokemon
	 * @param userPokemon If true, it prints information and it lets the user do
	 *                    choices. Otherwise, it will choose a random skill.
	 */
	private static void learnSkill(Pokemon pokemon, boolean userPokemon) {
		if (userPokemon) {
			System.out.print(
					pokemon.getName() + " puede aprender una nueva habilidad. ");
		}
		try {
			Map<Integer, Skill> availableSkills = getAllSkillsByType(pokemon.getType());
			// Next, from this temporary list of skills, we remove the ones the pokemon
			// already has.
			for (Skill alreadyLearnedSkill : pokemon.getSkills()) {
				availableSkills.remove(alreadyLearnedSkill.getId());
			}
			if (availableSkills.isEmpty()) {
				if (userPokemon) {
					System.out
							.println("Lamentablemente, no hay habilidades disponibles para que aprenda este pokemon.");
				}
			} else {

				if (userPokemon) {
					System.out.println(

							"Estas son las habilidades disponibles; ¿cual quieres que aprenda " + pokemon.getName()
									+ "?");

					for (Skill skill : availableSkills.values()) {
						System.out.println("\t" + skill.toString());
					}
				}
				int selectedSkillId;
				if (userPokemon) {
					selectedSkillId = UtilsAndConstants.readIntFromKeyboard();
				} else {
					Random random = new Random(System.currentTimeMillis());

					selectedSkillId = (int) availableSkills.keySet().toArray()[random.nextInt(0,
							availableSkills.size())];
				}
				// Ask again for the skill if a wrong one was selected
				if (userPokemon) {
					while (!availableSkills.containsKey(selectedSkillId)) {
						System.out.println(
								"No existe la skill seleccionada. Estas son las habilidades disponibles; ¿cual quieres que aprenda "
										+ pokemon.getName() + "?");
						for (Skill skill : availableSkills.values()) {
							System.out.println("\t" + skill.toString());
						}
						selectedSkillId = UtilsAndConstants.readIntFromKeyboard();
					}
				}
				Skill toLearn = null;
				toLearn = availableSkills.get(selectedSkillId);
				pokemon.getSkills().add(toLearn);
				if (userPokemon) {
					System.out.println("¡" + pokemon.getName() + " ha aprendido " + toLearn.getName() + "!");
				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("No hay habilidades disponibles. ¿Está el archivo correcto?");
		}

	}

	/**
	 * It returns all of the skills by type (i.e, generic and the type of the
	 * pokemon
	 * 
	 * @param type
	 * @return
	 * @throws FileNotFoundException
	 */
	private static Map<Integer, Skill> getAllSkillsByType(int type) throws FileNotFoundException {
		Map<Integer, Skill> result = new HashMap<Integer, Skill>();
		for (Entry<Integer, Skill> entry : getAllSkills().entrySet()) {
			int id = entry.getKey();
			Skill skill = entry.getValue();
			if (skill.getType() == UtilsAndConstants.TYPE_GENERIC || skill.getType() == type) {
				result.put(id, skill);
			}
		}
		return result;
	}

	private static Map<Integer, Skill> getAllSkills() throws FileNotFoundException {
		// If we did never read the skills file, we do it and we store it. Otherwise,
		// we use the one we stored before (we do not read it again).

		if (allSkills == null) {

			allSkills = new HashMap<Integer, Skill>();

			String fullPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
					+ UtilsAndConstants.FILE_SKILLS_LIST;
			Scanner scanner = new Scanner(new File(fullPath));

			// we ignore the header; we already know the list of parameters, so we advance.
			// We go line by line reading each skill.
			scanner.nextLine(); // ignore first line
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				String[] lineComponents = nextLine.split(",");
//					id, name, type, power, cost
				int id = Integer.parseInt(lineComponents[0].trim());
				String name = lineComponents[1].trim();
				int type = Integer.parseInt(lineComponents[2].trim());
				double powerMultiplier = Double.parseDouble(lineComponents[3].trim());
				int cost = Integer.parseInt(lineComponents[4].trim());
				Skill thisLineSkill = new Skill(id, type, name, powerMultiplier, cost);
				allSkills.put(id, thisLineSkill);
			}
		}
		return allSkills;
	}

	/**
	 * The first time it is called, it will read all of the pokemon definitions from
	 * the files in {@link FilesConstants} and cache it internally.
	 * 
	 * @return A copy of the full list of pokemon - with cloned pokemons; i.e,
	 *         modifying any pokemon in the list does not have any effect in the
	 *         cached version in this class.
	 * @throws FileNotFoundException
	 */
	public static Map<Integer, Pokemon> getAllPokemons() throws FileNotFoundException {
		// If we did never read the pokemons file, we do it and we store it. Otherwise,
		// we give A COPY of the one we have - not the original one, but a copy, so the
		// pokemons in the original one are never changed and they remain as a template.

		if (allPokemons == null) {

			allPokemons = new HashMap<Integer, Pokemon>();

			String fullPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
					+ UtilsAndConstants.FILE_POKEMON_LIST;
			Scanner scanner = new Scanner(new File(fullPath));

			// we ignore the header; we already know the list of parameters, so we advance.
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				String[] lineComponents = nextLine.split(",");
//			id, name, hp, mp, type, power, initial_skill
				// trim method removes whitespaces around the string
				int id = Integer.parseInt(lineComponents[0].trim());
				String name = lineComponents[1].trim();
				int hp = Integer.parseInt(lineComponents[2].trim());
				int mp = Integer.parseInt(lineComponents[3].trim());
				int type = Integer.parseInt(lineComponents[4].trim());
				int power = Integer.parseInt(lineComponents[5].trim());
				int initialSkill = Integer.parseInt(lineComponents[6].trim());
				List<Skill> initialSkills = new ArrayList<Skill>();
				initialSkills.add(getAllSkills().get(initialSkill));
				Pokemon thisLinePokemon = new Pokemon(id, name, hp, mp, hp, mp, 1, 0, type, power, initialSkills);
				allPokemons.put(id, thisLinePokemon);
			}
		}
		Map<Integer, Pokemon> cloned = new HashMap<Integer, Pokemon>();
		for (Integer key : allPokemons.keySet()) {
			cloned.put(key, allPokemons.get(key).clone());
		}
		return cloned;
	};

}
