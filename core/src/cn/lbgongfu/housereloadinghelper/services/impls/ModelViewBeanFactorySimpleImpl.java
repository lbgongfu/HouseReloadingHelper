package cn.lbgongfu.housereloadinghelper.services.impls;

import java.util.ArrayList;
import java.util.Collection;

import cn.lbgongfu.housereloadinghelper.models.FocalLength;
import cn.lbgongfu.housereloadinghelper.models.ModelViewBean;
import cn.lbgongfu.housereloadinghelper.services.ModelViewBeanFactory;

public class ModelViewBeanFactorySimpleImpl
  implements ModelViewBeanFactory
{
  private int count = 0;

  public ModelViewBean create(String xml)
  {
    return null;
  }

  public void save(ModelViewBean bean, String filePath)
  {
  }

  public void update(ModelViewBean bean)
  {
  }

  public Collection<? extends ModelViewBean> createList()
  {
    ArrayList beans = new ArrayList();
    this.count = 0;
    if (this.count < 1) {
      this.count += 1;
      ModelViewBean bean;

      bean = new ModelViewBean();
      bean.setDemoName("demo");
      bean.setTieTuType("dm");
      bean.setTieTuCeng(1);
      bean.setProductId(7L);
      bean.setModelPath("plane.g3dj");
      bean.setNodeId("pPlane1");
      bean.setTexturePath("256.png");
      bean.setCameraX(50F);
      bean.setCameraY(50F);
      bean.setCameraZ(50F);
      bean.setCameraTargetX(0F);
      bean.setCameraTargetY(0F);
      bean.setCameraTargetZ(0F);
      bean.setCameraNear(1.0F);
      bean.setCameraFar(17000.0F);
      bean.setCameraField(83.973999F);
      bean.setCameraFocalLength(FocalLength.FOCAL_LENGTH_20);
      beans.add(bean);

      bean = new ModelViewBean();
      bean.setDemoName("demo");
      bean.setTieTuType("dm");
      bean.setTieTuCeng(1);
      bean.setProductId(8L);
      bean.setGapType(1);
      bean.setGapCuttingWay("cross");
      bean.setGap(1);
      bean.setGapColor("black");
      bean.setModelPath("plane.g3dj");
      bean.setNodeId("pPlane1");
      bean.setTexturePath("gap_white.png");
      bean.setCameraX(50F);
      bean.setCameraY(50F);
      bean.setCameraZ(50F);
      bean.setCameraTargetX(0F);
      bean.setCameraTargetY(0F);
      bean.setCameraTargetZ(0F);
      bean.setCameraNear(1.0F);
      bean.setCameraFar(17000.0F);
      bean.setCameraField(83.973999F);
      bean.setCameraFocalLength(FocalLength.FOCAL_LENGTH_20);
      beans.add(bean);
    }
    return beans;
  }

  public void close()
  {
  }

  public void del(ModelViewBean bean)
  {
  }
}