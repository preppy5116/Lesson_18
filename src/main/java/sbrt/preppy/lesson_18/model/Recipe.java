package sbrt.preppy.lesson_18.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe {
    private Integer id;
    private String name;
    private String description;

    public Recipe() {
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id = " + id +
                ", name ='" + name + '\'' +
                ", description ='" + description + '\'' +
                '}';
    }

}
