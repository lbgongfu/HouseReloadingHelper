package cn.lbgongfu.housereloadinghelper.desktop;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFileChooser;

import cn.lbgongfu.housereloadinghelper.FileProvider;

public class DesktopFileProvider
  implements FileProvider
{
  private Component parent;

  public DesktopFileProvider(Component parent)
  {
    this.parent = parent;
  }

  public void selectFile(FileProvider.FileSelectedListener listener)
  {
  }

  public void selectDir(final FileProvider.FileSelectedListener listener)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(1);
        int result = fileChooser.showOpenDialog(DesktopFileProvider.this.parent);
        if ((result == 0) && (listener != null))
          listener.selected(fileChooser.getSelectedFile().getAbsolutePath());
      }
    });
  }
}