import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI extends JFrame {

    private JTextField userIdField;
    private JTextField productIdField;
    private JComboBox<Integer> ratingBox;
    private JTextArea reviewTextArea;

    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JTextArea reasonsArea;

    private DefaultTableModel tableModel;
    private JTable historyTable;

    private ReviewManager reviewManager;
    private FakeDetector fakeDetector;

    public MainGUI() {
        reviewManager = new ReviewManager();
        fakeDetector = new FakeDetector();

        setTitle("Rule-Based Fake Review Detector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildResultPanel(), BorderLayout.CENTER);
        add(buildHistoryPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Enter Review Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        userIdField = new JTextField(15);
        panel.add(userIdField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        productIdField = new JTextField(15);
        panel.add(productIdField, gbc);

        gbc.gridx = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 5;
        ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingBox.setSelectedItem(3);
        panel.add(ratingBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Review Text:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        reviewTextArea = new JTextArea(3, 40);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        JScrollPane textScroll = new JScrollPane(reviewTextArea);
        panel.add(textScroll, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton checkButton = new JButton("Check Review");
        JButton clearButton = new JButton("Clear");

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCheckReview();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClear();
            }
        });

        buttonPanel.add(checkButton);
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel buildResultPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Analysis Result"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Fake Score:"), gbc);
        gbc.gridx = 1;
        scoreLabel = new JLabel("—");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(scoreLabel, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("  Status:"), gbc);
        gbc.gridx = 3;
        statusLabel = new JLabel("—");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(statusLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Reasons:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        reasonsArea = new JTextArea(3, 40);
        reasonsArea.setEditable(false);
        reasonsArea.setLineWrap(true);
        reasonsArea.setWrapStyleWord(true);
        reasonsArea.setBackground(panel.getBackground());
        JScrollPane reasonsScroll = new JScrollPane(reasonsArea);
        panel.add(reasonsScroll, gbc);

        return panel;
    }

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Session Review History"));

        String[] columns = {"#", "User", "Product", "Rating", "Score", "Status", "Review Text"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(22);

        historyTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(45);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(45);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(300);

        JScrollPane tableScroll = new JScrollPane(historyTable);
        tableScroll.setPreferredSize(new Dimension(780, 160));
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private void onCheckReview() {
        String userId = userIdField.getText().trim();
        String productId = productIdField.getText().trim();
        int rating = (Integer) ratingBox.getSelectedItem();
        String text = reviewTextArea.getText().trim();

        if (userId.isEmpty() || productId.isEmpty() || text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields (User ID, Product ID, and Review Text).",
                "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Review review = new Review(userId, productId, rating, text);
        fakeDetector.analyze(review, reviewManager);
        reviewManager.addReview(review);

        scoreLabel.setText(String.valueOf(review.getFakeScore()));
        statusLabel.setText(review.getStatus());

        switch (review.getStatus()) {
            case "GENUINE":
                statusLabel.setForeground(new Color(0, 128, 0));
                break;
            case "SUSPICIOUS":
                statusLabel.setForeground(new Color(200, 140, 0));
                break;
            case "FAKE":
                statusLabel.setForeground(Color.RED);
                break;
        }

        List<String> reasons = review.getReasons();
        if (reasons.isEmpty()) {
            reasonsArea.setText("No suspicious patterns detected.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < reasons.size(); i++) {
                sb.append((i + 1) + ". " + reasons.get(i));
                if (i < reasons.size() - 1) sb.append("\n");
            }
            reasonsArea.setText(sb.toString());
        }

        int rowNum = tableModel.getRowCount() + 1;
        String shortText = text.length() > 50 ? text.substring(0, 50) + "..." : text;
        tableModel.addRow(new Object[]{
            rowNum, userId, productId, rating,
            review.getFakeScore(), review.getStatus(), shortText
        });
    }

    private void onClear() {
        userIdField.setText("");
        productIdField.setText("");
        ratingBox.setSelectedItem(3);
        reviewTextArea.setText("");
        scoreLabel.setText("—");
        statusLabel.setText("—");
        statusLabel.setForeground(Color.BLACK);
        reasonsArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI();
            }
        });
    }
}
