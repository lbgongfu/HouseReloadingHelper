package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class MainScreenMediator
{
  private Main game;
  private MainScreen screen;
  private Simulator simulator;
  private InputAdapter inputProcessor;

  public MainScreenMediator(Main game, MainScreen screen, Simulator simulator)
  {
    this.game = game;
    this.screen = screen;
    this.simulator = simulator;
  }

  public InputProcessor inputProcessor()
  {
    if (this.inputProcessor == null)
    {
      this.inputProcessor = new InputAdapter()
      {
        public boolean keyDown(int keycode)
        {
          if (keycode == 255)
          {
            MainScreenMediator.this.game.settingsProvider.showSettingsDialog();
          }
          else if (keycode == 66)
          {
            if ((MainScreenMediator.this.simulator.isCompleted()) && (!MainScreenMediator.this.screen.isShowingDialog()))
              MainScreenMediator.this.simulator.execute();
          }
          return super.keyDown(keycode);
        }
      };
    }
    return this.inputProcessor;
  }
}