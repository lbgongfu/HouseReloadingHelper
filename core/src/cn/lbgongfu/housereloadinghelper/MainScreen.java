package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MainScreen extends ScreenAdapter
  implements SettingsProvider.DialogListener
{
  private Main game;
  private Stage stage;
  private ModelBatch modelBatch;
  private Texture textureLogo;
  private Image imageLogo;
  private boolean showingDialog;
  private Camera currCamera;
  private ModelInstance currModelInstance;
  private Simulator simulator;

  public MainScreen(Main game)
  {
    this.game = game;
    modelBatch = new ModelBatch();
    game.settingsProvider.addDialogListener(this);
    simulator = new Simulator(game, this);
    MainScreenMediator mediator = new MainScreenMediator(game, this, simulator);
    stage = new Stage();
    textureLogo = new Texture("data/libgdx.png");
    imageLogo = new Image(textureLogo);
    imageLogo.setX((Gdx.graphics.getWidth() - imageLogo.getWidth()) / 2.0F);
    imageLogo.setY((Gdx.graphics.getHeight() - imageLogo.getHeight()) / 2.0F);
    imageLogo.setVisible(false);
    stage.addActor(imageLogo);
    Gdx.input.setInputProcessor(new InputMultiplexer(stage, mediator.inputProcessor()));
    simulator.start();
  }

  public void show()
  {
    game.settingsProvider.showSettingsDialog();
  }

  public void render(float delta)
  {
    simulator.update();
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    if ((!isShowingDialog()) && ((simulator.isLoading()) || (currModelInstance == null) || (currCamera == null)))
      imageLogo.setVisible(true);
    else {
      imageLogo.setVisible(false);
    }
    stage.act();
    stage.draw();

    if ((simulator.isLoading()) || (isShowingDialog()) || (currModelInstance == null) || (currCamera == null)) return;

    modelBatch.begin(currCamera);
    modelBatch.render(currModelInstance);
    modelBatch.end();
    simulator.takeScreenShot();
  }

  public void dispose()
  {
    simulator.stop();
    textureLogo.dispose();
    stage.dispose();
    modelBatch.dispose();
  }

  public void showSettingsPanel()
  {
    showingDialog = true;
    final SettingsPanel settingsPanel = new SettingsPanel(game.skin, game.fileProvider);
    Dialog settingsDialog = new Dialog("Settings", game.skin)
    {
      protected void result(Object object)
      {
        if (!settingsPanel.verified())
        {
          cancel();
          return;
        }
        if ((object != null) && ("Ok".equals(object.toString())))
        {
          settingsPanel.apply();
        }
        showingDialog = false;
      }
    };
    settingsDialog.setModal(true);
    settingsDialog.getContentTable().add(settingsPanel);
    settingsDialog.button("Ok", "Ok");
    settingsDialog.button("Cancel");
    settingsDialog.show(stage);
  }

  public synchronized boolean isShowingDialog()
  {
    return showingDialog;
  }

  public synchronized void setShowingDialog(boolean showingDialog)
  {
    this.showingDialog = showingDialog;
  }

  public void update(ModelInstance modelInstance, Camera camera)
  {
    currModelInstance = modelInstance;
    currCamera = camera;
  }

  public void dialogOpened()
  {
    setShowingDialog(true);
  }

  public void dialogClosed()
  {
    setShowingDialog(false);
  }
}