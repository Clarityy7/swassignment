import java.util.List;
import java.util.Random;

public class RecommendationEngine {
    private List<Cocktail> cocktails;

    // 엔진 초기화
    public RecommendationEngine(List<Cocktail> cocktails) {
        this.cocktails = cocktails;
    }

    // 무작위로 칵테일 추천
    public Cocktail recommend() {
        if (cocktails == null || cocktails.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(cocktails.size());
        return cocktails.get(index);
    }
}
