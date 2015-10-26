package cn.lbgongfu.housereloadinghelper;

public interface SettingsProvider
{
  public abstract void showSettingsDialog();

  public abstract void addDialogListener(DialogListener paramDialogListener);

  public static abstract interface DialogListener
  {
    public abstract void dialogOpened();

    public abstract void dialogClosed();
  }
}