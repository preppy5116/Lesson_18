package sbrt.preppy.lesson_18.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Unit {
    private Integer id;
    private String name;

    public Unit() {
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
