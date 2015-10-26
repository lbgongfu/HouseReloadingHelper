package cn.lbgongfu.housereloadinghelper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import cn.lbgongfu.housereloadinghelper.Main;
import cn.lbgongfu.housereloadinghelper.services.ModelViewBeanFactory;
import cn.lbgongfu.housereloadinghelper.services.impls.ModelViewBeanFactoryImpl;
import cn.lbgongfu.housereloadinghelper.services.impls.ModelViewBeanFactorySimpleImpl;

public class DesktopLauncher extends JDialog
{
  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(DesktopLauncher.class.getName());
  private LwjglCanvas canvas;
  private ModelViewBeanFactory factory;

  public DesktopLauncher()
  {
    setTitle("House Reloading Helper");
    setDefaultCloseOperation(2);
    setResizable(false);
    setUndecorated(true);
    getRootPane().setWindowDecorationStyle(0);
    addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        System.exit(0);
      }
    });
    LwjglApplicationConfiguration.disableAudio = true;
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.samples = 8;
    config.a = 0;
    Main main = new Main(new DesktopFileProvider(this), new DesktopSettingsProvider(this));

    canvas = new LwjglCanvas(main, config);
    canvas.getCanvas().setSize(1024, 768);
    getContentPane().add(canvas.getCanvas(), "Center");

    pack();
    setVisible(true);

    init(main);
  }

  private void init(final Main main) {
    new Thread(new Runnable()
    {
      public void run()
      {
        try {
          factory = new ModelViewBeanFactoryImpl();
//          factory = new ModelViewBeanFactorySimpleImpl();
          main.setFactory(factory);
          EventQueue.invokeLater(new Runnable()
          {
            public void run() {
              getRootPane().setWindowDecorationStyle(2);
              pack();
            }
          });
        }
        catch (Exception e)
        {
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run()
            {
              int result = JOptionPane.showConfirmDialog(DesktopLauncher.this, "无法创建ModelViewBeanFactoryImpl，是否重试？");
              if (result == 1)
              {
                canvas.stop();
                setVisible(false);
                SwingUtilities.invokeLater(new Runnable()
                {
                  public void run()
                  {
                    System.exit(0);
                  }
                });
              }
              else {
                init(main);
              }
            }
          });
          logger.log(Level.SEVERE, e.getMessage());
          e.printStackTrace();
        }
      }
    }).start();
  }

  protected void processWindowEvent(WindowEvent e)
  {
    if (e.getID() == 201)
    {
      int result = JOptionPane.showConfirmDialog(this, "你确定退出程序吗？", "退出程序", 0);
      if (result != 0)
        return;
    }
    super.processWindowEvent(e);
  }

  public static void main(String[] arg) {
    swing();
  }

  private static void swing() {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() {
        new DesktopLauncher();
      }
    });
  }
}