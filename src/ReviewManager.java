import java.util.ArrayList;
import java.util.List;

public class ReviewManager {

    private List<Review> reviews;

    public ReviewManager() {
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Review> getReviewsByUser(String userId) {
        List<Review> result = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getUserId().equalsIgnoreCase(userId)) {
                result.add(r);
            }
        }
        return result;
    }

    public boolean hasDuplicateText(String text) {
        for (Review r : reviews) {
            if (r.getReviewText().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        reviews.clear();
    }

    public int size() {
        return reviews.size();
    }
}
