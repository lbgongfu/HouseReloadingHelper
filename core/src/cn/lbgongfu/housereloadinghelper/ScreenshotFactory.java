package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

public class ScreenshotFactory
{
  public static void saveScreenshot(FileHandle fileHandle)
  {
    try
    {
      Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
      PixmapUtils.transparentBack(pixmap);
      PixmapIO.writePNG(fileHandle, pixmap);
      pixmap.dispose();
    }
    catch (Exception localException) {
    }
  }

  public static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
    Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

    if (yDown)
    {
      ByteBuffer pixels = pixmap.getPixels();
      int numBytes = w * h * 4;
      byte[] lines = new byte[numBytes];
      int numBytesPerLine = w * 4;
      for (int i = 0; i < h; i++) {
        pixels.position((h - i - 1) * numBytesPerLine);
        pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
      }
      pixels.clear();
      pixels.put(lines);
      pixels.clear();
    }

    return pixmap;
  }
}