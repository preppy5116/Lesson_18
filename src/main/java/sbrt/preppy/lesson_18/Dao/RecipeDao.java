package sbrt.preppy.lesson_18.Dao;

import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.model.Recipe;

import java.util.List;

@Component
public interface RecipeDao {
    void insert(Recipe recipe);

    Recipe getByPK(Integer primaryKey);

    List<Recipe> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Recipe recipe);
}
