package edu.ncsu.csc.CoffeeMaker.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIMappingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndexMapping() throws Exception {
        mockMvc.perform(get("/index"))
               .andExpect(view().name("index"));
        mockMvc.perform(get("/"))
               .andExpect(view().name("index"));
    }

    @Test
    public void testAddRecipePageMapping() throws Exception {
        mockMvc.perform(get("/recipe"))
               .andExpect(view().name("recipe"));
        mockMvc.perform(get("/recipe.html"))
               .andExpect(view().name("recipe"));
    }

    @Test
    public void testDeleteRecipeFormMapping() throws Exception {
        mockMvc.perform(get("/deleterecipe"))
               .andExpect(view().name("deleterecipe"));
        mockMvc.perform(get("/deleterecipe.html"))
               .andExpect(view().name("deleterecipe"));
    }

    @Test
    public void testInventoryFormMapping() throws Exception {
        mockMvc.perform(get("/inventory"))
               .andExpect(view().name("inventory"));
        mockMvc.perform(get("/inventory.html"))
               .andExpect(view().name("inventory"));
    }

    @Test
    public void testMakeCoffeeFormMapping() throws Exception {
        mockMvc.perform(get("/makecoffee"))
               .andExpect(view().name("makecoffee"));
        mockMvc.perform(get("/makecoffee.html"))
               .andExpect(view().name("makecoffee"));
    }

    @Test
    public void testAddRecipeFormMapping() throws Exception {
        mockMvc.perform(get("/addRecipe"))
               .andExpect(view().name("uc2"));
        mockMvc.perform(get("/uc2.html"))
               .andExpect(view().name("uc2"));
    }

    @Test
    public void testAddIngredientFormMapping() throws Exception {
        mockMvc.perform(get("/addIngredients"))
               .andExpect(view().name("uc7"));
        mockMvc.perform(get("/uc7.html"))
               .andExpect(view().name("uc7"));
    }
}