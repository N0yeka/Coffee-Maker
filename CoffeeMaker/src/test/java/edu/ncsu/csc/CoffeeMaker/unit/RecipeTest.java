package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }
    
    /*
     * Test Recipe constructor without initial ingredients list
     */
    @Test
    @Transactional
    public void testRecipeConstructor1() {
    	Recipe recipe = new Recipe("Hot Chocolate", 10);
    	
    	Assertions.assertEquals("Hot Chocolate", recipe.getName(), 
    			"Recipe name should be Hot Chocolate, but is not");
    	Assertions.assertEquals(10, recipe.getPrice(), 
    			"Recipe price should be 10, but is not");
    }
    
    /*
     * Test Recipe constructor with initial ingredients list
     */
    @Test
    @Transactional
    public void testRecipeConstructor2() {
    	List<Ingredient> ingredients = new ArrayList<Ingredient>();
    	Ingredient milk = new Ingredient("Milk", 50);
    	Ingredient chocolate = new Ingredient("Chocolate", 75);
    	ingredients.add(milk);
    	ingredients.add(chocolate);
    	Recipe recipe = new Recipe("Hot Chocolate", 10, ingredients);
    	
    	Assertions.assertEquals("Hot Chocolate", recipe.getName(), 
    			"Recipe name should be Hot Chocolate, but is not");
    	Assertions.assertEquals(10, recipe.getPrice(), 
    			"Recipe price should be 10, but is not");
    	
    	Assertions.assertEquals(2, recipe.getIngredients().size(), 
    			"Amount of ingredients should be 2, but was not");
    	
    	Assertions.assertEquals("Milk", recipe.getIngredients().get(0).getName(), 
    			"First ingredient in the recipe should be Milk, but was not");
    	Assertions.assertEquals("Chocolate", recipe.getIngredients().get(1).getName(), 
    			"Second ingredient in the recipe should be Chocolate, but was not");
    }
    
    /*
     * Test adding a new ingredient and adjusting the amount of an existing ingredient
     */
    @Test
    @Transactional
    public void testAdjustOrAddIngredient() {
    	List<Ingredient> ingredients = new ArrayList<Ingredient>();
    	Ingredient pumpkin = new Ingredient("Pumpkin spice", 50);
    	Ingredient coffee = new Ingredient("Coffee", 75);
    	ingredients.add(pumpkin);
    	ingredients.add(coffee);
    	
    	Recipe recipe = new Recipe("Pumpkin Latte", 15);
    	recipe.setIngredients(ingredients);
    	
    	Assertions.assertEquals(2, recipe.getIngredients().size(), 
    			"There should be 2 ingredients in the recipe, but there are not");
    	
    	recipe.adjustOrAddIngredient("Cream", 5);
    	Assertions.assertEquals(3, recipe.getIngredients().size(), 
    			"There should be 3 ingredients in the recipe, but there are not");
    	
    	Assertions.assertEquals(5, recipe.getIngredients().get(2).getAmount(), 
    			"Amount of Cream should be 5, but was not");
    	
    	recipe.adjustOrAddIngredient("Pumpkin spice", 10);
    	Assertions.assertEquals(10, recipe.getIngredients().get(0).getAmount(), 
    			"Amount of Pumpkin spice should be 10, but was not");
    	
    }
    
    /*
     * Test getting and editing the price of a recipe
     */
    @Test
    @Transactional
    public void testGetPrice() {
    	Recipe recipe = new Recipe("Milkshake", 100);
    	Assertions.assertEquals(100, recipe.getPrice(), 
    			"Price of Milkshake recipe should be 100, but was not");
    	
    	recipe.editPrice(10);
    	Assertions.assertEquals(10, recipe.getPrice(), 
    			"Price of Milkshake recipe should be changed to 10, but was not");
    }
    
    /*
     * Test for equals method to further improve coverage
     */
    @Test
    @Transactional
    public void testEquals() {
    	Recipe r1 = new Recipe("Milkshake", 8);
    	Assertions.assertTrue(r1.equals(r1));
    	
    	Recipe r2 = new Recipe("Milkshake", 8);
    	Assertions.assertTrue(r1.equals(r2));
    	
    	Recipe r3 = null;
    	Assertions.assertFalse(r1.equals(r3));
    	
    	Ingredient milk = new Ingredient("Milk", 50);
    	Assertions.assertFalse(r1.equals(milk));
    }
}
