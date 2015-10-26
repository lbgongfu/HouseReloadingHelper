package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.lbgongfu.housereloadinghelper.services.ModelViewBeanFactory;

public class Main extends Game
{
  public AssetManager assetManager;
  public Settings settings;
  public FileProvider fileProvider;
  public SettingsProvider settingsProvider;
  public ExecutorService executorService;
  public ModelViewBeanFactory factory;
  public Skin skin;

  public Main(FileProvider fileProvider, SettingsProvider settingsProvider)
  {
    this.fileProvider = fileProvider;
    this.settingsProvider = settingsProvider;
    this.executorService = Executors.newCachedThreadPool();
  }

  public void setFactory(ModelViewBeanFactory beanFactory)
  {
    this.factory = beanFactory;
  }

  public void create()
  {
    Gdx.app.setLogLevel(1);
    Pixmap.setBlending(Blending.None);
    this.settings = Settings.getInstance();
    this.assetManager = new AssetManager(new FileHandleResolver()
    {
      public FileHandle resolve(String fileName)
      {
        return Gdx.files.absolute(fileName);
      }
    });
    setScreen(new MainScreen(this));
  }

  public void render()
  {
    super.render();
  }

  public void dispose()
  {
    if (this.factory != null)
      this.factory.close();
    try {
      this.executorService.awaitTermination(2L, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.executorService.shutdownNow();
    try
    {
      getScreen().dispose();
      this.assetManager.dispose();
    }
    catch (Exception localException) {
    }
  }

  public boolean exit() {
    return false;
  }
}