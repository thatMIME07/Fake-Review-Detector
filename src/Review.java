import java.util.ArrayList;
import java.util.List;

public class Review {

    private String userId;
    private String productId;
    private int rating;
    private String reviewText;
    private int fakeScore;
    private String status;
    private List<String> reasons;

    public Review(String userId, String productId, int rating, String reviewText) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.fakeScore = 0;
        this.status = "UNKNOWN";
        this.reasons = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getFakeScore() {
        return fakeScore;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setFakeScore(int fakeScore) {
        this.fakeScore = fakeScore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }

    @Override
    public String toString() {
        return "Review{user='" + userId + "', product='" + productId +
               "', rating=" + rating + ", score=" + fakeScore +
               ", status='" + status + "', reasons=" + reasons + "}";
    }
}
