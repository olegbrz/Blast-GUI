package blastGUI;

import blast.BlastController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GUI extends JFrame {
  private final ResultsPanel resultsPanel;
  private final JLabel queryParams;
  private final SidePanel sidePanel;

  public GUI() {

    // Set layout, icon, title...
    this.setLayout(new BorderLayout());
    this.setTitle("BLAST GUI");
    URL iconURL = getClass().getResource("/resources/dna.png");
    ImageIcon icon = new ImageIcon(iconURL);
    this.setIconImage(icon.getImage());

    // Panels instances
    resultsPanel = new ResultsPanel();
    sidePanel = new SidePanel();

    // Window split pane
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, resultsPanel);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(280);
    this.add(splitPane, BorderLayout.CENTER);

    // Bottom panel
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    queryParams = new JLabel();

    // Blast button
    JButton blastButton = new JButton("BLAST");
    bottomPanel.add(blastButton);
    blastButton.addActionListener(e -> runQuery());

    // Clear button
    JButton clearButton = new JButton("Clear results");
    clearButton.addActionListener(
        e -> {
          resultsPanel.clearResults();
          queryParams.setText("");
        });
    bottomPanel.add(clearButton);

    // Query parameters
    bottomPanel.add(queryParams);

    this.add(bottomPanel, BorderLayout.SOUTH);

    this.setVisible(true);
    this.pack();
  }

  public void runQuery() {
    Object queryListItem = sidePanel.getComboBoxValue();
    String querySequence = "";
    if (queryListItem != null) querySequence = queryListItem.toString();
    char queryType = sidePanel.proteinSelected() ? 'p' : 'q';
    float similarity = sidePanel.getSliderValue();

    String errorMessage = "";
    if (querySequence.length() == 0) {
      errorMessage = errorMessage + "- The query sequence is empty.\n";
    }
    if (sidePanel.getDatabasePath() == null) {
      errorMessage = errorMessage + "- No database file.\n";
    }
    if (sidePanel.getDatabaseIndexPath() == null) {
      errorMessage = errorMessage + "- No database indexes file.\n";
    }

    if (errorMessage.length() != 0) {
      JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    BlastController blastController = new BlastController();
    try {
      String results =
          blastController.blastQuery(
              queryType,
              sidePanel.getDatabasePath(),
              sidePanel.getDatabaseIndexPath(),
              similarity,
              querySequence);
      resultsPanel.appendResults(results);
      sidePanel.addToComboBox(querySequence);
      queryParams.setText(
          " Query seq: "
              + querySequence.substring(0, querySequence.length() > 4 ? 4 : querySequence.length())
              + "...  | Query type: "
              + queryType
              + " | Similarity: "
              + similarity);

    } catch (Exception exc) {
      resultsPanel.appendResults("ERROR: " + exc.toString() + "\n");
    }
  }
}
