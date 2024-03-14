package sbrt.preppy.lesson_18.Dao;

import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.model.Composition;

import java.util.List;


public interface CompositionDao {
    void insert(Composition composition);

    Composition getByRecipeID(Integer recipeID);

    List<Composition> getAll();

    void deleteByRecipeID(Integer recipeID);

    List<Composition> searchByName(String name);
}
