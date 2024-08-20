package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.common.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIRecipeTest {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private MockMvc mvc;

	@Test
	@Transactional
	public void testRecipeCRUDOperations() throws Exception {
		recipeService.deleteAll();

		Recipe recipe = new Recipe("Mocha", 3, new ArrayList<>(List.of(new Ingredient("Coffee", 2),
				new Ingredient("Milk", 1), new Ingredient("Sugar", 1), new Ingredient("Chocolate", 1))));

		mvc.perform(
				post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(recipe)))
				.andExpect(status().isOk());
		
		mvc.perform(get("/api/v1/recipes/Mocha")).andDo(print()).andExpect(status().isOk());

		// Delete the recipe
		mvc.perform(delete("/api/v1/recipes/Mocha")).andExpect(status().isOk());

		// Confirm deletion
		Assertions.assertNull(recipeService.findByName("Mocha"));
	}

	@Transactional
	@Test
	public void testCreateRecipeThatExists() throws Exception {
		Recipe recipe1 = new Recipe("Latte", 5, Arrays.asList(new Ingredient("Coffee", 2), new Ingredient("Milk", 1)));
		recipeService.save(recipe1); // Manually save the recipe to simulate existing data

		Recipe recipe2 = new Recipe("Latte", 10, Arrays.asList(new Ingredient("Coffee", 3), new Ingredient("Milk", 2)));

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(recipe2))).andDo(print()).andExpect(status().isConflict());
	}

	@Test
	@Transactional
	public void testGetNonExistentRecipe() throws Exception {
		mvc.perform(get("/api/v1/recipes/NonExistentRecipe")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void testDeleteNonExistentRecipe() throws Exception {
		mvc.perform(delete("/api/v1/recipes/NonExistentRecipe")).andDo(print()).andExpect(status().isNotFound());
	}
	
	@Test
	@Transactional
	public void testTooManyRecipes() throws Exception {
		Recipe r1 = new Recipe("Mocha", 3, new ArrayList<>(List.of(new Ingredient("Coffee", 2),
				new Ingredient("Milk", 1), new Ingredient("Sugar", 1), new Ingredient("Chocolate", 1))));
		
		mvc.perform(
				post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());
		
		Recipe r2 = new Recipe("Latte", 10, Arrays.asList(new Ingredient("Coffee", 3), new Ingredient("Milk", 2)));
		
		mvc.perform(
				post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());
		
		Recipe r3 = new Recipe("Hot chocolate", 8, Arrays.asList(new Ingredient("Chocolate", 10), new Ingredient("Milk", 5)));
		mvc.perform(
				post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r3)))
				.andExpect(status().isOk());
		
		Recipe r4 = new Recipe("Pumpkin coffee", 12, Arrays.asList(new Ingredient("Pumpkin spice", 15), new Ingredient("Coffee", 5)));
		mvc.perform(
				post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r4)))
				.andExpect(status().isInsufficientStorage());
	}
	

}