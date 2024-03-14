package sbrt.preppy.lesson_18.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbrt.preppy.lesson_18.Dao.*;
import sbrt.preppy.lesson_18.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Отвечает за запуск книги рецептов
 */
@Component
public class ServiceBook {
    private final CompositionDao compositionDao;
    private final IngredientDao ingredientDao;
    private final RecipeDao recipeDao;
    private final UnitDao unitDao;

    private Scanner scanner = new Scanner(System.in);

    private static final String MAIN_MENU =
            "\n# Книга рецептов #\n" +
                    "1. Поиск рецепта по имени или части имени;\n" +
                    "2. Удаление рецепта;\n" +
                    "3. Добавление рецепта;\n" +
                    "4. Выход из программы.\n" +
                    "\nВведите номер операции: ";


    @Autowired
    public ServiceBook(CompositionDao compositionDao, IngredientDao ingredientDao, RecipeDao recipeDao, UnitDao unitDao) {
        this.compositionDao = compositionDao;
        this.ingredientDao = ingredientDao;
        this.recipeDao = recipeDao;
        this.unitDao = unitDao;

    }

    public void start() {
        while (true) {
            System.out.println(MAIN_MENU);

            int userInput = 0;
            try {
                userInput = Integer.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите цифру 1-4");
                continue;
            }

            switch (userInput) {
                case 1:
                    searchRecipe();
                    break;
                case 2:
                    deleteRecipe();
                    break;
                case 3:
                    addRecipe();
                    break;
                case 4:
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Введите цифру 1-4");
            }
        }
    }


    /**
     * 1. Поиск рецепта по имени или его части
     */
    private void searchRecipe() {
        System.out.print("Введите название рецепта (или его часть): ");
        String searchName = scanner.nextLine();
        System.out.println("Поиск рецептов, название которых содержит: " + searchName);

        List<Composition> compositionList = compositionDao.searchByName(searchName);
        System.out.println("Найдено " + compositionList.size() + " рецепт(ов)");
        printCompositionList(compositionList);
    }

    /**
     * Отображение найденного рецепта
     *
     * @param compositionList
     */
    private void printCompositionList(List<Composition> compositionList) {
        for (Composition composition : compositionList) {
            System.out.println("\nРецепт: " + composition.getRecipeRef().getName());
            System.out.println("Описание: " + composition.getRecipeRef().getDescription());
            System.out.println("Состав рецепта:");

            int i = 0;
            for (CompositionEntry compositionEntry : composition.getEntryList()) {
                System.out.println((++i) + ". " + compositionEntry.getIngredientRef().getName() +
                        " (" + compositionEntry.getAmount() + " " + compositionEntry.getUnitRef().getName() + ");");
            }
        }
    }


    /**
     * 2. Удаление рецепта
     */
    private void deleteRecipe() {
        System.out.println("\nУдаление рецепта");
        showAllRecipes();
        System.out.print("Введите id рецепта, который требуется удалить (0 - отмена): ");
        Integer userInput = null;
        try {
            userInput = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод");
            return;
        }
        if (userInput == 0) return;
        compositionDao.deleteByRecipeID(userInput);
        System.out.println("Рецепт удален");
    }

    /**
     * Отображение всех рецептов
     */
    private void showAllRecipes() {
        List<Recipe> recipeList = recipeDao.getAll();
        for (Recipe recipe : recipeList) {
            System.out.println(recipe.getId() + ". " + recipe.getName());
        }
    }


    /**
     * 3. Добавление рецепта
     */
    private void addRecipe() {
        Recipe recipe = new Recipe();

        System.out.print("Введите название рецепта: ");
        String recipeName = scanner.nextLine();
        recipe.setName(recipeName);

        System.out.print("Введите описание рецепта: ");
        String recipeDescription = scanner.nextLine();
        recipe.setDescription(recipeDescription);

        Composition composition = new Composition();
        composition.setRecipeRef(recipe);

        List<CompositionEntry> compositionEntryList = new ArrayList<>();
        composition.setEntryList(compositionEntryList);

        while (true) {
            System.out.println("\nВвод состава рецепта");
            showIngredients();
            System.out.println("Введите id требуемого ингредиента ");
            System.out.print("n для ввода нового ингредиента, q для завершения редактирования:");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("q")) break;

            Ingredient ingredient;
            if (userInput.equalsIgnoreCase("n")) {
                ingredient = createNewIngredient();
            } else {
                int ingredientId = 0;
                try {
                    ingredientId = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ввод");
                    continue;
                }
                ingredient = ingredientDao.getByPK(ingredientId);
            }

            System.out.println("Введите единицу измерения ингредиента " + ingredient.getName() + " :");
            showUnits();
            System.out.println("Введите id требуемой ед.изм.");
            System.out.print("n для ввода новой, q для завершения редактирования:");
            userInput = scanner.nextLine();

            Unit unit;
            if (userInput.equalsIgnoreCase("q")) break;
            if (userInput.equalsIgnoreCase("n")) {
                unit = createNewUnit();
            } else {
                int unitId = 0;
                try {
                    unitId = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ввод");
                    continue;
                }
                unit = unitDao.getByPK(unitId);
            }

            System.out.println("Введите требуемое количество ингредиента (в " + unit.getName() + "): ");

            Double amount = null;
            try {
                amount = Double.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод");
                continue;
            }

            compositionEntryList.add(new CompositionEntry(ingredient, amount, unit));

            System.out.println("Ингредиент добавлен (" + ingredient.getName() + " " + amount + " " + unit.getName() + ")");
        }

        compositionDao.insert(composition);
        System.out.println("Новый рецепт успешно сохранен.");

    }


    private void showIngredients() {
        List<Ingredient> ingredientList = ingredientDao.getAll();
        for (Ingredient ingredient : ingredientList) {
            System.out.println(ingredient.getId() + ". " + ingredient.getName());
        }
    }

    private Ingredient createNewIngredient() {
        Ingredient ingredient = new Ingredient();
        System.out.print("\nВведите наименование нового ингредиента: ");
        String name = scanner.nextLine();
        ingredient.setName(name);
        return ingredient;
    }

    private void showUnits() {
        List<Unit> unitList = unitDao.getAll();
        for (Unit unit : unitList) {
            System.out.println(unit.getId() + ". " + unit.getName());
        }
    }

    private Unit createNewUnit() {
        Unit unit = new Unit();
        System.out.print("\nВведите наименование новой ед. изм.: ");
        String name = scanner.nextLine();
        unit.setName(name);
        return unit;
    }

}
