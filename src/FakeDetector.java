public class FakeDetector {

    private RuleAnalyzer analyzer;

    public FakeDetector() {
        this.analyzer = new RuleAnalyzer();
    }

    public void analyze(Review review, ReviewManager manager) {
        int score = 0;

        score += analyzer.checkShortText(review);
        score += analyzer.checkDuplicateText(review, manager);
        score += analyzer.checkExtremeRatingSpam(review, manager);
        score += analyzer.checkMultipleReviews(review, manager);

        if (score > 100) {
            score = 100;
        }

        String status;
        if (score >= RuleAnalyzer.THRESHOLD_FAKE) {
            status = "FAKE";
        } else if (score >= RuleAnalyzer.THRESHOLD_SUSPICIOUS) {
            status = "SUSPICIOUS";
        } else {
            status = "GENUINE";
        }

        review.setFakeScore(score);
        review.setStatus(status);
    }
}
