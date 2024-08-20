package edu.ncsu.csc.CoffeeMaker.common;

import com.google.gson.Gson;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Class for handy utils shared across all of the API tests
 *
 * @author Kai Presler-Marshall
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUtils {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	private static Gson gson = new Gson();

	/**
	 * Uses Google's GSON parser to serialize a Java object to JSON. Useful for
	 * creating JSON representations of our objects when calling API methods.
	 *
	 * @param obj to serialize to JSON
	 * @return JSON string associated with object
	 */
	public static String asJsonString(final Object obj) {
		return gson.toJson(obj);
	}

	@Test
	@Transactional
	public void testRecipePresenceTest() throws Exception {
		String recipeResponse = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		if (!recipeResponse.contains("Mocha")) {
			List<Ingredient> ingredients = Arrays.asList(new Ingredient("Coffee", 2), new Ingredient("Milk", 2),
					new Ingredient("Sugar", 2), new Ingredient("Chocolate", 2));
			Recipe mochaRecipe = new Recipe("Mocha", 9, ingredients);

			mvc.perform(
					post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mochaRecipe)))
					.andExpect(status().isOk());

			// Verify that the recipe is now present
			recipeResponse = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn()
					.getResponse().getContentAsString();
			Assertions.assertTrue(recipeResponse.contains("Mocha"));

			// Fetch the current inventory
			Inventory currentInventory = fetchCurrentInventory();
			// Add or adjust stock
			currentInventory.adjustOrAddStock("Coffee", 50);
			currentInventory.adjustOrAddStock("Milk", 50);
			currentInventory.adjustOrAddStock("Sugar", 50);
			currentInventory.adjustOrAddStock("Chocolate", 50);

			// Update the inventory
			mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(currentInventory))).andExpect(status().isOk());

			// Verify inventory after update
			String inventoryResponse = mvc.perform(get("/api/v1/inventory")).andExpect(status().isOk()).andReturn()
					.getResponse().getContentAsString();
			System.out.println("Inventory after update: " + inventoryResponse);

			mvc.perform(post("/api/v1/makecoffee/Mocha").contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString("100"))).andExpect(status().isOk());
		}
	}

	private Inventory fetchCurrentInventory() throws Exception {
		String inventoryJson = mvc.perform(get("/api/v1/inventory")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		return gson.fromJson(inventoryJson, Inventory.class);
	}
}
