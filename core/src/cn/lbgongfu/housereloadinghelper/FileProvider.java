package cn.lbgongfu.housereloadinghelper;

public interface FileProvider
{
  public abstract void selectFile(FileSelectedListener paramFileSelectedListener);

  public abstract void selectDir(FileSelectedListener paramFileSelectedListener);

  public static abstract interface FileSelectedListener
  {
    public abstract void selected(String paramString);
  }
}