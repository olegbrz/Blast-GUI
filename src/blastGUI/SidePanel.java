package blastGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Hashtable;

public class SidePanel extends JPanel {

  private final JComboBox<Object> queryComboBox;
  private final JRadioButton proteinRadio, nucleotideRadio;
  private final JSlider similaritySlider;
  private final JTextField similaritySliderValue;
  private final JLabel selectedDbFileLabel;
  private final JLabel selectedDbIndexFileLabel;
  private String databasePath, databaseIndexPath;

  public SidePanel() {
    this.setLayout(new GridLayout(0, 1));

    // Query sequence
    JLabel queryLabel = new JLabel("Query sequence");
    this.add(queryLabel);

    queryComboBox = new JComboBox<>();
    queryComboBox.setEditable(true);
    JPanel comboWrapper = new JPanel(new BorderLayout());
    comboWrapper.add(queryComboBox, BorderLayout.PAGE_START);
    this.add(comboWrapper);

    // Query type
    JLabel queryTypeLabel = new JLabel("Query type");
    this.add(queryTypeLabel);

    ButtonGroup group = new ButtonGroup();
    proteinRadio = new JRadioButton("Protein");
    proteinRadio.setSelected(true);
    nucleotideRadio = new JRadioButton("Nucleotide");
    group.add(proteinRadio);
    group.add(nucleotideRadio);
    this.add(proteinRadio);
    this.add(nucleotideRadio);

    // Similarity
    JLabel similarityLabel = new JLabel("Similarity");
    this.add(similarityLabel);

    JPanel sliderWrapper = new JPanel();
    similaritySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    similaritySliderValue = new JTextField(4);
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
        e -> similaritySliderValue.setText("" + (float) similaritySlider.getValue() / 100));
    sliderWrapper.add(similaritySlider);
    similaritySliderValue.setText("1.0");
    similaritySliderValue.addActionListener(
        e ->
            similaritySlider.setValue(
                (int) (Float.parseFloat(similaritySliderValue.getText()) * 100)));
    sliderWrapper.add(similaritySliderValue);
    this.add(sliderWrapper);

    // Files
    JLabel filesLabel = new JLabel("Database files");
    this.add(filesLabel);

    JPanel dbOpenWrapper = new JPanel(new BorderLayout());
    JButton openDatabaseButton = new JButton("Open database");
    openDatabaseButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
    dbOpenWrapper.add(openDatabaseButton, BorderLayout.PAGE_START);
    openDatabaseButton.addActionListener(e -> openFile(FileClass.DATABASE));

    selectedDbFileLabel = new JLabel("No database selected.");
    Font dbFileLabelFont = selectedDbFileLabel.getFont();
    selectedDbFileLabel.setFont(
        dbFileLabelFont.deriveFont(dbFileLabelFont.getStyle() & ~Font.BOLD));
    dbOpenWrapper.add(selectedDbFileLabel, BorderLayout.CENTER);
    this.add(dbOpenWrapper);

    JPanel dbIndexOpenWrapper = new JPanel(new BorderLayout());
    JButton openDatabaseIndexButton = new JButton("Open database indexes");
    openDatabaseIndexButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
    dbIndexOpenWrapper.add(openDatabaseIndexButton, BorderLayout.PAGE_START);
    openDatabaseIndexButton.addActionListener(e -> openFile(FileClass.DATABASE_INDEX));

    selectedDbIndexFileLabel = new JLabel("No database indexes selected.");
    Font dbIndexFileLabelFont = selectedDbIndexFileLabel.getFont();
    selectedDbIndexFileLabel.setFont(
        dbIndexFileLabelFont.deriveFont(dbIndexFileLabelFont.getStyle() & ~Font.BOLD));
    dbIndexOpenWrapper.add(selectedDbIndexFileLabel, BorderLayout.CENTER);
    this.add(dbIndexOpenWrapper);
  }

  public String getDatabaseIndexPath() {
    return databaseIndexPath;
  }

  public String getDatabasePath() {
    return databasePath;
  }

  public void openFile(FileClass ft) {
    JFileChooser chooser = new JFileChooser();
    if (ft == FileClass.DATABASE_INDEX) {
      FileNameExtensionFilter indexesFile = new FileNameExtensionFilter("Index file", "indexs");
      chooser.setFileFilter(indexesFile);
    }
    chooser.setCurrentDirectory(new File("./"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      if (ft == FileClass.DATABASE_INDEX) {
        databaseIndexPath = f.getAbsolutePath();
        selectedDbIndexFileLabel.setText(f.getAbsoluteFile().getName() + " selected");
      } else if (ft == FileClass.DATABASE) {
        databasePath = f.getAbsolutePath();
        selectedDbFileLabel.setText(f.getAbsoluteFile().getName() + " selected");
      }
    }
  }

  public Object getComboBoxValue() {
    return queryComboBox.getSelectedItem();
  }

  public boolean proteinSelected() {
    return proteinRadio.isSelected();
  }

  public float getSliderValue() {
    return ((float) similaritySlider.getValue()) / 100;
  }

  public void addToComboBox(String query) {
    queryComboBox.addItem(query);
  }

  public enum FileClass {
    DATABASE,
    DATABASE_INDEX
  }
}
