import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String COCKTAILS_FILE_PATH = "cocktails.txt";
    private static final String BOOKMARKS_FILE_PATH = "bookmarks.txt";
    private static final String REVIEWS_FILE_PATH = "reviews.txt";

    // 칵테일 레시피를 파일에서 로드
    public List<Cocktail> loadCocktails() {
        List<Cocktail> cocktails = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COCKTAILS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Cocktail cocktail = parseCocktail(line);
                if (cocktail != null) {
                    cocktails.add(cocktail);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading cocktails: " + e.getMessage());
        }
        return cocktails;
    }

    // 칵테일 레시피에서 이름, 재료, 양 분리하고 분석
    private Cocktail parseCocktail(String line) {
        String[] parts = line.split(":");
        if (parts.length != 2) {
            return null;
        }
        String name = parts[0];
        String[] ingredientParts = parts[1].split(";");
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientPart : ingredientParts) {
            String[] ingredientDetails = ingredientPart.split(",");
            if (ingredientDetails.length != 2) {
                continue;
            }
            String ingredientName = ingredientDetails[0];
            String ingredientAmount = ingredientDetails[1];
            ingredients.add(new Ingredient(ingredientName, ingredientAmount));
        }
        return new Cocktail(name, ingredients);
    }

    // 칵테일 레시피를 파일에 저장
    public void saveCocktails(List<Cocktail> cocktails) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COCKTAILS_FILE_PATH))) {
            for (Cocktail cocktail : cocktails) {
                bw.write(formatCocktail(cocktail));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving cocktails: " + e.getMessage());
        }
    }

    // 칵테일 객체를 문자열로 변환하여 파일에 저장할 형식으로 포맷
    private String formatCocktail(Cocktail cocktail) {
        StringBuilder sb = new StringBuilder();
        sb.append(cocktail.getName()).append(":");
        for (int i = 0; i < cocktail.getIngredients().size(); i++) {
            Ingredient ingredient = cocktail.getIngredients().get(i);
            sb.append(ingredient.getName()).append(",").append(ingredient.getAmount());
            if (i < cocktail.getIngredients().size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    // 북마크된 칵테일 파일에 저장
    public void saveBookmarks(List<Cocktail> bookmarks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKMARKS_FILE_PATH))) {
            for (Cocktail cocktail : bookmarks) {
                bw.write(cocktail.getName());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookmarks: " + e.getMessage());
        }
    }

    // 파일에서 북마크된 칵테일 로드
    public List<Cocktail> loadBookmarks(List<Cocktail> allCocktails) {
        List<Cocktail> bookmarks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKMARKS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (Cocktail cocktail : allCocktails) {
                    if (cocktail.getName().equalsIgnoreCase(line)) {
                        bookmarks.add(cocktail);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookmarks: " + e.getMessage());
        }
        return bookmarks;
    }

    // 리뷰를 파일에 저장
    public void saveReviews(List<Review> reviews) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REVIEWS_FILE_PATH))) {
            for (Review review : reviews) {
                bw.write(review.getCocktailName() + ":" + review.getContent());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving reviews: " + e.getMessage());
        }
    }

    // 파일에서 리뷰를 로드
    public List<Review> loadReviews() {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(REVIEWS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    reviews.add(new Review(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
        }
        return reviews;
    }
}
