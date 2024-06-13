import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private static CocktailManager cocktailManager = new CocktailManager();
    private static RecommendationEngine recommendationEngine;
    private static DatabaseManager dbManager = new DatabaseManager();

    public static void main(String[] args) {
        // 프로그램 시작 시 레시피를 모두 로드하고 추천 엔진을 초기화
        List<Cocktail> cocktails = cocktailManager.loadAllCocktails();
        recommendationEngine = new RecommendationEngine(cocktails);

        // 북마크와 리뷰 데이터 로드
        cocktailManager.setBookmarks(dbManager.loadBookmarks(cocktails));
        cocktailManager.setReviews(dbManager.loadReviews());

        // 사용자 입력받기 위한 객체
        Scanner scanner = new Scanner(System.in);

        // 프로그램 종료 시 바뀐 데이터를 저장하기 위한 종료 후크
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                dbManager.saveBookmarks(cocktailManager.getBookmarks());
                dbManager.saveReviews(cocktailManager.getAllReviews());
            } catch (Exception e) {
                System.out.println("Error saving data on shutdown: " + e.getMessage());
            }
        }));

        // 메인 루프: 사용자가 메뉴에서 선택한 기능 처리
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 소비

            switch(choice) {
                case 1:
                    searchCocktail(scanner);
                    break;
                case 2:
                    selectIngredient(scanner);
                    break;
                case 3:
                    addRecipe(scanner);
                    break;
                case 4:
                    deleteRecipe(scanner);
                    break;
                case 5:
                    viewBookmarks(scanner);
                    break;
                case 6:
                    addReview(scanner);
                    break;
                case 7:
                    deleteReview(scanner);
                    break;
                case 8:
                    viewReviews(scanner);
                    break;
                case 9:
                    recommendCocktail();
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("------------------------------------------");
        System.out.println("       Welcone to Cocktail Craft");
        System.out.println("------------------------------------------");

        System.out.print("1. 칵테일 검색  ");
        System.out.print("2. 재료 선택  ");
        System.out.println("3. 레시피 추가");
        System.out.print("4. 레시피 삭제  ");
        System.out.print("5. 북마크 보기  ");
        System.out.println("6. 리뷰 추가");
        System.out.print("7. 리뷰 삭제  ");
        System.out.print("8. 리뷰 보기  ");
        System.out.println("9. 칵테일 추천");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }

    // 칵테일 이름을 입력으로 받아 칵테일을 검색하고 결과 출력
    private static void searchCocktail(Scanner scanner) {
        System.out.print("검색할 칵테일 이름을 입력하세요: ");
        String cocktailName = scanner.nextLine();
        Cocktail cocktail = cocktailManager.searchByName(cocktailName);

        if (cocktail != null) {
            System.out.println("칵테일 이름: " + cocktail.getName());
            System.out.println("레시피: ");
            for (Ingredient ingredient : cocktail.getIngredients()) {
                System.out.println("- " + ingredient.getName() + ": " + ingredient.getAmount());
            }
        } else {
            System.out.println("해당 칵테일을 찾을 수 없습니다.");
        }
    }

    // 재료들을 선택해서 입력하고 만들 수 있는 칵테일을 검색하고 결과를 출력
    private static void selectIngredient(Scanner scanner) {
        List<String> selectedIngredients = new ArrayList<>();
        System.out.println("재료를 입력하세요 (종료하려면 'done' 입력):");

        while (true) {
            System.out.print("재료: ");
            String ingredientName = scanner.nextLine();
            if (ingredientName.equalsIgnoreCase("done")) {
                break;
            }
            selectedIngredients.add(ingredientName);
        }

        List<Cocktail> possibleCocktails = cocktailManager.searchByIngredients(selectedIngredients);
        if (possibleCocktails.isEmpty()) {
            System.out.println("선택한 재료들로 만들 수 있는 칵테일이 없습니다.");
        } else {
            System.out.println("선택한 재료들로 만들 수 있는 칵테일:");
            for (Cocktail cocktail : possibleCocktails) {
                System.out.println("- " + cocktail.getName());
            }
        }
    }

    // 새로운 칵테일 레시피 추가
    private static void addRecipe(Scanner scanner) {
        System.out.print("추가할 칵테일 이름을 입력하세요: ");
        String name = scanner.nextLine();
        List<Ingredient> ingredients = new ArrayList<>();

        System.out.println("재료를 입력하세요 (종료하려면 'done' 입력):");
        while (true) {
            System.out.print("재료: ");
            String ingredientName = scanner.nextLine();
            if (ingredientName.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("양: ");
            String amount = scanner.nextLine();
            ingredients.add(new Ingredient(ingredientName, amount));
        }

        Cocktail newCocktail = new Cocktail(name, ingredients);
        cocktailManager.addRecipe(newCocktail);
        System.out.println("레시피가 추가되었습니다.");
    }

    // 저장되어 있는 레시피 삭제
    private static void deleteRecipe(Scanner scanner) {
        System.out.print("삭제할 칵테일 이름을 입력하세요: ");
        String name = scanner.nextLine();
        boolean success = cocktailManager.deleteRecipe(name);
        if (success) {
            System.out.println("레시피가 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 레시피입니다.");
        }
    }

    private static void addBookmark(Scanner scanner) {
        System.out.print("북마크할 칵테일 이름을 입력하세요: ");
        String name = scanner.nextLine();
        Cocktail cocktail = cocktailManager.searchByName(name);
        if (cocktail != null) {
            cocktailManager.addBookmark(cocktail);
            System.out.println("북마크에 추가되었습니다.");
        } else {
            System.out.println("해당 칵테일을 찾을 수 없습니다.");
        }
    }

    private static void deleteBookmark(Scanner scanner) {
        System.out.print("삭제할 북마크 칵테일 이름을 입력하세요: ");
        String name = scanner.nextLine();
        boolean success = cocktailManager.removeBookmark(name);
        if (success) {
            System.out.println("북마크에서 삭제되었습니다.");
        } else {
            System.out.println("해당 칵테일이 북마크에 없습니다.");
        }
    }

    // 사용자가 북마크된 칵테일 목록을 보고 추가, 삭제 가능
    private static void viewBookmarks(Scanner scanner) {
        List<Cocktail> bookmarks = cocktailManager.getBookmarks();
        if (bookmarks.isEmpty()) {
            System.out.println("등록되어 있는 칵테일이 없습니다.");
        } else {
            System.out.println("북마크된 칵테일:");
            for (Cocktail cocktail : bookmarks) {
                System.out.println("- " + cocktail.getName());
            }
            System.out.println();
        }

        while (true) {
            System.out.println("1. 북마크 추가");
            System.out.println("2. 북마크 삭제");
            System.out.println("3. 돌아가기");
            System.out.print("선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 소비

            switch(choice) {
                case 1:
                    addBookmark(scanner);
                    break;
                case 2:
                    deleteBookmark(scanner);
                    break;
                case 3:
                    return; // interface로 돌아가기
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }

    // 특정 칵테일에 대한 리뷰를 추가
    private static void addReview(Scanner scanner) {
        System.out.print("리뷰를 작성할 칵테일 이름을 입력하세요: ");
        String cocktailName = scanner.nextLine();
        System.out.print("리뷰 내용을 입력하세요: ");
        String content = scanner.nextLine();

        Review review = new Review(cocktailName, content);
        cocktailManager.addReview(review);
        System.out.println("리뷰가 추가되었습니다.");
    }

    // 특정 칵테일에 대한 리뷰를 삭제
    private static void deleteReview(Scanner scanner) {
        System.out.print("삭제할 리뷰의 칵테일 이름을 입력하세요: ");
        String cocktailName = scanner.nextLine();
        boolean success = cocktailManager.deleteReview(cocktailName);
        if (success) {
            System.out.println("리뷰가 삭제되었습니다.");
        } else {
            System.out.println("해당 칵테일에 대한 리뷰가 없습니다.");
        }
    }

    // 특정 칵테일에 대한 리뷰 보기
    private static void viewReviews(Scanner scanner) {
        System.out.print("리뷰를 볼 칵테일 이름을 입력하세요: ");
        String cocktailName = scanner.nextLine();
        List<Review> reviews = cocktailManager.getReviews(cocktailName);

        if (reviews.isEmpty()) {
            System.out.println("해당 칵테일에 대한 리뷰가 없습니다.");
        } else {
            //System.out.println("리뷰:");
            for (Review review : reviews) {
                System.out.println(review);
            }
        }
    }

    // 사용자에게 무작위로 칵테일 추천
    private static void recommendCocktail() {
        Cocktail recommended = recommendationEngine.recommend();
        if (recommended != null) {
            System.out.println("추천 칵테일: " + recommended.getName());
            System.out.println("레시피: ");
            for (Ingredient ingredient : recommended.getIngredients()) {
                System.out.println("- " + ingredient.getName() + ": " + ingredient.getAmount());
            }
        } else {
            System.out.println("추천할 칵테일이 없습니다.");
        }
    }
}
