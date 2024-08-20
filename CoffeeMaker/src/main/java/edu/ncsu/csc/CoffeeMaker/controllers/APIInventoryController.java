package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APIInventoryController extends APIController {

	/**
	 * InventoryService object, to be autowired in by Spring to allow for
	 * manipulating the Inventory model
	 */
	@Autowired
	private InventoryService service;

	/**
	 * REST API endpoint to provide GET access to the CoffeeMaker's singleton
	 * Inventory. This will convert the Inventory to JSON.
	 *
	 * @return response to the request
	 */
	@GetMapping(BASE_PATH + "/inventory")
	public ResponseEntity getInventory() {
		final Inventory inventory = service.getInventory();
		return new ResponseEntity(inventory, HttpStatus.OK);
	}

    /**
     * Updates the entire inventory based on the provided Inventory object.
     * This endpoint will overwrite the existing inventory with the new one provided.
     *
     * @param newInventory The complete new state of the inventory.
     * @return ResponseEntity with the updated inventory or an error message.
     */
	@PutMapping(BASE_PATH + "inventory")
	public ResponseEntity updateInventory(@RequestBody Inventory newInventory) {
		
		Inventory currentInventory = service.getInventory();
		
		currentInventory.setIngredients(newInventory.getIngredients());
		
	    service.save(currentInventory);
	    return new ResponseEntity(currentInventory, HttpStatus.OK);
	}
}
