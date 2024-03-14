package sbrt.preppy.lesson_18.Dao.mapRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.Dao.IngredientDao;
import sbrt.preppy.lesson_18.Dao.UnitDao;
import sbrt.preppy.lesson_18.model.CompositionEntry;
import sbrt.preppy.lesson_18.model.Ingredient;
import sbrt.preppy.lesson_18.model.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CompositionEntryMapper implements RowMapper<CompositionEntry> {

    private final IngredientDao ingredientDao;
    private final UnitDao unitDao;

    @Autowired
    public CompositionEntryMapper(IngredientDao ingredientDao, UnitDao unitDao) {
        this.ingredientDao = ingredientDao;
        this.unitDao = unitDao;
    }

    @Override
    public CompositionEntry mapRow(ResultSet resultSet, int i) throws SQLException {

        Integer ingredientId = resultSet.getInt("ingredient_id");
        Integer unitId = resultSet.getInt("unit_id");

        Ingredient ingredient = ingredientDao.getByPK(ingredientId);
        Unit unit = unitDao.getByPK(unitId);

        CompositionEntry compositionEntry = new CompositionEntry();
        compositionEntry.setIngredientRef(ingredient);
        compositionEntry.setAmount(resultSet.getDouble("amount"));
        compositionEntry.setUnitRef(unit);

        return compositionEntry;
    }
}
