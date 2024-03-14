package sbrt.preppy.lesson_18.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.Dao.mapRow.IngredientMapper;
import sbrt.preppy.lesson_18.model.Ingredient;

import javax.sql.DataSource;
import java.util.List;

@Component
public class IngredientDaoImpl implements IngredientDao {
    private static final String QUERY_INSERT =
            "INSERT INTO Ingredient(name)\n" +
                    "VALUES (:name);";

    private static final String QUERY_GET_BY_PK =
            "SELECT id, name\n" +
                    "FROM Ingredient\n" +
                    "WHERE id=:id;";

    private static final String QUERY_GET_ALL =
            "SELECT id, name\n" +
                    "FROM Ingredient;";

    private static final String QUERY_DELETE_BY_PK =
            "DELETE FROM Ingredient\n" +
                    "WHERE id=:id;";

    private static final String QUERY_UPDATE =
            "UPDATE Ingredient\n" +
                    "SET name = :name\n" +
                    "WHERE id = :id;";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final IngredientMapper ingredientMapper;
    @Autowired
    public IngredientDaoImpl(DataSource dataSource, IngredientMapper ingredientMapper) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public void insert(Ingredient ingredient) {
        if (ingredient.getId() != null) {
            System.out.println("Ингредиент id " + ingredient.getId() + " уже сохранен в базе");
            return;
        }
        SqlParameterSource param = new BeanPropertySqlParameterSource(ingredient);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = namedParameterJdbcTemplate.update(QUERY_INSERT, param, keyHolder);
        ingredient.setId(keyHolder.getKey().intValue());
        System.out.println("Добавлен " + result + " ингредиент: " + ingredient.getName());
    }

    @Override
    public Ingredient getByPK(Integer primaryKey) {
        SqlParameterSource param = new MapSqlParameterSource("id", primaryKey);
        Ingredient ingredient = namedParameterJdbcTemplate.queryForObject(QUERY_GET_BY_PK, param, ingredientMapper);
        return ingredient;
    }

    @Override
    public List<Ingredient> getAll() {
        List<Ingredient> ingredients = namedParameterJdbcTemplate.query(QUERY_GET_ALL, ingredientMapper);
        return ingredients;
    }

    @Override
    public void deleteByPK(Integer primaryKey) {
        SqlParameterSource param = new MapSqlParameterSource("id", primaryKey);
        int result = namedParameterJdbcTemplate.update(QUERY_DELETE_BY_PK, param);
        System.out.println("Удален " + result + " ингредиент ");
    }

    @Override
    public void update(Ingredient ingredient) {
        if (ingredient.getId() == null) return;
        SqlParameterSource param = new BeanPropertySqlParameterSource(ingredient);
        int result = namedParameterJdbcTemplate.update(QUERY_UPDATE, param);
        System.out.println("Обновлен " + result + " ингредиент " + ingredient.getName());
    }
}
