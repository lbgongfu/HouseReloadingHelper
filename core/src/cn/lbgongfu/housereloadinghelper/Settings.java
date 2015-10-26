package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;

public class Settings
{
  private static final String KEY_MODEL_PATH = "modelPath";
  private static final String KEY_TEXTURE_PATH = "texturePath";
  private static final String KEY_SAVE_PATH = "savePath";
  private static final String KEY_PERIOD = "period";
  private static final String KEY_MAX_PROCESS = "maxProcess";
  private static final String KEY_TESTING = "testing";
  private static final String KEY_GEN_MIP_MAPS = "genMipMaps";
  private static final String KEY_MIN_FILTER = "minFilter";
  private static final String KEY_MAG_FILTER = "magFilter";
  private final Preferences prefs;
  private static Settings instance;
  private String modelPath;
  private String texturePath;
  private String savePath;
  private long period;
  private int maxProcess;
  private boolean testing;
  private boolean genMipMaps = true;
  private String minFilter;
  private String magFilter;

  private Settings()
  {
    this.prefs = Gdx.app.getPreferences("settings");

    this.modelPath = this.prefs.getString(KEY_MODEL_PATH);
    this.texturePath = this.prefs.getString(KEY_TEXTURE_PATH);

    this.savePath = this.prefs.getString(KEY_SAVE_PATH);
    if ((this.savePath == null) || ("".equals(this.savePath)))
    {
      this.savePath = Paths.get(System.getProperty("user.home"), new String[] { "demoupload" }).toString();
      this.prefs.putString(KEY_SAVE_PATH, this.savePath);
    }
    Path path = Paths.get(this.savePath, new String[0]);
    if (!Files.exists(path, new LinkOption[0])) {
      try
      {
        Files.createDirectory(path, new FileAttribute[0]);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    this.period = this.prefs.getLong(KEY_PERIOD);
    if (this.period <= 0L)
    {
      this.period = 1000L;
      this.prefs.putLong(KEY_PERIOD, this.period);
    }

    this.maxProcess = this.prefs.getInteger(KEY_MAX_PROCESS);
    if (this.maxProcess <= 0)
    {
      this.maxProcess = 100;
      this.prefs.putInteger(KEY_MAX_PROCESS, this.maxProcess);
    }
    this.prefs.flush();

    this.testing = this.prefs.getBoolean(KEY_TESTING);

    this.genMipMaps = this.prefs.getBoolean(KEY_GEN_MIP_MAPS);

    this.minFilter = this.prefs.getString(KEY_MIN_FILTER);
    if (this.minFilter == null)
    {
      this.minFilter = "MipMap";
      this.prefs.putString(KEY_MIN_FILTER, this.minFilter);
    }

    this.magFilter = this.prefs.getString(KEY_MAG_FILTER);
    if (this.magFilter == null)
    {
      this.magFilter = "Linear";
      this.prefs.putString(KEY_MAG_FILTER, this.magFilter);
    }
  }

  public static Settings getInstance()
  {
    if (instance == null)
      instance = new Settings();
    return instance;
  }

  public String getModelPath() {
    return this.modelPath;
  }

  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
    this.prefs.putString(KEY_MODEL_PATH, modelPath);
    this.prefs.flush();
  }

  public String getTexturePath()
  {
    return this.texturePath;
  }

  public void setTexturePath(String texturePath)
  {
    this.texturePath = texturePath;
    this.prefs.putString(KEY_TEXTURE_PATH, texturePath);
    this.prefs.flush();
  }

  public String getSavePath() {
    return this.savePath;
  }

  public void setSavePath(String saveDir) {
    this.savePath = saveDir;
    this.prefs.putString(KEY_SAVE_PATH, saveDir);
    this.prefs.flush();
  }

  public long getPeriod() {
    return this.period;
  }

  public void setPeriod(long period) {
    this.period = period;
    this.prefs.putLong(KEY_PERIOD, period);
    this.prefs.flush();
  }

  public int getMaxProcess() {
    return this.maxProcess;
  }

  public void setMaxProcess(int maxProcess) {
    this.maxProcess = maxProcess;
    this.prefs.putLong(KEY_MAX_PROCESS, maxProcess);
    this.prefs.flush();
  }

  public boolean isTesting() {
    return this.testing;
  }

  public void setTesting(boolean testing) {
    this.testing = testing;
    this.prefs.putBoolean(KEY_TESTING, testing);
    this.prefs.flush();
  }

  public boolean isGenMipMaps() {
    return this.genMipMaps;
  }

  public void setGenMipMaps(boolean genMipMaps) {
    this.genMipMaps = genMipMaps;
    this.prefs.putBoolean(KEY_GEN_MIP_MAPS, genMipMaps);
    this.prefs.flush();
  }

  public String getMinFilter() {
    return this.minFilter;
  }

  public void setMinFilter(String minFilter) {
    this.minFilter = minFilter;
    this.prefs.putString(KEY_MIN_FILTER, minFilter);
    this.prefs.flush();
  }

  public String getMagFilter() {
    return this.magFilter;
  }

  public void setMagFilter(String maxFilter) {
    this.magFilter = maxFilter;
    this.prefs.putString(KEY_MAG_FILTER, maxFilter);
    this.prefs.flush();
  }

  public static String toGdxAssetPath(String sourcePath)
  {
    StringBuilder sb = new StringBuilder();
    ArrayList<String> list = new ArrayList<String>();
    Path path = Paths.get(sourcePath, new String[0]);
    String temp = path.getRoot().toString();
    list.add(temp.substring(0, temp.length() - 1));
    Iterator<Path> iterator = path.iterator();
    while (iterator.hasNext())
    {
      list.add(((Path)iterator.next()).toString());
    }
    int i = 0; for (int count = list.size(); i < count; i++)
    {
      sb.append((String)list.get(i));
      sb.append("/");
    }
    return sb.toString();
  }

  public static String toPlatformSpecifiedPath(String gdxAssetPath)
  {
    String[] segments = gdxAssetPath.split("/");
    StringBuilder sb = new StringBuilder();
    int i = 0; for (int count = segments.length; i < count; i++)
    {
      sb.append(segments[i]);
      if (i != count - 1)
        sb.append(File.separator);
    }
    return sb.toString();
  }
}