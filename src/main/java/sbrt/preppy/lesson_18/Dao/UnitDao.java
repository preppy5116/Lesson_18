package sbrt.preppy.lesson_18.Dao;

import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.model.Unit;

import java.util.List;

@Component
public interface UnitDao {

    void insert(Unit unit);

    Unit getByPK(Integer primaryKey);

    List<Unit> getAll();

    void deleteByPK(Integer primaryKey);

    void update(Unit unit);
}