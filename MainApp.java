import java.util.*;

class Review {
    String userId;
    String productId;
    int rating;
    String text;

    Review(String u, String p, int r, String t) {
        userId = u;
        productId = p;
        rating = r;
        text = t;
    }
}

class Result {
    int score;
    ArrayList<String> reasons;

    Result(int s, ArrayList<String> r) {
        score = s;
        reasons = r;
    }
}

class RuleAnalyzer {

    static Result analyze(Review review, ArrayList<Review> allReviews) {

        int score = 0;
        ArrayList<String> reasons = new ArrayList<>();

        String text = review.text.toLowerCase();

        boolean extreme = (review.rating == 1 || review.rating == 5);

        // 1. Short review
        if (text.length() < 20) {
            score += 15;
            reasons.add("Review is too short");
        }

        // 2. Repeated review text
        for (Review r : allReviews) {
            if (r != review && r.text.equalsIgnoreCase(review.text)) {
                score += 30;
                reasons.add("Repeated review detected");
                break;
            }
        }

        // 3. Extreme rating
        if (extreme) {
            score += 20;
            reasons.add("Extreme rating detected");
        }

        // 4. Same user multiple reviews
        int count = 0;
        for (Review r : allReviews) {
            if (r.userId.equals(review.userId)) {
                count++;
            }
        }
        if (count >= 2) {
            score += 25;
            reasons.add("Same user posted multiple reviews");
        }

        // 5. Positive keywords
        String[] pos = {"best", "amazing", "awesome", "perfect"};
        int pc = 0;
        for (String w : pos) {
            if (text.contains(w)) pc++;
        }
        if (pc >= 2) {
            score += 15;
            reasons.add("Too many positive keywords");
        }

        // 6. Negative keywords
        String[] neg = {"worst", "bad", "terrible", "useless"};
        int nc = 0;
        for (String w : neg) {
            if (text.contains(w)) nc++;
        }
        if (nc >= 2) {
            score += 15;
            reasons.add("Too many negative keywords");
        }

        // Always give reason
        if (reasons.size() == 0) {
            reasons.add("No suspicious patterns detected");
        }

        return new Result(score, reasons);
    }
}

class FakeDetector {
    static String classify(int score) {
        if (score >= 50) return "FAKE";
        else if (score >= 30) return "SUSPICIOUS";
        else if (score > 0) return "LOW RISK";
        else return "GENUINE";
    }
}

public class MainApp {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Review> reviews = new ArrayList<>();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n--- MENU ---");
            System.out.println("1. Add Review");
            System.out.println("2. Check Review");
            System.out.println("3. Search Keyword");
            System.out.println("4. Show All Reviews");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) addReview();
            else if (choice == 2) checkReview();
            else if (choice == 3) searchKeyword();
            else if (choice == 4) showAllReviews();
            else if (choice == 5) break;
        }
    }

    static void addReview() {
        System.out.print("User ID: ");
        String user = sc.nextLine();

        System.out.print("Product ID: ");
        String product = sc.nextLine();

        System.out.print("Rating: ");
        int rating = sc.nextInt();
        sc.nextLine();

        System.out.print("Review: ");
        String text = sc.nextLine();

        reviews.add(new Review(user, product, rating, text));
        System.out.println("Review added!");
    }

    static void checkReview() {

        if (reviews.size() == 0) {
            System.out.println("No reviews available.");
            return;
        }

        System.out.print("Enter review number to check: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index < 1 || index > reviews.size()) {
            System.out.println("Invalid review number!");
            return;
        }

        Review selected = reviews.get(index - 1);

        System.out.println("\n--- SELECTED REVIEW ---");
        System.out.println("User ID: " + selected.userId);
        System.out.println("Product ID: " + selected.productId);
        System.out.println("Rating: " + selected.rating);
        System.out.println("Text: " + selected.text);

        Result res = RuleAnalyzer.analyze(selected, reviews);
        String status = FakeDetector.classify(res.score);

        System.out.println("\nScore: " + res.score + " | Status: " + status);

        System.out.println("Reasons:");
        for (String r : res.reasons) {
            System.out.println("- " + r);
        }
    }

    static void searchKeyword() {
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        int count = 0;

        for (Review r : reviews) {
            if (r.text.toLowerCase().contains(keyword.toLowerCase())) {
                count++;
            }
        }

        System.out.println("Keyword found in " + count + " reviews");

        if (count >= 3) {
            System.out.println("Possible fake pattern detected!");
        } else {
            System.out.println("Normal usage");
        }
    }

    static void showAllReviews() {

        if (reviews.size() == 0) {
            System.out.println("No reviews available.");
            return;
        }

        System.out.println("\n--- ALL REVIEWS ---");

        for (int i = 0; i < reviews.size(); i++) {
            Review r = reviews.get(i);

            System.out.println("\nReview " + (i + 1));
            System.out.println("User ID: " + r.userId);
            System.out.println("Product ID: " + r.productId);
            System.out.println("Rating: " + r.rating);
            System.out.println("Text: " + r.text);
        }
    }
}