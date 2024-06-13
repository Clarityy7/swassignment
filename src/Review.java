public class Review {
    private String cocktailName;
    private String content;

    public Review(String cocktailName, String content) {
        this.cocktailName = cocktailName;
        this.content = content;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "칵테일: " + cocktailName + "\n리뷰: " + content;
    }
}
