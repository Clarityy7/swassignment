import java.util.ArrayList;
import java.util.List;

public class CocktailManager {
    private DatabaseManager dbManager = new DatabaseManager();
    private List<Review> reviews = new ArrayList<>();
    private List<Cocktail> bookmarks = new ArrayList<>();

    // 칵테일 이름으로 검색하여 칵테일 객체를 반환
    public Cocktail searchByName(String name) {
        List<Cocktail> cocktails = loadAllCocktails();

        for (Cocktail cocktail : cocktails) {
            if (cocktail.getName().equalsIgnoreCase(name)) {
                return cocktail;
            }
        }
        return null;
    }

    // 사용자가 입력한 재료들로 만들 수 있는 칵테일 목록 반환
    public List<Cocktail> searchByIngredients(List<String> selectedIngredients) {
        List<Cocktail> cocktails = loadAllCocktails();
        List<Cocktail> result = new ArrayList<>();

        for (Cocktail cocktail : cocktails) {
            if (canMakeCocktail(cocktail, selectedIngredients)) {
                result.add(cocktail);
            }
        }
        return result;
    }

    // 선택한 재료들로 칵테일 만들 수 있는지 확인
    private boolean canMakeCocktail(Cocktail cocktail, List<String> selectedIngredients) {
        List<Ingredient> cocktailIngredients = cocktail.getIngredients();
        for (Ingredient cocktailIngredient : cocktailIngredients) {
            boolean found = false;
            for (String selectedIngredient : selectedIngredients) {
                if (cocktailIngredient.getName().equalsIgnoreCase(selectedIngredient)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    // 레시피 추가, 삭제 방법: 레시피 데이터를 모두 로드하고 활용 프로그램 종료시 변경점 저장
    public void addRecipe(Cocktail cocktail) {
        List<Cocktail> cocktails = loadAllCocktails();
        cocktails.add(cocktail);
        dbManager.saveCocktails(cocktails);
    }

    public boolean deleteRecipe(String name) {
        List<Cocktail> cocktails = loadAllCocktails();
        boolean found = false;
        for (int i = 0; i < cocktails.size(); i++) {
            if (cocktails.get(i).getName().equalsIgnoreCase(name)) {
                cocktails.remove(i);
                found = true;
                break;
            }
        }
        if (found) {
            dbManager.saveCocktails(cocktails);
        }
        return found;
    }

    public List<Cocktail> loadAllCocktails() {
        return dbManager.loadCocktails();
    }

    // 새로운 리뷰 추가
    public void addReview(Review review) {
        reviews.add(review);
    }

    // 특정 칵테일 리뷰 삭제
    public boolean deleteReview(String cocktailName) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getCocktailName().equalsIgnoreCase(cocktailName)) {
                reviews.remove(i);
                return true;
            }
        }
        return false;
    }

    //  특정 칵테일의 리뷰 목록 반환
    public List<Review> getReviews(String cocktailName) {
        List<Review> result = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getCocktailName().equalsIgnoreCase(cocktailName)) {
                result.add(review);
            }
        }
        return result;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getAllReviews() {
        return reviews;
    }

    // 특정 칵테일 북마크에 추가
    public void addBookmark(Cocktail cocktail) {
        bookmarks.add(cocktail);
    }

    // 특정 칵테일 북마크에서 삭제
    public boolean removeBookmark(String name) {
        for (int i = 0; i < bookmarks.size(); i++) {
            if (bookmarks.get(i).getName().equalsIgnoreCase(name)) {
                bookmarks.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Cocktail> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Cocktail> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
