package myLib;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class MyFilter extends FileFilter
{
  String endung;

  public MyFilter(String endung)
  {
    this.endung = endung;
  }

  public boolean accept(File f)
  {
    if (f == null) return false;

    if (f.isDirectory()) return true;

    return f.getName().toLowerCase().endsWith(this.endung);
  }

  public String getDescription()
  {
    return this.endung + " only";
  }
}