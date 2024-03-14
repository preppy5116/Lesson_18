package sbrt.preppy.lesson_18.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompositionEntry {
    private Ingredient ingredientRef;
    private Double amount;
    private Unit unitRef;

    public CompositionEntry() {
    }

    public CompositionEntry(Ingredient ingredientRef, Double amount, Unit unitRef) {
        this.ingredientRef = ingredientRef;
        this.amount = amount;
        this.unitRef = unitRef;
    }

    @Override
    public String toString() {
        return "CompositionEntry{" +
                "ingredientRef=" + ingredientRef +
                ", amount=" + amount +
                ", unitRef=" + unitRef +
                '}';
    }
}
