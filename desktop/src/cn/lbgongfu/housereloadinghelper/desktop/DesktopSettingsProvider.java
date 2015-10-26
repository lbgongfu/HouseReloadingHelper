package cn.lbgongfu.housereloadinghelper.desktop;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cn.lbgongfu.housereloadinghelper.SettingsProvider;

public class DesktopSettingsProvider
  implements SettingsProvider
{
  private ArrayList<SettingsProvider.DialogListener> listeners = new ArrayList();
  private Window owner;

  public DesktopSettingsProvider(Window owner)
  {
    this.owner = owner;
  }

  public void showSettingsDialog()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() {
        DesktopSettingsProvider.this.showDialog();
      }
    });
  }

  public void addDialogListener(SettingsProvider.DialogListener listener)
  {
    if (listener == null)
      throw new NullPointerException("Parameter 'listener' can not be null");
    if (!this.listeners.contains(listener))
      this.listeners.add(listener);
  }

  private void showDialog()
  {
    final JDialog dialog = new JDialog(this.owner);
    dialog.addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent e) {
        DesktopSettingsProvider.this.notifyListeners(false);
      }
    });
    dialog.setTitle("G3dTest-Settings");
    dialog.setResizable(false);
    dialog.setUndecorated(true);
    dialog.getRootPane().setWindowDecorationStyle(2);
    dialog.getContentPane().setSize(500, 500);
    final SettingsPanel settingsPanel = new SettingsPanel();
    dialog.getContentPane().add(settingsPanel, "Center");
    JButton btnOk = new JButton("确定");
    btnOk.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        settingsPanel.apply();
        dialog.setVisible(false);
        DesktopSettingsProvider.this.notifyListeners(true);
      }
    });
    JButton btnCancel = new JButton("取消");
    btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dialog.setVisible(false);
        DesktopSettingsProvider.this.notifyListeners(true);
      }
    });
    JPanel panelBtns = new JPanel(new FlowLayout(1, 10, 10));
    panelBtns.add(btnOk);
    panelBtns.add(btnCancel);
    dialog.getContentPane().add(panelBtns, "South");

    dialog.pack();
    dialog.setVisible(true);
  }

  private void notifyListeners(boolean closed)
  {
    for (SettingsProvider.DialogListener listener : this.listeners)
    {
      if (closed)
        listener.dialogClosed();
      else
        listener.dialogOpened();
    }
  }
}