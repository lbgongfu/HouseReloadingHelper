package cn.lbgongfu.housereloadinghelper.models;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="bbibm_chutu")
public class ModelViewBean
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private int flag;
  private String demoName;
  private int tieTuCeng;
  private long productId;

  /** @deprecated */
  private short modelType;

  /** @deprecated */
  private String tieTuType;
  private String modelPath = "";
  private String nodeId = "";
  private float cameraX;
  private float cameraY;
  private float cameraZ;
  private float cameraField;
  private FocalLength cameraFocalLength;
  private float cameraNear;
  private float cameraFar;
  private float cameraTargetX;
  private float cameraTargetY;
  private float cameraTargetZ;
  private String texturePath = "";
  private int gap = 1;
  private String gapColor;
  private int gapType;
  private String gapCuttingWay;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy="increment")
  public long getId()
  {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name="flag")
  public int getFlag() {
    return this.flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  @Column(name="demoname")
  public String getDemoName() {
    return this.demoName;
  }

  public void setDemoName(String demoName) {
    this.demoName = demoName;
  }

  @Column(name="tietuceng")
  public int getTieTuCeng() {
    return this.tieTuCeng;
  }

  public void setTieTuCeng(int tieTuCeng) {
    this.tieTuCeng = tieTuCeng;
  }

  @Column(name="productid")
  public long getProductId() {
    return this.productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  /** @deprecated */
  @Transient
  public short getModelType()
  {
    return this.modelType;
  }

  /** @deprecated */
  public void setModelType(short modelType)
  {
    this.modelType = modelType;
  }

  /** @deprecated */
  @Transient
  public String getTieTuType()
  {
    return this.tieTuType;
  }

  /** @deprecated */
  public void setTieTuType(String tieTuType)
  {
    this.tieTuType = tieTuType;
  }

  @Column(name="modelPath")
  public String getModelPath() {
    return this.modelPath;
  }

  @Column(name="nodeId")
  public String getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
  }

  @Column(name="cameraX")
  public float getCameraX() {
    return this.cameraX;
  }

  public void setCameraX(float cameraX) {
    this.cameraX = cameraX;
  }

  @Column(name="cameraY")
  public float getCameraY() {
    return this.cameraY;
  }

  public void setCameraY(float cameraY) {
    this.cameraY = cameraY;
  }

  @Column(name="cameraZ")
  public float getCameraZ() {
    return this.cameraZ;
  }

  public void setCameraZ(float cameraZ) {
    this.cameraZ = cameraZ;
  }

  @Column(name="cameraField")
  public float getCameraField() {
    return this.cameraField;
  }

  public void setCameraField(float cameraField) {
    this.cameraField = cameraField;
  }
  @Enumerated(EnumType.STRING)
  @Column(name="cameraFocalLength")
  public FocalLength getCameraFocalLength() {
    return this.cameraFocalLength;
  }

  public void setCameraFocalLength(FocalLength focalLength) {
    this.cameraFocalLength = focalLength;
  }

  @Column(name="cameraNear")
  public float getCameraNear() {
    return this.cameraNear;
  }

  public void setCameraNear(float cameraNear) {
    this.cameraNear = cameraNear;
  }

  @Column(name="cameraFar")
  public float getCameraFar() {
    return this.cameraFar;
  }

  public void setCameraFar(float cameraFar) {
    this.cameraFar = cameraFar;
  }

  @Column(name="cameraTargetX")
  public float getCameraTargetX() {
    return this.cameraTargetX;
  }

  public void setCameraTargetX(float cameraTargetX) {
    this.cameraTargetX = cameraTargetX;
  }

  @Column(name="cameraTargetY")
  public float getCameraTargetY() {
    return this.cameraTargetY;
  }

  public void setCameraTargetY(float cameraTargetY) {
    this.cameraTargetY = cameraTargetY;
  }

  @Column(name="cameraTargetZ")
  public float getCameraTargetZ() {
    return this.cameraTargetZ;
  }

  public void setCameraTargetZ(float cameraTargetZ) {
    this.cameraTargetZ = cameraTargetZ;
  }

  @Column(name="texturePath")
  public String getTexturePath() {
    return this.texturePath;
  }

  public void setTexturePath(String texturePath) {
    this.texturePath = texturePath;
  }

  @Column(name="xiankuan")
  public int getGap() {
    return this.gap;
  }

  public void setGap(int gap) {
    this.gap = gap;
  }

  @Column(name="xiancolor")
  public String getGapColor() {
    return this.gapColor;
  }

  public void setGapColor(String gapColor) {
    this.gapColor = gapColor;
  }

  @Column(name="xiantype")
  public int getGapType() {
    return this.gapType;
  }

  public void setGapType(int gapType) {
    this.gapType = gapType;
  }

  @Column(name="xianqie")
  public String getGapCuttingWay() {
    return this.gapCuttingWay;
  }

  public void setGapCuttingWay(String gapCuttingWay) {
    this.gapCuttingWay = gapCuttingWay;
  }

  public String toString()
  {
    return String.format("ModelViewBean [id=%d, modelPath=%s, texturePath=%s]", new Object[] { Long.valueOf(this.id), this.modelPath, this.texturePath });
  }
}