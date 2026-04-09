import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GUIApp extends JFrame {

    JTextField userField, productField, ratingField, keywordField;
    JTextArea reviewArea, outputArea;
    DefaultListModel<String> reviewListModel;
    JList<String> reviewList;

    ArrayList<Review> reviews = new ArrayList<>();

    public GUIApp() {

        setTitle("Fake Review Detector");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // INPUT PANEL
        JPanel inputPanel = new JPanel(new GridLayout(4,2,10,10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Review Details"));

        userField = new JTextField();
        productField = new JTextField();
        ratingField = new JTextField();
        reviewArea = new JTextArea(3,20);

        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(userField);

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productField);

        inputPanel.add(new JLabel("Rating:"));
        inputPanel.add(ratingField);

        inputPanel.add(new JLabel("Review:"));
        inputPanel.add(new JScrollPane(reviewArea));

        add(inputPanel, BorderLayout.NORTH);

        // LIST PANEL
        reviewListModel = new DefaultListModel<>();
        reviewList = new JList<>(reviewListModel);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Stored Reviews"));
        listPanel.add(new JScrollPane(reviewList));

        add(listPanel, BorderLayout.CENTER);

        // ACTION PANEL
        JPanel rightPanel = new JPanel(new GridLayout(6,1,10,10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton addBtn = new JButton("Add Review");
        JButton checkBtn = new JButton("Check Selected");
        JButton clearBtn = new JButton("Clear All Reviews");
        JButton searchBtn = new JButton("Search Keyword");

        keywordField = new JTextField();

        rightPanel.add(addBtn);
        rightPanel.add(checkBtn);
        rightPanel.add(clearBtn);
        rightPanel.add(new JLabel("Keyword:"));
        rightPanel.add(keywordField);
        rightPanel.add(searchBtn);

        add(rightPanel, BorderLayout.EAST);

        // OUTPUT PANEL
        outputArea = new JTextArea(8,40);
        outputArea.setEditable(false);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output"));
        outputPanel.add(new JScrollPane(outputArea));

        add(outputPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS

        addBtn.addActionListener(e -> {
            try {
                String user = userField.getText();
                String product = productField.getText();
                int rating = Integer.parseInt(ratingField.getText());
                String text = reviewArea.getText();

                Review r = new Review(user, product, rating, text);
                reviews.add(r);

                reviewListModel.addElement(user + " | " + product + " | " + text);
                outputArea.setText("Review Added Successfully!");

            } catch (NumberFormatException ex) {
                outputArea.setText("Rating must be a number!");
            } catch (Exception ex) {
                outputArea.setText("Invalid Input!");
            }
        });

        checkBtn.addActionListener(e -> {
            int index = reviewList.getSelectedIndex();

            if (index == -1) {
                outputArea.setText("Select a review first!");
                return;
            }

            Review r = reviews.get(index);

            Result res = RuleAnalyzer.analyze(r, reviews);
            String status = FakeDetector.classify(res.score);

            outputArea.setText("Score: " + res.score + "\nStatus: " + status + "\n");

            for (String reason : res.reasons) {
                outputArea.append("- " + reason + "\n");
            }
        });

        clearBtn.addActionListener(e -> {
            reviews.clear();
            reviewListModel.clear();
            outputArea.setText("All reviews cleared!");
        });

        searchBtn.addActionListener(e -> {
            String keyword = keywordField.getText().toLowerCase();
            int count = 0;

            for (Review r : reviews) {
                if (r.text.toLowerCase().contains(keyword)) {
                    count++;
                }
            }

            outputArea.setText("Keyword found in " + count + " reviews\n");

            if (count >= 3) {
                outputArea.append("Possible fake pattern detected!");
            } else {
                outputArea.append("Normal usage");
            }
        });
    }

    public static void main(String[] args) {
        new GUIApp().setVisible(true);
    }
}