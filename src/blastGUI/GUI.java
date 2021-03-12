package blastGUI;

import blast.BlastController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Hashtable;

public class GUI extends JFrame {
  private final JTextField queryField;
  private final JTextArea resultsArea;
  private final JRadioButton proteinRadio;
  private final JSlider similaritySlider;
  private final JLabel dbIndexFileLabel;
  private final JLabel dbFileLabel;
  private JTextField similarityText;
  private String databasePath, databaseIndexPath;

  public GUI() {

    this.setLayout(new BorderLayout());
    this.setTitle("BLAST GUI");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Results panel (right)
    resultsArea = new JTextArea(10, 60);
    resultsArea.setEditable(false);
    JScrollPane resultsPanel = new JScrollPane(resultsArea);

    // Left side menu
    JPanel sideMenu = new JPanel();
    sideMenu.setLayout(new GridLayout(0, 1));

    JLabel queryLabel = new JLabel("Query sequence");
    sideMenu.add(queryLabel);

    queryField = new JTextField(20);
    sideMenu.add(queryField);

    JLabel queryTypeLabel = new JLabel("Query type");
    sideMenu.add(queryTypeLabel);

    ButtonGroup group = new ButtonGroup();

    proteinRadio = new JRadioButton("Protein");
    group.add(proteinRadio);
    sideMenu.add(proteinRadio);
    proteinRadio.setSelected(true);

    JRadioButton nucRadio = new JRadioButton("Nucleotide");
    group.add(nucRadio);
    sideMenu.add(nucRadio);

    JLabel similarityLabel = new JLabel("Similarity");
    sideMenu.add(similarityLabel);

    JPanel sliderPan = new JPanel();

    similaritySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    similaritySlider.setMajorTickSpacing(50);
    similaritySlider.setMinorTickSpacing(10);

    Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
    labelTable.put(0, new JLabel("0"));
    labelTable.put(50, new JLabel("0.5"));
    labelTable.put(100, new JLabel("1"));

    similaritySlider.setLabelTable(labelTable);
    similaritySlider.setPaintTicks(true);
    similaritySlider.setPaintLabels(true);
    similaritySlider.addChangeListener(
        e -> similarityText.setText("" + (float) similaritySlider.getValue() / 100));
    sliderPan.add(similaritySlider);

    similarityText = new JTextField(4);
    similarityText.addActionListener(
        e -> similaritySlider.setValue((int) (Float.parseFloat(similarityText.getText()) * 100)));
    sliderPan.add(similarityText);

    sideMenu.add(sliderPan);

    JLabel filesLabel = new JLabel("Database files");
    sideMenu.add(filesLabel);

    JButton openDbFile = new JButton("Open a database file");
    sideMenu.add(openDbFile);
    openDbFile.addActionListener(e -> selectDatabase());

    dbFileLabel = new JLabel("No database selected.");
    Font dbFileLabelFont = dbFileLabel.getFont();
    dbFileLabel.setFont(dbFileLabelFont.deriveFont(dbFileLabelFont.getStyle() & ~Font.BOLD));
    sideMenu.add(dbFileLabel);

    JButton openDbIndFile = new JButton("Open a database indexes file");
    sideMenu.add(openDbIndFile);
    openDbIndFile.addActionListener(e -> selectDatabaseIndex());

    dbIndexFileLabel = new JLabel("No database indexes selected.");
    Font dbIndexFileLabelFont = dbIndexFileLabel.getFont();
    dbIndexFileLabel.setFont(
        dbIndexFileLabelFont.deriveFont(dbIndexFileLabelFont.getStyle() & ~Font.BOLD));
    sideMenu.add(dbIndexFileLabel);

    // Window split pane
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideMenu, resultsPanel);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(280);
    this.add(splitPane, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JButton blast = new JButton("BLAST");
    bottomPanel.add(blast);
    blast.addActionListener(e -> runQuery());

    JButton clearButton = new JButton("Clear results");
    clearButton.addActionListener(e -> resultsArea.setText(""));
    bottomPanel.add(clearButton);

    this.add(bottomPanel, BorderLayout.SOUTH);

    this.setVisible(true);
    this.pack();
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(
        () -> {
          @SuppressWarnings("unused")
          GUI newWindow = new GUI();
        });
  }

  public void selectDatabaseIndex() {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter indexesFile = new FileNameExtensionFilter("Index file", "indexs");
    chooser.setFileFilter(indexesFile);
    chooser.setCurrentDirectory(new File("./"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      databaseIndexPath = f.getAbsolutePath();
      dbIndexFileLabel.setText(f.getAbsoluteFile().getName() + " selected");
    }
  }

  public void selectDatabase() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("./"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      databasePath = f.getAbsolutePath();
      dbFileLabel.setText(f.getAbsoluteFile().getName() + " selected");
    }
  }

  public void runQuery() {
    String query = queryField.getText();
    char queryType = proteinRadio.isSelected() ? 'p' : 'q';
    float similarity = ((float) similaritySlider.getValue()) / 100;

    String errorMessage = "";
    if (query.length() == 0) {
      errorMessage = errorMessage + "- The query sequence is empty.\n";
    }
    if (databasePath == null) {
      errorMessage = errorMessage + "- No database file.\n";
    }
    if (databaseIndexPath == null) {
      errorMessage = errorMessage + "- No database indexes file.\n";
    }

    if (errorMessage.length() != 0) {
      JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // System.out.println(query + " " + similarity + " " + queryType + " ");

    BlastController bCnt = new BlastController();
    try {
      String result =
          bCnt.blastQuery(queryType, databasePath, databaseIndexPath, similarity, query);
      resultsArea.setText("");
      resultsArea.append(result);
    } catch (Exception exc) {
      System.out.println("Error en la llamada: " + exc.toString());
    }
  }
}
