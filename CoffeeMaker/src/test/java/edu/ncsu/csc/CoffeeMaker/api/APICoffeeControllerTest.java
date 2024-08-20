package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private RecipeService recipeService;

	@BeforeEach
	public void setup() {
		inventoryService.deleteAll();
		recipeService.deleteAll();

		final Inventory inventory = inventoryService.getInventory();
		inventory.adjustOrAddStock("Coffee", 200);
		inventory.adjustOrAddStock("Milk", 200);
		inventory.adjustOrAddStock("Sugar", 300);
		inventory.adjustOrAddStock("Chocolate", 200);
		inventoryService.save(inventory);

		List<Ingredient> l = new ArrayList<Ingredient>();

		l.add(new Ingredient("Milk", 2));
		l.add(new Ingredient("Coffee", 2));
		l.add(new Ingredient("Chocolate", 2));
		l.add(new Ingredient("Sugar", 1));

		Recipe r = new Recipe("Mocha", 15, l);
		recipeService.save(r);
	}

	@Test
	@Transactional
	public void testMakeCoffeeNoRecipeFound() throws Exception {
		mvc.perform(post("/api/v1/makecoffee/NonExistent").contentType(MediaType.APPLICATION_JSON).content("25"))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void testMakeCoffeeNotEnoughMoney() throws Exception {
		mvc.perform(post("/api/v1/makecoffee/Mocha").contentType(MediaType.APPLICATION_JSON).content("10"))
				.andExpect(status().isPaymentRequired());
	}

	@Test
	@Transactional
	public void testMakeCoffeeNotEnoughIngredients() throws Exception {
		Inventory inventory = inventoryService.getInventory();
		//empty inventory
		List<Ingredient> l = new ArrayList<Ingredient>();
		inventory.setIngredients(l);
		inventoryService.save(inventory);

		mvc.perform(post("/api/v1/makecoffee/Mocha").contentType(MediaType.APPLICATION_JSON).content("100"))
				.andExpect(status().isConflict());
	}

	@Test
	@Transactional
	public void testMakeCoffeeSuccess() throws Exception {
		Inventory inventory = inventoryService.getInventory();
		// undo setting coffee to 0 from the avbove test
		inventory.adjustOrAddStock("Coffee", 200);
		mvc.perform(post("/api/v1/makecoffee/Mocha").contentType(MediaType.APPLICATION_JSON).content("50"))
				.andExpect(status().isOk());
	}
}
