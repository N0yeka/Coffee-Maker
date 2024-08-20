package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = inventoryService.getInventory();
        inventory.adjustOrAddStock("Coffee", 200);
        inventory.adjustOrAddStock("Milk", 100);
        inventoryService.save(inventory);
    }

    /**
     * Used only for testing.
     *  
     * @param ingredients
     * @param name
     * @return
     */
    private Ingredient findIngredientByName(List<Ingredient> ingredients, String name) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(name)) {
                return ingredient;
            }
        }
        return null; 
    }

    @Test
    @Transactional
    public void testAddNewIngredient() {
        inventory.adjustOrAddStock("Vanilla", 50);
        Ingredient vanilla = findIngredientByName(inventory.getIngredients(), "Vanilla");
        Assertions.assertNotNull(vanilla, "Vanilla should be added to the inventory");
        Assertions.assertEquals(50, vanilla.getAmount(), "Vanilla amount should be 50");
    }

    @Test
    @Transactional
    public void testAdjustExistingIngredient() {
        inventory.adjustOrAddStock("Coffee", 50);
        Ingredient coffee = findIngredientByName(inventory.getIngredients(), "Coffee");
        Assertions.assertNotNull(coffee, "Coffee ingredient should exist in inventory");
        Assertions.assertEquals(250, coffee.getAmount(), "Coffee amount should be 250 units");
    }

    @Test
    @Transactional
    public void testHasExactlyEnoughIngredients() {
        Recipe recipe = new Recipe("Coffee Brew", 50, Arrays.asList(new Ingredient("Coffee", 200), new Ingredient("Milk", 100)));
        Assertions.assertTrue(inventory.hasEnoughIngredients(recipe),
                "There should be exactly enough ingredients for the recipe");
    }

    @Test
    @Transactional
    public void testHasInsufficientIngredients() {
        Recipe recipe = new Recipe("Deluxe Coffee", 75, Arrays.asList(new Ingredient("Coffee", 201), new Ingredient("Milk", 101)));
        Assertions.assertFalse(inventory.hasEnoughIngredients(recipe),
                "There should not be enough ingredients for the recipe");
    }

    @Test
    @Transactional
    public void testUseIngredientsSuccessfully() {
        Recipe recipe = new Recipe("Standard Brew", 25, Arrays.asList(new Ingredient("Coffee", 50), new Ingredient("Milk", 50)));
        Assertions.assertTrue(inventory.useIngredients(recipe),
                "Ingredients should be used successfully for the recipe");
        
        Ingredient coffee = findIngredientByName(inventory.getIngredients(), "Coffee");
        Assertions.assertNotNull(coffee, "Coffee ingredient should exist in inventory");
        Assertions.assertEquals(150, coffee.getAmount(), "Coffee amount should be reduced to 150 units");
        
        Ingredient milk = findIngredientByName(inventory.getIngredients(), "Milk");
        Assertions.assertNotNull(milk, "Milk ingredient should exist in inventory");
        Assertions.assertEquals(50, milk.getAmount(), "Milk amount should be reduced to 50 units");
    }

    @Test
    @Transactional
    public void testUseIngredientsUnsuccessfully() {
        Recipe recipe = new Recipe("Expensive Brew", 100, Arrays.asList(new Ingredient("Coffee", 250), new Ingredient("Milk", 150)));
        Assertions.assertFalse(inventory.useIngredients(recipe),
                "There should not be enough stock to use the ingredients for the recipe");
    }
    
    

}
