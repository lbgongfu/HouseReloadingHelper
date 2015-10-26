package cn.lbgongfu.housereloadinghelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

public class SettingsPanel extends Table
{
  private Skin skin;
  private TextField fieldModelPath;
  private TextField fieldTexturePath;
  private TextField fieldSavePath;
  private TextField fieldPeriod;
  private TextField fieldMaxProcess;
  private CheckBox checkBoxTesting;
  private CheckBox checkBoxGenMipMaps;
  private SelectBox selectBoxMinFilter;
  private SelectBox selectBoxMagFilter;
  private Label labelMsg;
  private FileProvider fileProvider;
  private Settings settings;

  public SettingsPanel(Skin skin, FileProvider fileProvider)
  {
    this.skin = skin;
    this.fileProvider = fileProvider;
    createUI();
    this.settings = Settings.getInstance();
    this.fieldModelPath.setText(Settings.toPlatformSpecifiedPath(this.settings.getModelPath()));
    this.fieldTexturePath.setText(Settings.toPlatformSpecifiedPath(this.settings.getTexturePath()));
    this.fieldSavePath.setText(this.settings.getSavePath());
    this.fieldPeriod.setText(String.valueOf(this.settings.getPeriod()));
    this.fieldMaxProcess.setText(String.valueOf(this.settings.getMaxProcess()));
    this.checkBoxTesting.setChecked(this.settings.isTesting());
    this.checkBoxGenMipMaps.setChecked(this.settings.isTesting());
    this.selectBoxMinFilter.setSelected(this.settings.getMinFilter());
    this.selectBoxMagFilter.setSelected(this.settings.getMagFilter());
  }

  public boolean verified() {
    this.labelMsg.setText("");
    boolean result = true;
    StringBuilder sb = new StringBuilder();
    if ("".equals(this.fieldModelPath.getText()))
    {
      result = false;
      sb.append("Model Path can not be empty!").append("\n");
    }
    else if (Files.notExists(Paths.get(this.fieldModelPath.getText(), new String[0]), new LinkOption[0]))
    {
      result = false;
      sb.append("Model Path not exists!").append("\n");
    }
    if ("".equals(this.fieldTexturePath.getText()))
    {
      result = false;
      sb.append("Texture Path can not be empty!").append("\n");
    }
    else if (Files.notExists(Paths.get(this.fieldTexturePath.getText(), new String[0]), new LinkOption[0]))
    {
      result = false;
      sb.append("Texture Path not exists!").append("\n");
    }
    this.labelMsg.setText(sb);
    return result;
  }

  public void apply()
  {
    this.settings.setModelPath(Settings.toGdxAssetPath(this.fieldModelPath.getText()));
    this.settings.setTexturePath(Settings.toGdxAssetPath(this.fieldTexturePath.getText()));
    this.settings.setSavePath(this.fieldSavePath.getText());
    this.settings.setPeriod(Long.valueOf(this.fieldPeriod.getText()).longValue());
    this.settings.setMaxProcess(Integer.valueOf(this.fieldMaxProcess.getText()).intValue());
    this.settings.setTesting(this.checkBoxTesting.isChecked());
    this.settings.setGenMipMaps(this.checkBoxGenMipMaps.isChecked());
    this.settings.setMinFilter(this.selectBoxMinFilter.getSelected().toString());
    this.settings.setMagFilter(this.selectBoxMagFilter.getSelected().toString());
  }

  private void createUI() {
    defaults().space(10.0F);
    columnDefaults(0).align(Integer.valueOf(16));
    columnDefaults(1).minWidth(300.0F);

    Label labelModelPath = new Label("Model Path:", this.skin);
    this.fieldModelPath = new TextField("", this.skin);
    this.fieldModelPath.setMessageText("Location of Models");
    this.fieldModelPath.setDisabled(true);
    TextButton btnSelectModelPath = new TextButton("Browser", this.skin);
    btnSelectModelPath.addListener(new ChangeListener()
    {
      public void changed(ChangeEvent event, Actor actor) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldModelPath);
      }
    });
    Label labelTexturePath = new Label("Texture Path:", this.skin);
    this.fieldTexturePath = new TextField("", this.skin);
    this.fieldTexturePath.setMessageText("Location of Textures");
    this.fieldTexturePath.setDisabled(true);
    TextButton btnSelectTexturePath = new TextButton("Browser", this.skin);
    btnSelectTexturePath.addListener(new ChangeListener()
    {
      public void changed(ChangeEvent event, Actor actor) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldTexturePath);
      }
    });
    Label labelSavePath = new Label("Save Path:", this.skin);
    this.fieldSavePath = new TextField("", this.skin);
    this.fieldSavePath.setDisabled(true);
    TextButton btnSelectSavePath = new TextButton("Browser", this.skin);
    btnSelectSavePath.addListener(new ChangeListener()
    {
      public void changed(ChangeEvent event, Actor actor) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldSavePath);
      }
    });
    TextFieldFilter fieldFilter = new TextFieldFilter()
    {
      public boolean acceptChar(TextField textField, char c) {
        int num = c;
        return (num >= 48) && (num <= 57);
      }
    };
    Label labelPeriod = new Label("Period(millis):", this.skin);
    this.fieldPeriod = new TextField("", this.skin);
    this.fieldPeriod.setTextFieldFilter(fieldFilter);

    Label labelMaxProcess = new Label("Max Process:", this.skin);
    this.fieldMaxProcess = new TextField("", this.skin);
    this.fieldMaxProcess.setTextFieldFilter(fieldFilter);

    Label labelMinFilter = new Label("Min Filter", this.skin);
    String[] filters = new String[TextureFilter.values().length];
    int idx = 0;
    for (TextureFilter filter : TextureFilter.values()) {
      filters[(idx++)] = filter.toString();
    }
    this.selectBoxMinFilter = new SelectBox(this.skin);
    this.selectBoxMinFilter.setItems(filters);

    Label labelMagFilter = new Label("Mag Filter", this.skin);
    this.selectBoxMagFilter = new SelectBox(this.skin);
    this.selectBoxMagFilter.setItems(new Object[] { "Nearest", "Linear" });

    Table table = new Table();
    table.defaults().space(10.0F);
    table.columnDefaults(0).align(Integer.valueOf(16));
    table.columnDefaults(1).fillX();
    table.add(labelMinFilter);
    table.add(this.selectBoxMinFilter);
    table.row();
    table.add(labelMagFilter);
    table.add(this.selectBoxMagFilter);

    this.checkBoxGenMipMaps = new CheckBox("GenMipMaps", this.skin);
    this.checkBoxGenMipMaps.addListener(new ChangeListener()
    {
      public void changed(ChangeEvent event, Actor actor) {
        if (SettingsPanel.this.checkBoxGenMipMaps.isChecked())
        {
          SettingsPanel.this.selectBoxMinFilter.setDisabled(false);
          SettingsPanel.this.selectBoxMagFilter.setDisabled(false);
        }
        else
        {
          SettingsPanel.this.selectBoxMinFilter.setDisabled(true);
          SettingsPanel.this.selectBoxMagFilter.setDisabled(true);
        }
      }
    });
    final Label labelTips = new Label("Tap 'Enter' to execute once", this.skin);
    labelTips.setColor(Color.YELLOW);
    labelTips.setVisible(false);
    this.checkBoxTesting = new CheckBox("Testing", this.skin);
    this.checkBoxTesting.addListener(new ChangeListener()
    {
      public void changed(ChangeEvent event, Actor actor) {
        if (SettingsPanel.this.checkBoxTesting.isChecked())
          labelTips.setVisible(true);
        else
          labelTips.setVisible(false);
      }
    });
    this.labelMsg = new Label("", this.skin);
    this.labelMsg.setColor(Color.RED);

    add(labelModelPath);
    add(this.fieldModelPath);
    add(btnSelectModelPath);
    row();
    add(labelTexturePath);
    add(this.fieldTexturePath);
    add(btnSelectTexturePath);
    row();
    add(labelSavePath);
    add(this.fieldSavePath);
    add(btnSelectSavePath);
    row();
    add(labelPeriod);
    add(this.fieldPeriod);
    row();
    add(labelMaxProcess);
    add(this.fieldMaxProcess);
    row();
    add(this.checkBoxGenMipMaps);
    add(table);
    row();
    add(this.checkBoxTesting);
    add(labelTips).colspan(Integer.valueOf(2));
    row();
    add(this.labelMsg).height(100.0F).colspan(Integer.valueOf(3)).align(Integer.valueOf(16));
    pack();
  }

  private void showFileChooser(final TextField field) {
    this.fileProvider.selectDir(new FileProvider.FileSelectedListener()
    {
      public void selected(String filePath) {
        field.setText(filePath);
      }
    });
  }
}