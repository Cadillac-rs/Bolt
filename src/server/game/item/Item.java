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

/**
 * 
 * @author Stuart Murphy
 *
 */
public class Item {
	
	private int id;
	private int amount;
	
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void incrementAmount() {
		if(((long)amount + 1) > Integer.MAX_VALUE) {
			return;
		}
		amount++;
	}
	
	public void decrementAmount() {
		if((amount - 1) < 0) {
			return;
		}
		amount--;
	}
	
	public void incrementAmountBy(int amount) {
		if(((long)this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}
	
	public void decrementAmountBy(int amount) {
		if((this.amount - amount) < 1) {
			this.amount = 1;
		} else {
			this.amount -= amount;
		}
	}
	
	public ItemDefinition getDefinition() {
		return ItemDefinition.getDefinition(id);
	}

}