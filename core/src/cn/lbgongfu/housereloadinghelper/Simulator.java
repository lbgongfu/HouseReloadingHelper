package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.lbgongfu.housereloadinghelper.models.ModelViewBean;
import cn.lbgongfu.log.LogUtil;

public class Simulator
{
  private static final Logger logger = LogUtil.getLogger(Simulator.class.getName());
  private static final String TAG = Simulator.class.getName();
  private static final BoundingBox bounding = new BoundingBox();
  private Main game;
  private MainScreen screen;
  private Timer timer;
  private TextureParameter textureParams;

  private Array<ModelInstance> instances = new Array();
  private Array<Camera> cameras = new Array();
  private List<ModelViewBean> beans = new ArrayList();
  private List<ModelViewBean> availableBeans = new ArrayList();
  private Set<String> visited = new HashSet();
  private int currIndex = -1;
  private boolean loading;
  private boolean saved = true;
  private boolean completed;

  public Simulator(Main game, MainScreen screen)
  {
    this.game = game;
    this.screen = screen;
    completed = true;

    textureParams = new TextureParameter();
    textureParams.genMipMaps = Settings.getInstance().isGenMipMaps();
  }

  public void start()
  {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask()
    {
      public void run() {
        if (game.factory == null) return;
        if (Settings.getInstance().isTesting()) return;
        if (completed && (!screen.isShowingDialog()))
          execute();
      }
    }
    , 0L, Settings.getInstance().getPeriod());
  }

  public void stop()
  {
    timer.cancel();
  }

  public boolean isLoading()
  {
    return loading;
  }
  public boolean isCompleted() {
    return completed;
  }
  public void update() {
    if ((loading) && (game.assetManager.update()))
      doneLoading();
  }

  public void takeScreenShot() {
    if (saved)
    {
      saved = false;
      final ModelViewBean bean = availableBeans.get(currIndex);
      String fileName = "";
      if (bean.getGapType() == 0)
        fileName = String.format("%s_%s_%s.png", new Object[] { bean.getDemoName(), Integer.valueOf(bean.getTieTuCeng()), Long.valueOf(bean.getProductId()) });
      else
        fileName = String.format("%s_%s_%s_%s_%s_%s.png", new Object[] { bean.getDemoName(), Integer.valueOf(bean.getTieTuCeng()), bean.getGapCuttingWay(), Integer.valueOf(bean.getGapType()), Integer.valueOf(bean.getGap()), bean.getGapColor() });
      Gdx.app.debug(TAG, "save \"" + fileName + "\"");
      final Path path = Paths.get(Settings.getInstance().getSavePath(), new String[]{fileName});
      final Pixmap pixmap = ScreenshotFactory.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
      game.executorService.execute(new Runnable()
      {
        public void run() {
          PixmapIO.writePNG(Gdx.files.absolute(path.toString()), pixmap);
          pixmap.dispose();
          try
          {
            game.factory.del(bean);
          }
          catch (Exception e)
          {
            e.printStackTrace();
            Simulator.logger.log(Level.SEVERE, e.getMessage());
          }
          saved = true;
        }
      });
      currIndex += 1;
      if (currIndex == instances.size)
      {
        screen.update(null, null);
        completed = true;
      }
      else
      {
        screen.update(instances.get(currIndex), cameras.get(currIndex));
      }
    }
  }

  private void load() {
    Gdx.app.postRunnable(new Runnable()
    {
      public void run() {
        visited.clear();
        for (ModelViewBean bean : beans)
        {
          String modelPath = Settings.getInstance().getModelPath() + bean.getModelPath();
          String texturePath = Settings.getInstance().getTexturePath() + bean.getTexturePath();
          boolean error = false;
          if (Files.notExists(Paths.get(modelPath)))
          {
            error = true;
            logger.log(Level.WARNING, String.format("File '%s' does not exist", modelPath));
          }
          if (Files.notExists(Paths.get(texturePath)))
          {
            error = true;
            logger.log(Level.WARNING, String.format("File '%s' does not exist", texturePath));
          }
          if (!error) {
            if (!visited.contains(modelPath))
            {
              game.assetManager.load(modelPath, Model.class);
              visited.add(modelPath);
            }
            if (!visited.contains(texturePath))
            {
              game.assetManager.load(texturePath, Texture.class, textureParams);
              visited.add(texturePath);
            }
          }
        }
        loading = true;
      }
    });
  }

  private void doneLoading() {
    for (ModelViewBean bean : beans)
    {
      String modelPath = Settings.getInstance().getModelPath() + bean.getModelPath();
      String texturePath = Settings.getInstance().getTexturePath() + bean.getTexturePath();

      if (!game.assetManager.isLoaded(modelPath))
        continue;
      if (!game.assetManager.isLoaded(texturePath))
        continue;
      Model model = game.assetManager.get(modelPath, Model.class);
      Texture texture = game.assetManager.get(texturePath, Texture.class);
      if (Settings.getInstance().isGenMipMaps())
        texture.setFilter(Texture.TextureFilter.valueOf(Settings.getInstance().getMinFilter()), Texture.TextureFilter.valueOf(Settings.getInstance().getMagFilter()));
      else {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      }
      texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

      ModelInstance instance = null;
      if (model.getNode(bean.getNodeId()) == null)
      {
        logger.log(Level.WARNING, "node id is incorrect");
        continue;
      }
      instance = new ModelInstance(model, bean.getNodeId(), true);

      for (Node node : instance.nodes)
      {
        for (NodePart nodePart : node.parts)
        {
          MeshPart meshPart = nodePart.meshPart;
          meshPart.mesh.calculateBoundingBox(bounding, meshPart.indexOffset, meshPart.numVertices);
          float[] values = new float[3];
          values[0] = bounding.getWidth();
          values[1] = bounding.getHeight();
          values[2] = bounding.getDepth();
          Arrays.sort(values);
          float width = values[2];
          float height = values[1];
          float repeatX = width / texture.getWidth();
          float repeatY = height / texture.getHeight();
          updateUV(meshPart, repeatX, repeatY);
        }
      }

      TextureAttribute attr = TextureAttribute.createDiffuse(texture);
      BlendingAttribute blending = new BlendingAttribute();
      for (Material material : instance.materials)
      {
        material.set(attr);
        material.set(blending);
      }
      instances.add(instance);

      Camera camera = new PerspectiveCamera((Constants.MAX_GDX_FIELD_MAP.get(Float.valueOf(bean.getCameraFocalLength().getLength()))).floatValue(),
              Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//      camera.position.set(bean.getCameraX(), bean.getCameraZ(), -bean.getCameraY());
//      camera.lookAt(bean.getCameraTargetX(), bean.getCameraTargetZ(), -bean.getCameraTargetY());
      camera.position.set(bean.getCameraX(), bean.getCameraY(), bean.getCameraZ());
      camera.lookAt(bean.getCameraTargetX(), bean.getCameraTargetY(), bean.getCameraTargetZ());
      camera.near = bean.getCameraNear();
      camera.far = bean.getCameraFar();
      camera.update();
      cameras.add(camera);

      availableBeans.add(bean);
    }
    if (instances.size == 0)
    {
      completed = true;
      loading = false;
      return;
    }
    currIndex = 0;

    screen.update(instances.get(currIndex), cameras.get(currIndex));
    loading = false;
  }

  private int calcNumComponentsPerVertex (Mesh mesh) {
    int numComponents = 0;
    VertexAttributes attrs = mesh.getVertexAttributes();
    Iterator<VertexAttribute> ite = attrs.iterator();
    while (ite.hasNext())
    {
      VertexAttribute attr = ite.next();
      numComponents += attr.numComponents;
    }
    return numComponents;
  }

  private void updateUV (MeshPart meshPart, float repeatX, float repeatY) {
    int numComponents = calcNumComponentsPerVertex(meshPart.mesh);
    float[] totalVertices = new float[meshPart.mesh.getNumVertices() * numComponents];
    meshPart.mesh.getVertices(totalVertices);
    if (meshPart.mesh.getNumIndices() > 0)
    {
      short[] indices = new short[meshPart.numVertices];
      meshPart.mesh.getIndices(meshPart.indexOffset, meshPart.numVertices, indices, 0);
      Set<Short> set = new HashSet<Short>();
      for (short v : indices)
        set.add(v);
      for (short v : set)
      {
        int index = v * numComponents + numComponents;
        totalVertices[index - 2] *= repeatX;
        totalVertices[index - 1] *= repeatY;
      }
    }
    else
    {
      for (int i = meshPart.indexOffset; i < meshPart.numVertices; i += numComponents)
      {
        int index = i + numComponents;
        totalVertices[index - 2] *= repeatX;
        totalVertices[index - 1] *= repeatY;
      }
    }
    meshPart.mesh.updateVertices(0, totalVertices);
  }

  private void clear() {
    visited.clear();
    for (ModelViewBean bean : beans)
    {
      String modelPath = Settings.getInstance().getModelPath() + bean.getModelPath();
      String texturePath = Settings.getInstance().getTexturePath() + bean.getTexturePath();
      if (!visited.contains(modelPath))
      {
        try
        {
          game.assetManager.unload(modelPath);
          visited.add(modelPath);
        } catch (Exception localException) {
        }
      }
      if (!visited.contains(texturePath))
      {
        try
        {
          game.assetManager.unload(texturePath);
          visited.add(texturePath);
        } catch (Exception localException1) {
        }
      }
    }
    beans.clear();
    availableBeans.clear();
    cameras.clear();
    instances.clear();
  }

  public void execute() {
    if (game.factory == null) return;
    completed = false;
    Gdx.app.postRunnable(new Runnable()
    {
      public void run() {
        clear();
        game.executorService.execute(new Runnable()
        {
          public void run()
          {
            try {
              beans.addAll(game.factory.createList());
              if (beans.size() == 0) {
                completed = true;
                return;
              }
              load();
            }
            catch (Exception e)
            {
              completed = true;
              logger.log(Level.SEVERE, e.getMessage());
            }
          }
        });
      }
    });
  }
}