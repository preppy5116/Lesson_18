package sbrt.preppy.lesson_18.Dao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sbrt.preppy.lesson_18.Dao.mapRow.UnitMapper;
import sbrt.preppy.lesson_18.model.Unit;

import javax.sql.DataSource;
import java.util.List;

public class UnitDaoImpl implements UnitDao {
    private static final String QUERY_INSERT =
            "INSERT INTO Unit(name)\n" +
                    "VALUES (:name);";

    private static final String QUERY_GET_BY_PK =
            "SELECT id, name\n" +
                    "FROM Unit\n" +
                    "WHERE id=:id;";

    private static final String QUERY_GET_ALL =
            "SELECT id, name\n" +
                    "FROM Unit;";

    private static final String QUERY_DELETE_BY_PK =
            "DELETE FROM Unit\n" +
                    "WHERE id=:id;";

    private static final String QUERY_UPDATE =
            "UPDATE Unit\n" +
                    "SET name = :name\n" +
                    "WHERE id = :id;";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UnitMapper unitMapper;

    public UnitDaoImpl(DataSource dataSource, UnitMapper unitMapper) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.unitMapper = unitMapper;
    }

    @Override
    public void insert(Unit unit) {
        if (unit.getId() != null) {
            System.out.println("Ед. изм. id " + unit.getId() + " уже сохранена в базе");
            return;
        }
        SqlParameterSource param = new BeanPropertySqlParameterSource(unit);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = namedParameterJdbcTemplate.update(QUERY_INSERT, param, keyHolder);
        unit.setId(keyHolder.getKey().intValue());
        System.out.println("Добавлена " + result + " ед. изм.: " + unit.getName());
    }

    @Override
    public Unit getByPK(Integer primaryKey) {
        SqlParameterSource param = new MapSqlParameterSource("id", primaryKey);
        Unit unit = namedParameterJdbcTemplate.queryForObject(QUERY_GET_BY_PK, param, unitMapper);
        return unit;
    }

    @Override
    public List<Unit> getAll() {
        List<Unit> units = namedParameterJdbcTemplate.query(QUERY_GET_ALL, unitMapper);
        return units;
    }

    @Override
    public void deleteByPK(Integer primaryKey) {
        SqlParameterSource param = new MapSqlParameterSource("id", primaryKey);
        int result = namedParameterJdbcTemplate.update(QUERY_DELETE_BY_PK, param);
        System.out.println("Удалена " + result + " ед. изм. ");
    }

    @Override
    public void update(Unit unit) {
        if (unit.getId() == null ) return;
        SqlParameterSource param = new BeanPropertySqlParameterSource(unit);
        int result = namedParameterJdbcTemplate.update(QUERY_UPDATE, param);
        System.out.println("Обновлена " + result + " ед. изм. " + unit.getName());
    }
}