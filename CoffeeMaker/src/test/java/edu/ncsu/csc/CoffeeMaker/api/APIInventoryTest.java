package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIInventoryTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private InventoryService iService;

	@BeforeEach
	public void setup() {

		final Inventory ivt = iService.getInventory();

		ivt.adjustOrAddStock("Vanilla", 100);
		ivt.adjustOrAddStock("Cinnamon", 100);
		ivt.adjustOrAddStock("Pumpkin spice", 100);
		ivt.adjustOrAddStock("Coffee", 100);

		iService.save(ivt);
	}

	@Test
	@Transactional
	public void testGetInventory() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Transactional
	@Test
	public void testUpdateInventory() throws Exception {
		Inventory inventory = iService.getInventory();

		inventory.adjustOrAddStock("Milk", 50);

		System.out.println(TestUtils.asJsonString(inventory));

		mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(inventory))).andExpect(status().isOk());

		// Re-fetch the inventory to verify changes
		Inventory updatedInventory = iService.getInventory();
		boolean foundMilk = false;
		int milkAmount = 0;

		for (Ingredient ingredient : updatedInventory.getIngredients()) {
			if ("Milk".equals(ingredient.getName())) {
				foundMilk = true;
				milkAmount = ingredient.getAmount();
				break;
			}
		}

		Assertions.assertTrue(foundMilk, "Milk should be in the inventory.");
		Assertions.assertTrue(milkAmount == 50, "Milk amount should be increased by at least 50.");
	}
}
