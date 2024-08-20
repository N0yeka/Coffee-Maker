package DataGeneration;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {
    
    @Autowired
    private RecipeService recipeService;
    
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }
    
    
    @Test
    @Transactional
    public void createRecipe () {
    	recipeService.deleteAll();
    	
        final Recipe r1 = new Recipe();

        r1.adjustOrAddIngredient("Coffee", 10 );
        r1.adjustOrAddIngredient("Pumpkin spice", 3);
        r1.adjustOrAddIngredient("Milk", 2 );
        
        recipeService.save( r1 );
        
        printRecipes();
    }
    
    private void printRecipes () {
        for ( DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}