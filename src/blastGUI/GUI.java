package blastGUI;

import blast.BlastController;

import java.awt.*;
import java.io.File;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
  private JButton openDbFile, openDbIndFile, blast;
  private JTextField queryField, similarityField, similarityText;
  private JTextArea resultsArea;
  private JRadioButton nucRadio, protRadio;
  private JSlider similaritySlider;
  private String databasePath, databaseIndexPath;
  private JSplitPane splitPane;

  public GUI() {

    this.setLayout(new BorderLayout());
    this.setTitle("BLAST GUI");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Results panel (right)
    resultsArea = new JTextArea(110, 60);
    resultsArea.setEditable(false);
    JScrollPane resultsPanel = new JScrollPane(resultsArea);
    resultsPanel.setPreferredSize(new Dimension(650, 600));

    // Left side menu
    JPanel sideMenu = new JPanel();
    sideMenu.setLayout(new GridLayout(0,1));

    JLabel queryLabel = new JLabel("Query sequence");
    sideMenu.add(queryLabel);

    queryField = new JTextField(20);
    sideMenu.add(queryField);

    JLabel queryTypeLabel = new JLabel("Query type");
    sideMenu.add(queryTypeLabel);

    ButtonGroup group = new ButtonGroup();

    protRadio = new JRadioButton("Protein");
    group.add(protRadio);
    sideMenu.add(protRadio);
    protRadio.setSelected(true);

    nucRadio = new JRadioButton("Nucleotide");
    group.add(nucRadio);
    sideMenu.add(nucRadio);

    JLabel similarityLabel = new JLabel("Similarity");
    sideMenu.add(similarityLabel);

    JPanel sliderPan = new JPanel();

    similaritySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    similaritySlider.setMajorTickSpacing(50);
    similaritySlider.setMinorTickSpacing(10);

    Hashtable labelTable = new Hashtable<Integer, Double>();
    labelTable.put(0,new JLabel("0"));
    labelTable.put(50, new JLabel("0.5"));
    labelTable.put(100, new JLabel("1"));

    similaritySlider.setLabelTable(labelTable);
    similaritySlider.setPaintTicks(true);
    similaritySlider.setPaintLabels(true);
    similaritySlider.addChangeListener(
        e -> {
          similarityText.setText("" + (float) similaritySlider.getValue() / 100);
        });
    sliderPan.add(similaritySlider);

    similarityText = new JTextField(4);
    similarityText.addActionListener(
        e -> {
          similaritySlider.setValue((int) (Float.parseFloat(similarityText.getText()) * 100));
        });
    sliderPan.add(similarityText);

    sideMenu.add(sliderPan);

    JPanel openButtons = new JPanel();

    openDbFile = new JButton("Open a database file");
    openButtons.add(openDbFile);
    openDbFile.addActionListener(
        e -> {
          selectDatabase();
        });

    openDbIndFile = new JButton("Open a database indexes file");
    openButtons.add(openDbIndFile);
    openDbIndFile.addActionListener(
        e -> {
          selectDatabaseIndex();
        });

    sideMenu.add(openButtons);

    blast = new JButton("BLAST");
    sideMenu.add(blast);
    blast.addActionListener(
        e -> {
          runQuery();
        });


    // Window split pane
    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            sideMenu, resultsPanel);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(280);
    this.add(splitPane, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton clearButton = new JButton("Clear results");
    clearButton.addActionListener(e -> {resultsArea.setText("");});
    bottomPanel.add(clearButton);

    this.add(bottomPanel, BorderLayout.SOUTH);

    this.setVisible(true);
    this.pack();
  }

  public void selectDatabaseIndex() {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter indexesFile = new FileNameExtensionFilter("Index file", "indexs");
    chooser.setFileFilter(indexesFile);
    chooser.setCurrentDirectory(new File("./"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      databaseIndexPath = f.getAbsolutePath();
      // read  and/or display the file somehow. ....
    } else {
      // user changed their mind
    }
  }

  public void selectDatabase() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("./"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      databasePath = f.getAbsolutePath();
      // read  and/or display the file somehow. ....
    } else {
      // user changed their mind
    }
  }

  public void runQuery() {
    String query = queryField.getText();
    char queryType = protRadio.isSelected() ? 'p' : 'q';
    float similarity = ((float) similaritySlider.getValue()) /100;

    //System.out.println(query + " " + similarity + " " + queryType + " ");

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

  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            @SuppressWarnings("unused")
            GUI newWindow = new GUI();
          }
        });
  }
}
