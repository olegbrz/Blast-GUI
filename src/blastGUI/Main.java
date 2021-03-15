package blastGUI;

import java.awt.*;

public class Main {
  public static void main(String[] args) {
    EventQueue.invokeLater(
        () -> {
          @SuppressWarnings("unused")
          GUI newWindow = new GUI();
        });
  }
}
