package sbrt.preppy.lesson_18.Dao;

import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.model.Ingredient;

import java.util.List;

@Component
public interface IngredientDao {
    void insert(Ingredient ingredient);

    Ingredient getByPK(Integer primaryKey);

    List<Ingredient> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Ingredient ingredient);
}
