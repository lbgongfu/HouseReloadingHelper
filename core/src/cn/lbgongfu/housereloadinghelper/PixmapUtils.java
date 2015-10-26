package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.nio.ByteBuffer;

public class PixmapUtils
{
  public static final Color TRANSPARENT = Color.valueOf("00000000");

  public static Pixmap createBorder(Pixmap source, int thickness, Color color)
  {
    int width = source.getWidth();
    int height = source.getHeight();
    Pixmap pixmap = new Pixmap(width, height, source.getFormat());
    pixmap.setColor(0.0F, 0.0F, 0.0F, 0.0F);
    pixmap.fillRectangle(0, 0, width, height);
    pixmap.drawPixmap(source, 0, 0);
    pixmap.setColor(color);
    pixmap.fillRectangle(0, 0, width, thickness);
    pixmap.fillRectangle(width - thickness, 0, thickness, height);
    pixmap.fillRectangle(0, height - thickness, width, thickness);
    pixmap.fillRectangle(0, 0, thickness, height);
    return pixmap;
  }

  public static Pixmap createRepeat(Pixmap source, int repeatX, int repeatY, int gap, Color gapColor)
  {
    Pixmap low = createRepeat(source, repeatX, repeatY);
    Pixmap overlay = createGrid(low.getWidth(), low.getHeight(), repeatX, repeatY, gap, gapColor, false);
    Pixmap pixmap = createOverlay(low, overlay);
    low.dispose();
    overlay.dispose();
    return pixmap;
  }

  public static Pixmap createRepeat(FileHandle fileHandle, int repeatX, int repeatY, int gap, Color gapColor)
  {
    Pixmap low = createRepeat(fileHandle, repeatX, repeatY);
    Pixmap overlay = createGrid(low.getWidth(), low.getHeight(), repeatX, repeatY, gap, gapColor, false);
    Pixmap pixmap = createOverlay(low, overlay);
    low.dispose();
    overlay.dispose();
    return pixmap;
  }

  public static Pixmap createRepeat(FileHandle fileHandle, int repeatX, int repeatY)
  {
    Pixmap source = new Pixmap(fileHandle);
    Pixmap result = createRepeat(source, repeatX, repeatY);
    source.dispose();
    return result;
  }

  public static void removeColor(Pixmap source, Color[] colors)
  {
    ByteBuffer pixels = source.getPixels();
    int w = source.getWidth();
    int h = source.getHeight();
    int size = w * h;
    int numBytes = w * h * 4;
    byte[] lines = new byte[numBytes];

    byte r = 0; byte g = 0; byte b = 0; byte a = 0;
    int index = 0;
    for (int i = 0; i < size; i++)
    {
      r = pixels.get();
      g = pixels.get();
      b = pixels.get();
      a = pixels.get();
      boolean ok = false;
      for (Color color : colors)
      {
        float r1 = color.r; float g1 = color.g; float b1 = color.b; float a1 = color.a;
        if ((r1 == 1.0F) && (g1 == 1.0F) && (b1 == 1.0F) && (a1 == 1.0F))
        {
          r1 = g1 = b1 = a1 = -1.0F;
        }
        if ((r == r1) && (g == g1) && (b == b1))
        {
          ok = true;
          break;
        }
      }
      if (ok)
      {
        r = 0;
        g = 0;
        b = 0;
        a = 0;
      }
      lines[(index++)] = r;
      lines[(index++)] = g;
      lines[(index++)] = b;
      lines[(index++)] = a;
    }
    pixels.clear();
    pixels.put(lines);
    pixels.clear();
  }

  public static void clearColorExcept(Pixmap source, Color color)
  {
    int colorBits = color.toIntBits();
    source.setColor(TRANSPARENT);
    int w = source.getWidth();
    int h = source.getHeight();
    for (int i = 0; i < w; i++)
    {
      for (int j = 0; j < h; j++)
      {
        System.out.println(String.format("Source: %d, Target: %d", new Object[] { Integer.valueOf(source.getPixel(i, j)), Integer.valueOf(colorBits) }));
        if (source.getPixel(i, j) != colorBits)
        {
          source.drawPixel(i, j);
        }
      }
    }
  }

  public static Pixmap toFormat(Pixmap source, Format targetFormat)
  {
    Pixmap pixmap = new Pixmap(source.getWidth(), source.getHeight(), targetFormat);
    pixmap.drawPixmap(source, 0, 0);
    return pixmap;
  }

  public static void replaceColor(Pixmap source, Color color, boolean keepAlpha)
  {
    ByteBuffer pixels = source.getPixels();
    int w = source.getWidth();
    int h = source.getHeight();
    int size = w * h;
    int numBytes = w * h * 4;
    byte[] lines = new byte[numBytes];

    byte r = 0; byte g = 0; byte b = 0; byte a = 0;
    int index = 0;
    int colorBits = color.toIntBits();
    for (int i = 0; i < size; i++)
    {
      r = pixels.get();
      g = pixels.get();
      b = pixels.get();
      a = pixels.get();
      if (a > -49)
      {
        if (!keepAlpha)
          a = (byte)((colorBits & 0xFF000000) >>> 24);
        b = (byte)((colorBits & 0xFF0000) >>> 16);
        g = (byte)((colorBits & 0xFF00) >>> 8);
        r = (byte)(colorBits & 0xFF);
      }
      lines[(index++)] = r;
      lines[(index++)] = g;
      lines[(index++)] = b;
      lines[(index++)] = a;
    }
    pixels.clear();
    pixels.put(lines);
    pixels.clear();
  }

  public static void replaceColor(Pixmap source, Color color)
  {
    source.setColor(color);
    int w = source.getWidth();
    int h = source.getHeight();
    for (int i = 0; i < w; i++)
    {
      for (int j = 0; j < h; j++)
      {
        if ((source.getPixel(i, j) & 0xFF) > 207)
        {
          source.drawPixel(i, j);
        }
      }
    }
  }

  public static void transparentBack(Pixmap source)
  {
    ByteBuffer pixels = source.getPixels();
    int w = source.getWidth();
    int h = source.getHeight();
    int size = w * h;
    int numBytes = w * h * 4;
    byte[] lines = new byte[numBytes];

    byte r = 0; byte g = 0; byte b = 0; byte a = 0;
    int index = 0;
    for (int i = 0; i < size; i++)
    {
      r = pixels.get();
      g = pixels.get();
      b = pixels.get();
      a = pixels.get();

      if ((r == 0) && (g == 0) && (b == 0))
      {
        r = -1;
        g = -1;
        b = -1;
        a = 0;
      }

      lines[(index++)] = r;
      lines[(index++)] = g;
      lines[(index++)] = b;
      lines[(index++)] = a;
    }
    pixels.clear();
    pixels.put(lines);
    pixels.clear();
  }

  public static Pixmap createGrid(int width, int height, int cols, int rows, int thickness, Color color, boolean drawBorder)
  {
    Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
    pixmap.setColor(Color.rgba8888(0.0F, 0.0F, 0.0F, 0.0F));
    pixmap.fillRectangle(0, 0, width, height);
    pixmap.setColor(color);
    if (drawBorder)
    {
      pixmap.drawRectangle(0, 0, width, height);
    }
    int partWidth = width / cols;
    int partHeight = height / rows;
    for (int i = 1; i < cols; i++)
    {
      int x = partWidth * i;
      pixmap.fillRectangle(x, 0, thickness, height);
    }
    for (int i = 1; i < rows; i++)
    {
      int y = partHeight * i;
      pixmap.fillRectangle(0, y, width, thickness);
    }
    return pixmap;
  }

  public static Pixmap createOverlay(Pixmap low, Pixmap overlay)
  {
    Pixmap pixmap = new Pixmap(low.getWidth(), low.getHeight(), low.getFormat());
    pixmap.drawPixmap(low, 0, 0);
    pixmap.drawPixmap(overlay, 0, 0);
    return pixmap;
  }

  public static Pixmap createRepeat(Pixmap source, int repeatX, int repeatY)
  {
    if (source == null)
      throw new GdxRuntimeException("source Pixmap can not be NULL.");
    if (repeatX == 0) repeatX = 1;
    if (repeatY == 0) repeatY = 1;
    int newWidth = source.getWidth() * repeatX;
    int newHeight = source.getHeight() * repeatY;
    Pixmap pixmap = new Pixmap(newWidth, newHeight, source.getFormat());
    int count = repeatX * repeatY;
    int x = 0; int y = 0;
    for (int i = 0; i < count; i++)
    {
      x = source.getWidth() * (i % repeatX);
      y = source.getHeight() * (i / repeatX);
      pixmap.drawPixmap(source, x, y);
    }
    return pixmap;
  }

  public static void binaryzation(Pixmap source)
  {
    ByteBuffer pixels = source.getPixels();
    int w = source.getWidth();
    int h = source.getHeight();
    int size = w * h;
    int numBytes = w * h * 4;
    byte[] lines = new byte[numBytes];

    byte r = 0; byte g = 0; byte b = 0; byte a = 0;
    int dr = 0; int dg = 0; int db = 0; int da = 0;
    int index = 0;
    for (int i = 0; i < size; i++)
    {
      r = pixels.get();
      g = pixels.get();
      b = pixels.get();
      a = pixels.get();
      dr = r & 0xFF;
      dg = g & 0xFF;
      db = b & 0xFF;
      da = a & 0xFF;

      if (223 < (dr + dg + db) / 3) {
        a = 0;
      }

      lines[(index++)] = r;
      lines[(index++)] = g;
      lines[(index++)] = b;
      lines[(index++)] = a;
    }
    pixels.clear();
    pixels.put(lines);
    pixels.clear();
  }

  public static Pixmap toPowerOf2(Pixmap source)
  {
    int width = powerOf2(source.getWidth());
    int height = powerOf2(source.getHeight());
    Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
    pixmap.drawPixmap(source, 0, 0, source.getWidth(), source.getHeight(), 0, 0, width, height);
    return pixmap;
  }

  private static int powerOf2(int value) {
    if (isPowerOf2(value)) return value;
    int temp1 = value;
    do
    {
      temp1--;
    }while (!isPowerOf2(temp1));

    int temp2 = value;
    do
    {
      temp2++;
    }while (!isPowerOf2(temp2));

    if (temp2 >= 1024) return temp1;
    return Math.abs(temp1 - value) < temp2 - value ? temp1 : temp2;
  }

  public static boolean isPowerOf2(int n) {
    return (n > 0) && ((n & n - 1) == 0);
  }

  public static void zeroToOne(Pixmap source) {
    ByteBuffer pixels = source.getPixels();
    int w = source.getWidth();
    int h = source.getHeight();
    int size = w * h;
    int numBytes = w * h * 4;
    byte[] lines = new byte[numBytes];

    byte r = 0; byte g = 0; byte b = 0; byte a = 0;
    int index = 0;
    for (int i = 0; i < size; i++)
    {
      r = pixels.get();
      g = pixels.get();
      b = pixels.get();
      a = pixels.get();
      if ((r == 0) && (g == 0) && (b == 0))
      {
        r = 9;
        g = 9;
        b = 9;
      }
      lines[(index++)] = r;
      lines[(index++)] = g;
      lines[(index++)] = b;
      lines[(index++)] = a;
    }
    pixels.clear();
    pixels.put(lines);
    pixels.clear();
  }
}