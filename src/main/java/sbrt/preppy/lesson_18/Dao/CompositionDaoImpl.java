package sbrt.preppy.lesson_18.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sbrt.preppy.lesson_18.Dao.mapRow.CompositionEntryMapper;
import sbrt.preppy.lesson_18.model.Composition;
import sbrt.preppy.lesson_18.model.CompositionEntry;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompositionDaoImpl implements CompositionDao {
    private static final String QUERY_INSERT =
            "INSERT INTO Composition(recipe_id, ingredient_id, amount, unit_id)\n" +
                    "VALUES (:recipe_id, :ingredient_id, :amount, :unit_id);";

    private static final String QUERY_GET_BY_RECIPE_ID =
            "SELECT ingredient_id, amount, unit_id\n" +
                    "FROM Composition\n" +
                    "WHERE recipe_id=:recipe_id;";

    private static final String QUERY_GET_COMPOSITIONS_ID_BY_RECIPE_ID =
            "SELECT id FROM Composition WHERE recipe_id=:recipe_id;";

    private static final String QUERY_GET_ALL_RECIPE_ID =
            "SELECT id FROM Recipe;";

    private static final String QUERY_DELETE =
            "DELETE FROM Composition\n" +
                    "WHERE recipe_id=:recipe_id;";

    private static final String QUERY_SEARCH_BY_NAME =
            "SELECT ID\n" +
                    "FROM RECIPE\n" +
                    "WHERE NAME LIKE :name;";


    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final CompositionEntryMapper compositionEntryMapper;

    private final IngredientDao ingredientDao;
    private final RecipeDao recipeDao;
    private final UnitDao unitDao;

    @Autowired
    public CompositionDaoImpl(DataSource dataSource, CompositionEntryMapper compositionEntryMapper,
                              IngredientDao ingredientDao, RecipeDao recipeDao, UnitDao unitDao) {

        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        this.compositionEntryMapper = compositionEntryMapper;

        this.ingredientDao = ingredientDao;
        this.recipeDao = recipeDao;
        this.unitDao = unitDao;
    }

    @Transactional
    @Override
    public void insert(Composition composition) {
        if (composition.getIdList() != null) {
            System.out.println("Состав рецепта id " + composition.getRecipeRef().getId() + " уже сохранен в базе");
            return;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        List<Integer> idList = new ArrayList<>();
        int result = 0;

        recipeDao.insert(composition.getRecipeRef());

        for (CompositionEntry entry : composition.getEntryList()) {

            ingredientDao.insert(entry.getIngredientRef());
            unitDao.insert(entry.getUnitRef());

            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("recipe_id", composition.getRecipeRef().getId())
                    .addValue("ingredient_id", entry.getIngredientRef().getId())
                    .addValue("amount", entry.getAmount())
                    .addValue("unit_id", entry.getUnitRef().getId());

            result += namedParameterJdbcTemplate.update(QUERY_INSERT, param, keyHolder);
            idList.add(keyHolder.getKey().intValue());
        }

        composition.setIdList(idList);

        System.out.println("Добавлен состав рецепта: '" + composition.getRecipeRef().getName()
                + "', кол-во ингредиентов: " + result);
    }

    @Override
    public Composition getByRecipeID(Integer recipeID) {
        SqlParameterSource param = new MapSqlParameterSource("recipe_id", recipeID);

        Composition composition = new Composition();
        composition.setRecipeRef(recipeDao.getByPK(recipeID));

        //Получаем состав рецепта
        List<CompositionEntry> compositionEntryList = namedParameterJdbcTemplate.query(QUERY_GET_BY_RECIPE_ID, param, compositionEntryMapper);
        composition.setEntryList(compositionEntryList);

        //Получаем айдишники отдельно
        List<Integer> idList = namedParameterJdbcTemplate.queryForList(QUERY_GET_COMPOSITIONS_ID_BY_RECIPE_ID, param, Integer.class);
        composition.setIdList(idList);

        return composition;
    }

    @Override
    public List<Composition> getAll() {
        List<Composition> compositionList = new ArrayList<>();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<Integer> recipeIdList = jdbcTemplate.queryForList(QUERY_GET_ALL_RECIPE_ID, Integer.class);

        // Перебираем все рецепты и по id получаем полные рецепты
        for (Integer recipeId : recipeIdList) {
            compositionList.add(getByRecipeID(recipeId));
        }

        return compositionList;
    }

    @Transactional
    @Override
    public void deleteByRecipeID(Integer recipeID) {
        // Удаляем рецепт из Recipe и его составляющие из Composition
        SqlParameterSource param = new MapSqlParameterSource("recipe_id", recipeID);
        int result = namedParameterJdbcTemplate.update(QUERY_DELETE, param);

        recipeDao.deleteByPK(recipeID);
        System.out.println("Удалены составляющие рецепта id: " + recipeID + " (" + result + " строк)");
    }

    @Override
    public List<Composition> searchByName(String name) {
        SqlParameterSource param = new MapSqlParameterSource("name", "%" + name + "%");
        List<Integer> idList = namedParameterJdbcTemplate.queryForList(QUERY_SEARCH_BY_NAME, param, Integer.class);

        List<Composition> compositionList = new ArrayList<>();
        for (Integer id : idList) {
            compositionList.add(getByRecipeID(id));
        }

        return compositionList;
    }
}
