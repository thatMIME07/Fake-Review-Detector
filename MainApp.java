MIME
thatmime
but the Lord laughs at the wicked, for he knows their day is coming.

You missed a call from 
Fellow Buffalo
 that lasted a few seconds. — 17:57
MIME
 started a call that lasted an hour. — 17:58
Fellow Buffalo — 19:07
https://github.com/thatMIME07/Fake-Review-Detector
GitHub
GitHub - thatMIME07/Fake-Review-Detector
Contribute to thatMIME07/Fake-Review-Detector development by creating an account on GitHub.
Contribute to thatMIME07/Fake-Review-Detector development by creating an account on GitHub.
ALT
import java.util.*;

class Review {
    String userId;
    String productId;
    int rating;

MainApp.java
7 KB
MIME [SOS団],  — 19:11
kardi repo update
account bana kar send kardiyo
You missed a call from 
Fellow Buffalo
 that lasted a few seconds. — 22:09
Fellow Buffalo — 22:17
sunn
ye code bhi add krde repo mein
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GUIApp extends JFrame {

GUIApp.java
6 KB
MIME [SOS団],  — 22:26
kardiya
Fellow Buffalo — 22:31
Attachment file type: acrobat
JAVA phase 2 report.pdf
425.89 KB
isko replace krde purane main waale se
import java.util.*;

class Review {
    String userId;
    String productId;
    int rating;

MainApp.java
7 KB
﻿
Fellow Buffalo
vedanshmanwal
 
 
Check this out
My profile on Letterboxd 
https://boxd.it/bAlIv
My profile on Serializd
https://srlzd.com/u/VedanshManwal
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

        // 2. Repeated review text (avoid self-check)
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

        // Always provide at least one reason
        if (reasons.isEmpty()) {
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

        OUTER:
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add Review");
            System.out.println("2. Check Review");
            System.out.println("3. Search Keyword");
            System.out.println("4. Show All Reviews");
            System.out.println("5. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> addReview();
                case 2 -> checkReview();
                case 3 -> searchKeyword();
                case 4 -> showAllReviews();
                case 5 -> {
                    break OUTER;
                }
                default -> {
                }
            }
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

        if (reviews.isEmpty()) {
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

        if (reviews.isEmpty()) {
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
