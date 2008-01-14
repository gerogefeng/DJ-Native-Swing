/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package chrriis.dj.nativeswing.demo.examples.flashplayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import chrriis.dj.nativeswing.demo.DemoUtils;
import chrriis.dj.nativeswing.ui.JFlashPlayer;

/**
 * @author Christopher Deckers
 */
public class Interactions extends JPanel {

  public Interactions() {
    super(new BorderLayout(0, 0));
    JPanel playerPanel = new JPanel(new BorderLayout(0, 0));
    playerPanel.setBorder(BorderFactory.createTitledBorder("Native Flash Player component"));
    final JFlashPlayer player = new JFlashPlayer();
    player.setAutoStart(true);
    String resourceURL = DemoUtils.getResourceURL(Interactions.class, "resource/dyn_text_moving.swf");
    player.setURL(resourceURL);
    new Thread() {
      @Override
      public void run() {
        try {
          sleep(1000);
        } catch(Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            // We have to delay, because setting a variable only works when the flash application is loaded.
            player.setVariable("mytext", "My Text");
          }
        });
      }
    }.start();
    playerPanel.add(player, BorderLayout.CENTER);
    add(playerPanel, BorderLayout.CENTER);
    JPanel variablePanel = new JPanel(new BorderLayout(0, 0));
    variablePanel.setBorder(BorderFactory.createTitledBorder("Get/Set Variables"));
    JPanel getSetNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
    getSetNorthPanel.add(new JLabel("Text:"));
    final JTextField setTextField = new JTextField(new PlainDocument() {
      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && getLength() + str.length() > 7) {
          return;
        }
        super.insertString(offs, str, a);
      }
    }, "Set", 14);
    getSetNorthPanel.add(setTextField);
    JButton setButton = new JButton("Set");
    setButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        player.setVariable("mytext", setTextField.getText());
      }
    });
    getSetNorthPanel.add(setButton);
    JButton getButton = new JButton("Get");
    getSetNorthPanel.add(getButton);
    variablePanel.add(getSetNorthPanel, BorderLayout.NORTH);
    JPanel getSetSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
    getSetSouthPanel.add(new JLabel("Last acquired text:"));
    final JLabel getLabel = new JLabel("-");
    getButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String value = player.getVariable("mytext");
        getLabel.setText(value == null || value.length() == 0? " ": value);
      }
    });
    getSetSouthPanel.add(getLabel);
    variablePanel.add(getSetSouthPanel, BorderLayout.SOUTH);
    add(variablePanel, BorderLayout.NORTH);
  }
  
}
