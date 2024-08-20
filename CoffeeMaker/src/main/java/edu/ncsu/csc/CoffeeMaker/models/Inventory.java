package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

	/** id for inventory entry */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * List of Ingredients in the Inventory
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredients;

	/**
	 * Empty constructor for Hibernate
	 */
	public Inventory() {
		this.ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Returns the ID of the entry in the DB
	 *
	 * @return long
	 */
	@Override
	public Serializable getId() {
		return id;
	}

	/**
	 * Set the ID of the Inventory (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Returns the current inventory.
	 *
	 * @return list of inventory
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Sets the Ingredient list to a different given Ingredient list
	 *
	 * @param ingredients The new Ingredient list 
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * Adds a new ingredient to the inventory if it does not already exist. If it
	 * exists, creates a new ingredient and adds it.
	 *
	 * @param name The name of the Ingredient to add or adjust
	 * @param amountToAdjust The amount of the Ingredient to add or adjust
	 */
	public void adjustOrAddStock(String name, int amountToAdjust) {
		for (Ingredient item : ingredients) {
			if (item.getName().equals(name)) {
				item.setAmount(item.getAmount() + amountToAdjust);
				return; // If found, update the amount and return immediately
			}
		}
		// If not found, create and add a new ingredient to the inventory
		Ingredient newIngredient = new Ingredient(name, amountToAdjust);
		ingredients.add(newIngredient);
	}

	/**
	 * Checks if there are enough ingredients in the inventory to make the given
	 * recipe.
	 *
	 * @param recipe the recipe to check against the inventory
	 * @return true if all ingredients are available in sufficient quantity, false
	 *         otherwise
	 */
	public boolean hasEnoughIngredients(Recipe recipe) {
		for (Ingredient required : recipe.getIngredients()) {
			boolean found = false;
			for (Ingredient stock : ingredients) {
				if (stock.equals(required) && stock.getAmount() >= required.getAmount()) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false; // required ingredient is not found or not enough
			}
		}
		return true;
	}

	/**
	 * Uses the ingredients needed to make the specified recipe. Assumes that there
	 * are enough ingredients to make the recipe.
	 *
	 * @param recipe the recipe to make
	 * @return true if the recipe is made successfully, false otherwise
	 */
	public boolean useIngredients(Recipe recipe) {
		if (!hasEnoughIngredients(recipe)) {
			return false; // not enough ingredients to make the recipe
		}

		for (Ingredient required : recipe.getIngredients()) {
			for (Ingredient stock : ingredients) {
				if (stock.equals(required)) {
					int newAmount = stock.getAmount() - required.getAmount();
					stock.setAmount(newAmount);
				}
			}
		}
		return true;
	}

	/**
	 * Returns a string describing the current contents of the inventory.
	 *
	 * @return String representation of the inventory
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer("Inventory: [");
		for (Ingredient ingredient : ingredients) {
			buf.append("\n    ");
			buf.append(ingredient.getName());
		}
		buf.append("\n]");
		return buf.toString();
	}

}
