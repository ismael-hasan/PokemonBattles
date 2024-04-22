# Pokemon fights 

## Description
This is a pokemon-battle environment set up in Liceo. The entry point of the game is the `ControlCenter` class.

The main menu allows you to:  
1. Start a new game
2. Load a saved game (not implemented)
0. Exit 

When you start a new game, professor Yisus will assign a new pokemon to you. From that moment on, you can: 
1. Check the status of your pokemons
2. Search for a random fight.
    - At the start, the fight will be of the same level of your pokemon. 
    - As you gain more pokemon and level up, the fights will have more random levels.
3. Check your items.
    - You start the game with 3 pokeballs and 3 potions.
4. Heal a pokemon with a potion.
5. Go into the tournament (not implemented).
6. Save game (not implemented).
0. Exit game (go back to the main menu). 

### Fights
When you start a fight, you can choose a pokemon from your list to start. 
The battle will have rounds in which you do one action, and your opponent does one action. 

Your options in the fight are: 

0. Change your pokemon (beware, your opponent will attack the new pokemon).
1. Retreat - there is a chance to be able to escape and end the fight.
2. Heal pokemon - if you have potions, you can heal your pokemon. Beware, if you do not have potions you will lose the action!
3. Throw pokeball - you can try to capture the enemy pokemon. The lower its health, the more likely it is that you capture it. 
4..X. Apply a specific skill. You do the base damage of your pokemon multiplied by the skill. NOTE: _skills affinity is not implemented yet_. Be careful, if you try to do a skill with a higher cost than your current MP you will lose the action!

After you do your action, if you did not manage to retreat, or you did not defeat the enemy, or you did not capture the enemy, it attacks.
Enemies will randomly select an attack from the available ones. There is a chance that the enemy will be confused and fail the attack - the higher the level of the enemy, the less chances to be confused.   

#### Fight end
The fight ends if: 
- You retreat
- You defeat the opponent
- You capture the opponent
- Your opponent defeats you (you do not have any pokemon left). *This means GAME OVER*.

If you were able to defeat your opponent or capture it: 
- There is a chance to gain a new pokeball
- There is a chance to gain a new potion
- The experience gained is distributed among all of the pokemon that participated. Note that if a pokemon was defeated, its share of experience is lost, not distributed among the others. 

## Pokemons and skills
Every pokemon has several attributes:
- id: an unique id (within your pokemon list)
- name: the name of the pokemon 
- hp: the amount of health
- mp: the amount of energy
- skills: a list of available skills
- a type: electric, earth... 

The game has several skills. Each skill has: 
- id: an unique id
- name: the name of the skill.
- type: the nature of the skill
- power_multiplier: how much damage it does (it multiplies the base attack power of the pokemon that uses it)
- cost: how many mp are needed to use it.

### Leveling up
After each pokemon gains experience, it can level up if it gained enough experience.
Currently, the game can lead you up to level 10. Each 3 levels, the pokemon can gain a new skill (generic, or from its own type). If there are no new skills that it can learn, learning a skill is lost (it is not staged for later).
If a pokemon gets a new level, its hp and mp are replenished. 

### Game end
The game finishes when all your pokemon are defeated, or if you finish the tournament (to be implemented). 

## File configuration
The game uses 2 CSV file for configuration (comma-separated values, see [CSV in Spanish wikipedia](https://es.wikipedia.org/wiki/Valores_separados_por_comas)).
You can add more pokemons and skills via these files. Note that all of the skills referenced by the pokemons MUST exist.
 
The first one is _resources/PokemonList.csv_: 
```
id, name, hp, mp, type, power, initial_skill
0, Pikachu, 50, 50, 0, 30, 3
1, Charmander, 60, 40, 3, 35, 3
2, Bulbasur, 40, 60, 4, 25, 3
```

The second one is _resources/SkillList.csv_
```
id, name, type, power_multiplier, cost
0, impactrueno, 0, 1.5, 35
1, mechero, 3, 1.5, 35
2, enredar, 4, 1.5, 35
3, golpetazo, 6, 1, 0
```

If you want to add more pokemons to the base list, and more skills, you can add more lines to these files. Note that the changes are only applied if you restart the application.

## Constants configuration
There are some constants used during the game hard-coded in Java files. These include the list of elemental types, the XP required to advance to the next level, how much XP an enemy gives, what are the chances of winning pokeballs and potions...


## Structure
The project contains several packages/folders.
 
### `com.pokemonfights.exceptions`
Some exceptions created for specific conditions; for instance, when you try to heal a pokemon but do not have potions left. 

### `com.pokemonfights.main`
*ControlCenter* - it is the entry point of the application. It manages all of the interaction, menus, fights...
*GameStatus* - a class that contains the full game status - what are your pokemons, their stats, etc. When you start a new game, the application creates a new GameStatus object to manage the progress.
*PokemonManager* - an utility class to manage game-related data. It reads the pokemon and skill files to cache them locally, it offers methods to level your pokemon via XP gain...
*UtilsAndConstants* - some hard-coded configuration and generic methods (like printing a list of pokemons, reading data from keyboard...).

### `com.pokemonfights.pokemon`
*Pokemon* - a Java class representing a pokemon. Note the use of the `clone` method to make a copy of a pokemon. 
*Skill* - a Java class representing a skill.

## Tasks
The game is not finished! There are several tasks proposed; the recommendation is doing them in order. 

### Get familiar with the game
- Play the game. 
- Change some constants and see what is the effect. 
- Add more pokemon and skills.
- Check where the created exceptions are used, and understand what is their goal - why they are used in that way.
- Get familiar with the use of Map and List in the code. 
- Understand how the CSV files are read, and its contents stored in variables.
- Understand what the GameStatus contains, and how it is used. For instance, the GameStatus contains a list of the pokemons that the player has, so each time we have to show the pokemon list we get it from the GameStatus. 
- **Spend enough time doing all of the previous tasks**.
- 

### Fix a bug
When you tried the game you may have realized that when several Pokemon participate in a fight, only the Pokemon selected first gets the XP - it is not shared among all as explained _Fight End_ . Try to fix this intentional (cough, cough) bug so it works as explained in that section. 
 
### Save/load game
All of the info in the current game is stored in an object of type GameStatus. 
- Figure out a way to store all of this information in a file.
- The game must allow multiple files with different names. 
- Create a way to list of all of the saved games, and to start the game from a saved game - i.e, a way to load into the GameStatus the information previously stored in a file.

### Add MP potions
Currently, only health potions are used (they replenish both hp and mp). There should be two types of potions, one for HP and one for MP.

### Create achievements 
For instance, create achievements for "Captured all types of pokemon", "Got a pokemon to level 3", "Got a pokemon with 4 skills", "Defeated a higher level pokemon", "Won the tournament"... 
The obtained achievements should be stored in the GameStatus - so, when you save the game, the achievements are stored.
 
### Get constants from files.
Currently, some constants like the XP scaling is hard-coded. Create a configuration file with all of these properties, and read it when the application starts. 
The goal is that to change some configuration we just need to change a text file, we do not need to change Java code. 
Choose the appropiate format for the file. Typical formats for configuration are: 
- Properties files
```
property1=value
name=pokemon game
```
- [yml](https://www.redhat.com/es/topics/automation/what-is-yaml) 
- [JSON](https://www.w3schools.com/whatis/whatis_json.asp)


### Create skills with multiple types
Each pokemon can only learn generic or its own type skills. 
Create skills with multiple types, so for instance, a skill is available for multiple types of pokemon.  

### Add elemental affinity
Currently, damage is plain, solely based on the skill damage boost. Add elemental affinity and weaknesses. For instance, fire attacks should be more powerful when done against a plant pokemon.

### Add locations
The GameStatus contains a location - where the player is at the moment. 
Add more locations, so: 
- The player has the option to move to another location. 
- Each location will have preferred pokemons for the random encounters. For instance, maybe Pikachus appear more often in "Aula 6".
- Add action-based locations. 
    - For instance, at the nursery, there may be an option of using just one health potion to heal all of the pokemon. Be creative on this.  

### Skills limit
Currently, there is not a limit to the number of skills a pokemon can have. 
Limit this to 4. Also, when at the limit, if a Pokemon can learn a new skill it can decide to replace an existing one. 
 
### Tournament
The player can enter a tournament. 
- The tournament has 10 levels. When you enter a tournament battle, you continue from before.
- Before each fight, the player will select 3 pokemons, and it can only use those ones.
- Each battle is against 3 pokemons. The level of this must be around the level of the current fight: e.g, if you are playing level 5, you should face pokemons with levels between 4 and 6.
- The tournament does not give XP, potions or pokeballs. 
- Potions are not allowed in the tournament.
 

### Create better skills
- Create skills with status effects. For instance, a skill that does not do damage but temporarily reduces the enemy defenses, that puts the enemy to sleep for some rounds...
- Create support skills. For instance, skills that allow to heal a pokemon. These can be used inside or outside battles.  

### Go multiplayer! 
Allow multiplayer fights. For a multiplayer fight: 
- Load 2 different saved games as Player 1 and Player 2.
    - Remember, you can just copy a saved file from one computer to another one. 
- By turns, each player chooses 1 of its pokemon. 
- When both players have 3 pokemon, the battle start. 
- Potions are not allowed. 
