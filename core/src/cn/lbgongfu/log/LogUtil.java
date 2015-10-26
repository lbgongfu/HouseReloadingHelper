package cn.lbgongfu.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogUtil
{
  private static FileHandler fileHandler;

  public static Logger getLogger(String name)
  {
    Logger logger = Logger.getLogger(name);
    logger.setLevel(Level.ALL);
    Handler handler = createHandler();
    if (handler != null)
      logger.addHandler(handler);
    return logger;
  }

  private static Handler createHandler() {
    if (fileHandler == null) {
      try
      {
        Path path = Paths.get(System.getProperty("user.dir"), new String[] { "logs" });
        if (Files.notExists(path, new LinkOption[0]))
          Files.createDirectory(path, new FileAttribute[0]);
        fileHandler = new FileHandler(path.toString() + "/g3dtest%g.log", true);
        fileHandler.setFormatter(new Formatter()
        {
          public String format(LogRecord record) {
            Calendar now = Calendar.getInstance();
            return String.format("%tF %tT %s %s:\n\t%s\n", new Object[] { now, now, record.getLevel(), record.getLoggerName(), record.getMessage() });
          }
        });
        return fileHandler;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return fileHandler;
  }
}