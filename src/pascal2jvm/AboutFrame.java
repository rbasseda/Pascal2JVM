package pascal2jvm;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: LexerView</p>
 * <p>Description: Viewing the first part of compiler project</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: I.U.S.T</p>
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */

public class AboutFrame extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JPanel myJPanel = new JPanel();

  public AboutFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.getContentPane().add(myJPanel, BorderLayout.WEST);
  }
}