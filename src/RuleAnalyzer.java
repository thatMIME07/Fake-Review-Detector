import java.util.List;

public class RuleAnalyzer {

    public static final int MIN_TEXT_LENGTH = 20;

    public static final int WEIGHT_SHORT_TEXT = 30;
    public static final int WEIGHT_DUPLICATE_TEXT = 40;
    public static final int WEIGHT_EXTREME_RATING_SPAM = 25;
    public static final int WEIGHT_MULTIPLE_REVIEWS = 20;

    public static final int THRESHOLD_SUSPICIOUS = 30;
    public static final int THRESHOLD_FAKE = 70;

    public int checkShortText(Review review) {
        if (review.getReviewText().trim().length() < MIN_TEXT_LENGTH) {
            review.addReason("Review text is very short (< " + MIN_TEXT_LENGTH + " chars)");
            return WEIGHT_SHORT_TEXT;
        }
        return 0;
    }

    public int checkDuplicateText(Review review, ReviewManager manager) {
        if (manager.hasDuplicateText(review.getReviewText())) {
            review.addReason("Duplicate review text detected");
            return WEIGHT_DUPLICATE_TEXT;
        }
        return 0;
    }

    public int checkExtremeRatingSpam(Review review, ReviewManager manager) {
        int rating = review.getRating();
        if (rating != 1 && rating != 5) {
            return 0;
        }

        List<Review> pastReviews = manager.getReviewsByUser(review.getUserId());
        for (Review past : pastReviews) {
            if (past.getRating() == 1 || past.getRating() == 5) {
                review.addReason("Repeated extreme rating by same user");
                return WEIGHT_EXTREME_RATING_SPAM;
            }
        }
        return 0;
    }

    public int checkMultipleReviews(Review review, ReviewManager manager) {
        List<Review> pastReviews = manager.getReviewsByUser(review.getUserId());
        if (!pastReviews.isEmpty()) {
            review.addReason("Multiple reviews from same user in session");
            return WEIGHT_MULTIPLE_REVIEWS;
        }
        return 0;
    }
}
