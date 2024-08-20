package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 *
 * The APICoffeeController is responsible for making coffee when a user submits
 * a request to do so.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON.
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APICoffeeController extends APIController {

	/**
	 * InventoryService object, to be autowired in by Spring to allow for
	 * manipulating the Inventory model
	 */
	@Autowired
	private InventoryService inventoryService;

	/**
	 * RecipeService object, to be autowired in by Spring to allow for manipulating
	 * the Recipe model
	 */
	@Autowired
	private RecipeService recipeService;

	/**
	 * POST method to make coffee by using a recipe name and amount paid.
	 *
	 * @param name    The name of the recipe.
	 * @param amtPaid The amount of money paid.
	 * @return ResponseEntity with the appropriate message and HTTP status.
	 */
	@PostMapping(BASE_PATH + "makecoffee/{name}")
	public ResponseEntity makeCoffee(@PathVariable("name") String name, @RequestBody int amtPaid) {
		final Recipe recipe = recipeService.findByName(name);
		if (recipe == null) {
			return new ResponseEntity(errorResponse("No recipe found for name: " + name), HttpStatus.NOT_FOUND);
		}

		if (amtPaid < recipe.getPrice()) {
			return new ResponseEntity(errorResponse("Not enough money paid"), HttpStatus.PAYMENT_REQUIRED);
		}

		final Inventory inventory = inventoryService.getInventory();
		if (!inventory.hasEnoughIngredients(recipe)) {
			return new ResponseEntity(errorResponse("Not enough ingredients available"), HttpStatus.CONFLICT);
		}

		int change = amtPaid - recipe.getPrice();
		boolean success = inventory.useIngredients(recipe);
		if (success) {
			inventoryService.save(inventory); // Persist changes to inventory
			return new ResponseEntity(successResponse("Coffee made successfully. Change: " + change + " cents"),
					HttpStatus.OK);
		} else {
			return new ResponseEntity(errorResponse("Unable to make coffee due to an unknown error"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
