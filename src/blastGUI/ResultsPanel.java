package blastGUI;

import javax.swing.*;
import java.awt.*;

public class ResultsPanel extends JPanel {
  private final JTextArea resultsArea;

  public ResultsPanel() {
    this.setLayout(new BorderLayout());
    resultsArea = new JTextArea(30, 60);
    resultsArea.setEditable(false);
    resultsArea.setLineWrap(true);
    resultsArea.setBackground(Color.BLACK);
    resultsArea.setForeground(Color.WHITE);
    this.add(new JScrollPane(resultsArea), BorderLayout.CENTER);
  }

  public void clearResults() {
    resultsArea.setText("");
  }

  public void appendResults(String results) {
    resultsArea.append(results);
  }
}
