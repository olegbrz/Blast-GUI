package blastGUI;

import blast.BlastController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
  private JButton openDbFile, openDbIndFile, blast;
  private JTextField queryField, similarityField;
  private JRadioButton nucRadio, protRadio;
  private String databasePath, databaseIndexPath;
  final JFileChooser fc = new JFileChooser();

  public GUI() {

    this.setLayout(new GridLayout(0, 1));
    this.setTitle("BLAST");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel queryLabel = new JLabel("Query sequence");
    this.add(queryLabel);

    queryField = new JTextField(20);
    this.add(queryField);

    JLabel queryTypeLabel = new JLabel("Query type");
    this.add(queryTypeLabel);

    ButtonGroup group = new ButtonGroup();

    protRadio = new JRadioButton("Protein");
    group.add(protRadio);
    this.add(protRadio);
    protRadio.setSelected(true);

    nucRadio = new JRadioButton("Nucleotide");
    group.add(nucRadio);
    this.add(nucRadio);

    JLabel similarityLabel = new JLabel("Similarity");
    this.add(similarityLabel);

    similarityField = new JTextField(20);
    this.add(similarityField);

    openDbFile = new JButton("Open a database file");
    this.add(openDbFile);
    openDbFile.addActionListener(
        e -> {
          selectDatabase();
        });

    openDbIndFile = new JButton("Open a database indexes file");
    this.add(openDbIndFile);
    openDbIndFile.addActionListener(
        e -> {
          selectDatabaseIndex();
        });

    blast = new JButton("BLAST");
    this.add(blast);
    blast.addActionListener(
        e -> {
          runQuery();
        });

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
    System.out.println(query.length() == 0);
    char queryType = protRadio.isSelected() ? 'p' : 'q';
    float similarity = Float.parseFloat(similarityField.getText());

    BlastController bCnt = new BlastController();
    try {
      String result =
              bCnt.blastQuery(queryType, databasePath, databaseIndexPath, similarity, query);
      System.out.println(result);
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
