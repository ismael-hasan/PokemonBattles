package com.pokemonfights.pokemons;

import java.util.ArrayList;
import java.util.List;

import com.pokemonfights.main.UtilsAndConstants;

public class Pokemon {

	private Integer id;

	private String name;

	// Max values at the current level
	private int maxHp;
	private int maxMp;

	// Current values
	private int currentHp;
	private int currentMp;

	// XP-related vars
	private int level;
	private int xp;

	// As defined in @PokemonConstants
	private int type;

	// Attack related.
	private int power;
	private List<Skill> skills;

	public Pokemon(Integer id, String name, int maxHp, int maxMp, int currentHp, int currentMp, int level,
			int xp, int type, int power, List<Skill> skills) {
		super();
		this.id = id;
		this.name = name;
		this.maxHp = maxHp;
		this.maxMp = maxMp;
		this.currentHp = currentHp;
		this.currentMp = currentMp;
		this.level = level;
		this.xp = xp;
		this.type = type;
		this.power = power;
		this.skills = skills;
	}


	@Override
	public String toString() {
		String skillsAsString = "";
		for (Skill skill:skills) {
			skillsAsString+=", " + skill.toString();
		}
		// We delete the initial ", " 
		skillsAsString = skillsAsString.substring(2);
		return "(" + id + ") " + name + "\n\tVida: " + currentHp + "/" + maxHp + "\n\tEnergía: " + currentMp + "/" + maxMp + "\n\tNivel: " + level + "\n\tExperiencia: " + xp + ", tipo: "
				+ UtilsAndConstants.getTypeText(type) + "\n\tAtaque: " + power + "\n\tHabilidades: " + skillsAsString;
	}
	
	@Override
	public Pokemon clone() {
		List<Skill> skills = new ArrayList<Skill>();
		skills.addAll(this.skills);
		return new Pokemon(id, name, maxHp, maxMp, currentHp, currentMp, level, xp, type, power, skills);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getMaxMp() {
		return maxMp;
	}

	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public int getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(int currentMp) {
		this.currentMp = currentMp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	

}
