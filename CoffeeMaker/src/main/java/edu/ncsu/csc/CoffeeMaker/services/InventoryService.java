package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class InventoryService extends Service<Inventory, Long> {

	/**
	 * InventoryRepository, to be autowired in by Spring and provide CRUD operations
	 * on Inventory model.
	 */
	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	protected JpaRepository<Inventory, Long> getRepository() {
		return inventoryRepository;
	}

	/**
	 * Retrieves the singleton Inventory instance from the database, creating it if
	 * it does not exist.
	 *
	 * @return the Inventory, either new or fetched
	 */
	public synchronized Inventory getInventory() {
		List<Inventory> inventories = inventoryRepository.findAll();
		if (inventories.size() > 1) {
			throw new IllegalStateException("Multiple inventory instances detected.");
		}
		if (inventories.isEmpty()) {
			Inventory newInventory = new Inventory();
			inventoryRepository.save(newInventory);
			return newInventory;
		}
		return inventories.get(0);
	}

	/**
	 * Saves the specified inventory
	 *
	 * @param inventory the inventory to save
	 */
	public void save(Inventory inventory) {
		inventoryRepository.save(inventory);
	}

}
