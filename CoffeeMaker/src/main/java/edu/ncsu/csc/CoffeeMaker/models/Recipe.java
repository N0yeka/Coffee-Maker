package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

	/** Recipe id */
	@Id
	@GeneratedValue
	private Long id;

	/** Recipe name */
	private String name;

	/** Recipe price */
	@Min(0)
	private Integer price;

	/** Ingredient List */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredients;

	/**
	 * Creates a default recipe for the coffee maker.
	 */
	public Recipe() {
		this.ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Creates a recipe with price for the coffee maker.
	 * 
	 * @param name The name to set the Recipe name to
	 * @param price The price to set the Recipe price to
	 */
	public Recipe(String name, int price) {
		setName(name);
		editPrice(price);
		this.ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Creates brand new recipe. Used more for testing.
	 * 
	 * @param name The name to set the Recipe name to
	 * @param price The price to set the Recipe price to
	 * @param ingredientList the Ingredients to set the Recipe Ingredients to
	 */
	public Recipe(String name, int price, List<Ingredient> ingredientList) {
		setName(name);
		editPrice(price);
		setIngredients(ingredientList);
	}

	/**
	 * Adjusts the amount of an existing ingredient or adds a new ingredient if it
	 * doesn't exist.
	 *
	 * @param name   The name of the ingredient.
	 * @param amount The amount to set or add.
	 */
	public void adjustOrAddIngredient(String name, int amount) {

		Ingredient existingIngredient = null;
		for (Ingredient ingredient : ingredients) {
			if (ingredient.getName().equals(name)) {
				existingIngredient = ingredient;
				break;
			}
		}

		if (existingIngredient != null) {
			// ingredient exists, update the amount
			existingIngredient.setAmount(amount);
		} else {
			// ingredient does not exist, add as new ingredient
			Ingredient newIngredient = new Ingredient(name, amount);
			ingredients.add(newIngredient);
		}
	}

	/**
	 * Gets the Ingredients of a Recipe
	 * @return The Ingredients of a Recipe
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Set ALL ingredients
	 * 
	 * @param ingredients The Ingredients to set all Recipe Ingredients to
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * Get the ID of the Recipe
	 *
	 * @return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Recipe (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Returns name of the recipe.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the recipe name.
	 *
	 * @param name The name to set.
	 */
	private void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the price of the recipe.
	 *
	 * @return Returns the price.
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Sets the recipe price.
	 *
	 * @param price The price to set.
	 */
	public void editPrice(final Integer price) {
		this.price = price;
	}

	/**
	 * Returns the name of the recipe.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", price=" + price + ", ingredients=" + ingredients + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(name, other.name);
	}

}
