package sbrt.preppy.lesson_18.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Composition {
    private List<Integer> idList;
    private Recipe recipeRef;
    private List<CompositionEntry> entryList = new ArrayList<>();

    public Composition() {
    }

    @Override
    public String toString() {
        return "Composition{" +
                "idList=" + idList +
                ", recipeRef=" + recipeRef +
                ", entryList=" + entryList +
                '}';
    }
}
