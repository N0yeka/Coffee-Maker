package edu.ncsu.csc.CoffeeMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)

public class TestDatabaseInteraction {
	@Autowired
	private RecipeService recipeService;

	@BeforeEach
	public void setup() {
		recipeService.deleteAll();
	}

	@Test
	@Transactional
	public void testRecipes() {
		List<Ingredient> l = new ArrayList<Ingredient>();

		l.add(new Ingredient("Coffee", 2));
		l.add(new Ingredient("Chocolate", 1));
		l.add(new Ingredient("Milk", 1));
		l.add(new Ingredient("Sugar", 1));

		Recipe r = new Recipe("Matcha", 3, l);

		recipeService.save(r);

		List<Recipe> dbRecipes = recipeService.findAll();
		Assertions.assertEquals(1, dbRecipes.size());

		Recipe dbRecipe = dbRecipes.get(0);
		Assertions.assertEquals(r.getName(), dbRecipe.getName());

		Recipe finalRecipe = recipeService.findByName("Matcha");
		Assertions.assertEquals(dbRecipe, finalRecipe);

		// updates
		finalRecipe.editPrice(15);
		finalRecipe.adjustOrAddIngredient("Sugar", 11); // Setting total to 11

		Assertions.assertEquals(15, finalRecipe.getPrice());
		Ingredient sugar = findIngredientByName(finalRecipe.getIngredients(), "Sugar");
		Assertions.assertNotNull(sugar);
		Assertions.assertEquals(11, sugar.getAmount());

		Assertions.assertEquals(1, recipeService.count());

		recipeService.save(finalRecipe);

	}

	@Test
	@Transactional
	public void testDeleteRecipe() {
		// Create a new Recipe
		Recipe recipe = new Recipe("Test Recipe", 50, Arrays.asList(new Ingredient("Coffee", 2),
				new Ingredient("Milk", 2), new Ingredient("Sugar", 2), new Ingredient("Chocolate", 2)));

		recipeService.save(recipe);

		Recipe foundRecipe = recipeService.findByName("Test Recipe");
		Assertions.assertNotNull(foundRecipe, "The recipe should exist in the database before deletion.");

		// Delete the Recipe
		recipeService.delete(foundRecipe);

		Recipe deletedRecipe = recipeService.findByName("Test Recipe");
		Assertions.assertNull(deletedRecipe, "The recipe should no longer exist in the database after deletion.");
	}

	@Test
	@Transactional
	public void testExistsById() {
		Recipe r = new Recipe("Exists Check", 5, Arrays.asList(new Ingredient("Coffee", 2),
				new Ingredient("Chocolate", 2), new Ingredient("Milk", 1), new Ingredient("Sugar", 3)));

		recipeService.save(r);
		Long savedId = r.getId();

		boolean exists = recipeService.existsById(savedId);
		Assertions.assertTrue(exists, "The recipe should exist by its ID.");
	}

	@Test
	@Transactional
	public void testFindById() {
		Recipe r = new Recipe("ID Finder", 6, Arrays.asList(new Ingredient("Coffee", 1), new Ingredient("Chocolate", 2),
				new Ingredient("Milk", 3), new Ingredient("Sugar", 4)));

		recipeService.save(r);
		Long savedId = r.getId();

		// Valid ID
		Recipe foundRecipe = recipeService.findById(savedId);
		Assertions.assertNotNull(foundRecipe, "Should be able to find the recipe by ID.");
		Assertions.assertEquals("ID Finder", foundRecipe.getName(), "The found recipe should have the correct name.");

		// Null ID
		Recipe nullIdRecipe = recipeService.findById(null);
		Assertions.assertNull(nullIdRecipe, "Finding by null ID should return null.");

		// Non-existing ID
		Recipe nonExistingIdRecipe = recipeService.findById(Long.MAX_VALUE);
		Assertions.assertNull(nonExistingIdRecipe, "Finding by a non-existing ID should return null.");
	}

	private Ingredient findIngredientByName(List<Ingredient> ingredients, String name) {
		for (Ingredient ingredient : ingredients) {
			if (ingredient.getName().equals(name)) {
				return ingredient;
			}
		}
		return null;
	}

}