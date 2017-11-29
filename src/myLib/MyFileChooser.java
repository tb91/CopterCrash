package myLib;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MyFileChooser extends JFileChooser
{
  private static final long serialVersionUID = 1L;

  public void approveSelection()
  {
    File f = getSelectedFile();
    if ((f.exists()) && (getDialogType() == 1)) {
      int result = JOptionPane.showConfirmDialog(this, "The file exists.\nDo you want to replace it?", "Existing file", 0);
      if ((result == 1) || (result == -1))
      {
        return;
      }
    }

    if (!f.getPath().toString().endsWith(".jar")) {
      setSelectedFile(new File(f + ".jar"));
      approveSelection();
    } else {
      super.approveSelection();
    }
  }
}