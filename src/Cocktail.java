import java.util.List;

public class Cocktail {
    private String name;
    private List<Ingredient> ingredients;

    public Cocktail(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
