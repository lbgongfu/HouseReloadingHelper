package cn.lbgongfu.housereloadinghelper.desktop;

import com.badlogic.gdx.graphics.Texture.TextureFilter;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.lbgongfu.housereloadinghelper.FileProvider;
import cn.lbgongfu.housereloadinghelper.Settings;

public class SettingsPanel extends JPanel
{
  private JTextField fieldModelPath;
  private JTextField fieldTexturePath;
  private JTextField fieldSavePath;
  private JTextField fieldPeriod;
  private JTextField fieldMaxProcess;
  private JCheckBox checkBoxTesting;
  private JCheckBox checkBoxGenMipMaps;
  private JComboBox<String> selectBoxMinFilter;
  private JComboBox<String> selectBoxMagFilter;
  private JLabel labelMsg;
  private static final long serialVersionUID = 1L;

  public SettingsPanel()
  {
    setLayout(new GridBagLayout());
    createUI();
    Settings settings = Settings.getInstance();
    this.fieldModelPath.setText(Settings.toPlatformSpecifiedPath(settings.getModelPath()));
    this.fieldTexturePath.setText(Settings.toPlatformSpecifiedPath(settings.getTexturePath()));
    this.fieldSavePath.setText(settings.getSavePath());
    this.fieldPeriod.setText(String.valueOf(settings.getPeriod()));
    this.fieldMaxProcess.setText(String.valueOf(settings.getMaxProcess()));
    this.checkBoxTesting.setSelected(settings.isTesting());
    this.checkBoxGenMipMaps.setSelected(settings.isGenMipMaps());
    this.selectBoxMinFilter.setSelectedItem(settings.getMinFilter());
    this.selectBoxMagFilter.setSelectedItem(settings.getMagFilter());
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
    this.labelMsg.setText(sb.toString());
    return result;
  }

  public void apply() {
    Settings.getInstance().setModelPath(Settings.toGdxAssetPath(this.fieldModelPath.getText()));
    Settings.getInstance().setTexturePath(Settings.toGdxAssetPath(this.fieldTexturePath.getText()));
    Settings.getInstance().setSavePath(this.fieldSavePath.getText());
    Settings.getInstance().setPeriod(Long.valueOf(this.fieldPeriod.getText()).longValue());
    Settings.getInstance().setMaxProcess(Integer.valueOf(this.fieldMaxProcess.getText()).intValue());
    Settings.getInstance().setTesting(this.checkBoxTesting.isSelected());
    Settings.getInstance().setGenMipMaps(this.checkBoxGenMipMaps.isSelected());
    Settings.getInstance().setMinFilter(this.selectBoxMinFilter.getSelectedItem().toString());
    Settings.getInstance().setMagFilter(this.selectBoxMagFilter.getSelectedItem().toString());
  }

  private void createUI() {
    JLabel labelModelPath = new JLabel("Model Path:");
    this.fieldModelPath = new JTextField("");
    this.fieldModelPath.setToolTipText("Location of Models");
    this.fieldModelPath.setEditable(false);
    JButton btnSelectModelPath = new JButton("Browser");
    btnSelectModelPath.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldModelPath);
      }
    });
    JLabel labelTexturePath = new JLabel("Texture Path:");
    this.fieldTexturePath = new JTextField("");
    this.fieldTexturePath.setToolTipText("Location of Textures");
    this.fieldTexturePath.setEditable(false);
    JButton btnSelectTexturePath = new JButton("Browser");
    btnSelectTexturePath.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldTexturePath);
      }
    });
    JLabel labelSavePath = new JLabel("Save Path:");
    this.fieldSavePath = new JTextField("");
    this.fieldSavePath.setEditable(false);
    JButton btnSelectSavePath = new JButton("Browser");
    btnSelectSavePath.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        SettingsPanel.this.showFileChooser(SettingsPanel.this.fieldSavePath);
      }
    });
    JLabel labelPeriod = new JLabel("Period(millis):");
    this.fieldPeriod = new JTextField("");

    JLabel labelMaxProcess = new JLabel("Max Process:");
    this.fieldMaxProcess = new JTextField("");

    JLabel labelMinFilter = new JLabel("Min Filter");
    String[] filters = new String[TextureFilter.values().length];
    int idx = 0;
    for (TextureFilter filter : TextureFilter.values()) {
      filters[(idx++)] = filter.toString();
    }
    this.selectBoxMinFilter = new JComboBox(filters);

    JLabel labelMagFilter = new JLabel("Mag Filter");
    this.selectBoxMagFilter = new JComboBox(new String[] { "Nearest", "Linear" });

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    panel.add(labelMinFilter, constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.fill = 2;
    panel.add(this.selectBoxMinFilter, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    panel.add(labelMagFilter, constraints);

    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.fill = 2;
    panel.add(this.selectBoxMagFilter, constraints);

    this.checkBoxGenMipMaps = new JCheckBox("GenMipMaps");
    this.checkBoxGenMipMaps.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        if (SettingsPanel.this.checkBoxGenMipMaps.isSelected())
        {
          SettingsPanel.this.selectBoxMinFilter.setEnabled(true);
          SettingsPanel.this.selectBoxMagFilter.setEnabled(true);
        }
        else
        {
          SettingsPanel.this.selectBoxMinFilter.setEnabled(false);
          SettingsPanel.this.selectBoxMagFilter.setEnabled(false);
        }
      }
    });
    final JLabel labelTips = new JLabel("Tap 'Enter' to execute once");
    labelTips.setForeground(Color.BLUE);
    labelTips.setVisible(false);
    this.checkBoxTesting = new JCheckBox("Testing");
    this.checkBoxTesting.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent arg0) {
        if (SettingsPanel.this.checkBoxTesting.isSelected())
          labelTips.setVisible(true);
        else
          labelTips.setVisible(false);
      }
    });
    this.labelMsg = new JLabel("");
    this.labelMsg.setForeground(Color.RED);

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = 13;
    add(labelModelPath, constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.fill = 2;
    add(this.fieldModelPath, constraints);

    constraints.gridx = 2;
    constraints.gridy = 0;
    add(btnSelectModelPath, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.anchor = 13;
    add(labelTexturePath, constraints);

    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.fill = 2;
    add(this.fieldTexturePath, constraints);

    constraints.gridx = 2;
    constraints.gridy = 1;
    add(btnSelectTexturePath, constraints);

    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.anchor = 13;
    add(labelSavePath, constraints);

    constraints.gridx = 1;
    constraints.gridy = 2;
    constraints.fill = 2;
    add(this.fieldSavePath, constraints);

    constraints.gridx = 2;
    constraints.gridy = 2;
    add(btnSelectSavePath, constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.anchor = 13;
    add(labelPeriod, constraints);

    constraints.gridx = 1;
    constraints.gridy = 3;
    constraints.fill = 2;
    add(this.fieldPeriod, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.anchor = 13;
    add(labelMaxProcess, constraints);

    constraints.gridx = 1;
    constraints.gridy = 4;
    constraints.fill = 2;
    add(this.fieldMaxProcess, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    constraints.anchor = 13;
    add(this.checkBoxGenMipMaps, constraints);

    constraints.gridx = 1;
    constraints.gridy = 5;
    add(panel, constraints);

    constraints.gridx = 0;
    constraints.gridy = 6;
    constraints.anchor = 13;
    add(this.checkBoxTesting, constraints);

    constraints.gridx = 1;
    constraints.gridy = 6;
    add(labelTips, constraints);

    constraints.gridx = 0;
    constraints.gridy = 7;
    add(this.labelMsg, constraints);
  }

  private void showFileChooser(final JTextField field) {
    new DesktopFileProvider(this).selectDir(new FileProvider.FileSelectedListener()
    {
      public void selected(String filePath) {
        field.setText(filePath);
      }
    });
  }
}