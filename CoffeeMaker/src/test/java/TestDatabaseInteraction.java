
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

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
	@Autowired
	private RecipeService recipeService;
	/*
	@Test
	@Transactional
	public void testRecipes(){
	    recipeService.deleteAll();
	    
	    Recipe r = new Recipe();
	    r.setName("Matcha");
	    r.setCoffee(2);
	    r.setChocolate(1);
	    r.setMilk(1);
	    r.setSugar(1);
	    r.setPrice(3);
	    
	    recipeService.save(r);
	    
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

	    Assertions.assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);

	    Assertions.assertEquals(r.getName(), dbRecipe.getName());
	    // Find by name
	    Recipe finalRecipe = recipeService.findByName("Matcha");
	    
	    Assertions.assertEquals(dbRecipe, finalRecipe);
	    
	    recipeService.save( r );
	    
	    // updates
	    r.setPrice(15);
	    r.setSugar(12);
	    recipeService.save(r);
	    
	    int testSugarNum = dbRecipes.get(0).getSugar();
	    int testPriceNum = dbRecipes.get(0).getPrice();
	    
	    Assertions.assertEquals(r.getSugar(), testSugarNum);
	    Assertions.assertEquals(r.getPrice(), testPriceNum);
	    
	    Assertions.assertEquals(1,  recipeService.count());
	    
	    Assertions.assertEquals(15,  (int) ((Recipe) recipeService.findAll().get(0)).getPrice());
	    
	}
	
	 @Test
	    @Transactional
	    public void testDeleteRecipe() {
	        // Create a new Recipe
	        Recipe recipe = new Recipe();
	        recipe.setName("Test Recipe");
	        recipe.setCoffee(2);
	        recipe.setMilk(2);
	        recipe.setSugar(2);
	        recipe.setChocolate(2);
	        recipe.setPrice(50);

	        recipeService.save(recipe);

	        Recipe foundRecipe = recipeService.findByName("Test Recipe");
	        Assertions.assertNotNull(foundRecipe, "The recipe should exist in the database before deletion.");

	        // Delete the Recipe
	        recipeService.delete(foundRecipe);

	        // Verify the Recipe no longer exists
	        Recipe deletedRecipe = recipeService.findByName("Test Recipe");
	        Assertions.assertNull(deletedRecipe, "The recipe should no longer exist in the database after deletion.");
	    }
	    */

	
}
