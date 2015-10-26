package cn.lbgongfu.housereloadinghelper.services;

import java.util.Collection;

import cn.lbgongfu.housereloadinghelper.models.ModelViewBean;

public abstract interface ModelViewBeanFactory
{
  public abstract ModelViewBean create(String paramString);

  public abstract void save(ModelViewBean paramModelViewBean, String paramString);

  public abstract void update(ModelViewBean paramModelViewBean);

  public abstract void del(ModelViewBean paramModelViewBean);

  public abstract Collection<? extends ModelViewBean> createList();

  public abstract void close();
}