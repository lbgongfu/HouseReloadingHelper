package cn.lbgongfu.housereloadinghelper.services.impls;

import com.badlogic.gdx.Gdx;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import cn.lbgongfu.housereloadinghelper.Settings;
import cn.lbgongfu.housereloadinghelper.models.ModelViewBean;
import cn.lbgongfu.housereloadinghelper.services.ModelViewBeanFactory;

public class ModelViewBeanFactoryImpl
  implements ModelViewBeanFactory
{
  private static final String TAG = ModelViewBeanFactoryImpl.class.getName();
  private EntityManagerFactory entityManagerFactory;

  public ModelViewBeanFactoryImpl()
  {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
  }

  public ModelViewBean create(String xml)
  {
    try
    {
      FileInputStream inputStream = new FileInputStream(xml);
      XMLDecoder decoder = new XMLDecoder(inputStream);
      return (ModelViewBean)decoder.readObject();
    }
    catch (IOException e)
    {
      Gdx.app.error(TAG, e.getMessage());
    }
    return null;
  }

  public void save(ModelViewBean bean, String filePath)
  {
    try
    {
      FileOutputStream outputStream = new FileOutputStream(filePath);
      XMLEncoder encoder = new XMLEncoder(outputStream, "utf-8", true, 0);
      encoder.writeObject(bean);
      encoder.close();
    }
    catch (IOException e)
    {
      Gdx.app.error(TAG, e.getMessage());
    }
  }

  public void update(ModelViewBean bean)
  {
    EntityManager entityManager = this.entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(bean);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  public Collection<? extends ModelViewBean> createList()
  {
    EntityManager entityManager = this.entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    String queryString = "";
    if (Settings.getInstance().isTesting())
      queryString = "from ModelViewBean";
    else
      queryString = "from ModelViewBean a where a.flag = 0";
    TypedQuery query = entityManager.createQuery(queryString, ModelViewBean.class);

    query.setMaxResults(Settings.getInstance().getMaxProcess());
    List result = query.getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();
    return result;
  }

  public void close()
  {
    if (this.entityManagerFactory != null)
      this.entityManagerFactory.close();
  }

  public void del(ModelViewBean bean)
  {
    EntityManager entityManager = this.entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    bean = (ModelViewBean)entityManager.find(ModelViewBean.class, Long.valueOf(bean.getId()));
    entityManager.remove(bean);
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}