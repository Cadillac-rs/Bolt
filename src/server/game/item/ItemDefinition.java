package server.game.item;
/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.util.Misc;

/**
 * 
 * @author Stuart Murphy
 *
 */
public class ItemDefinition {
	
	private static ItemDefinition[] definitions;
	
	public static ItemDefinition getDefinition(int id) {
		return definitions[id];
	}
	
	private int id;
	private String name;
	private String examine;
	private boolean noted;
	private boolean noteable;
	private boolean stackable;
	private int parentId;
	private int notedId;
	private boolean members;
	private int shopValue;
	private int highAlchValue;
	private int lowAlchValue;
	private int[] bonuses;
	private double weight;
	private int slot;
	private boolean fullMask;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExamine() {
		return examine;
	}
	
	public void setExamine(String examine) {
		this.examine = examine;
	}
	
	public boolean isNoted() {
		return noted;
	}
	
	public void setNoted(boolean noted) {
		this.noted = noted;
	}
	
	public boolean isNoteable() {
		return noteable;
	}
	
	public void setNoteable(boolean noteable) {
		this.noteable = noteable;
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getNotedId() {
		return notedId;
	}
	
	public void setNotedId(int notedId) {
		this.notedId = notedId;
	}
	
	public boolean isMembers() {
		return members;
	}
	
	public void setMembers(boolean members) {
		this.members = members;
	}
	
	public int getShopValue() {
		return shopValue;
	}
	
	public void setShopValue(int shopValue) {
		this.shopValue = shopValue;
	}
	
	public int getHighAlchValue() {
		return highAlchValue;
	}
	
	public void setHighAlchValue(int highAlchValue) {
		this.highAlchValue = highAlchValue;
	}
	
	public int getLowAlchValue() {
		return lowAlchValue;
	}
	
	public void setLowAlchValue(int lowAlchValue) {
		this.lowAlchValue = lowAlchValue;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public boolean isFullMask() {
		return fullMask;
	}

	public void setFullMask(boolean fullMask) {
		this.fullMask = fullMask;
	}

}
