package sbrt.preppy.lesson_18.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ingredient {
    private Integer id;
    private String name;

    public Ingredient() {
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
