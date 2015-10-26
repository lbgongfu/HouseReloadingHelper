package cn.lbgongfu.housereloadinghelper.models;

import java.util.Arrays;
import java.util.List;

public enum FocalLength
{
  FOCAL_LENGTH_15(15.0F, 100.389F), 
  FOCAL_LENGTH_20(20.0F, 83.973999F), 
  FOCAL_LENGTH_24(24.0F, 73.739998F), 
  FOCAL_LENGTH_28(28.0F, 65.470001F), 
  FOCAL_LENGTH_35(35.0F, 54.431999F), 
  FOCAL_LENGTH_50(50.0F, 39.598F), 
  FOCAL_LENGTH_85(85.0F, 23.913F), 
  FOCAL_LENGTH_135(135.0F, 15.189F), 
  FOCAL_LENGTH_200(200.0F, 10.286F), 

  FOCAL_LENGTH_16(16.0F, 96.733002F), 
  FOCAL_LENGTH_20_068(20.068001F, 83.780998F), 
  FOCAL_LENGTH_22_469(22.469F, 77.397003F), 
  FOCAL_LENGTH_22(22.0F, 78.579002F), 
  FOCAL_LENGTH_24_49(24.49F, 72.630997F), 
  FOCAL_LENGTH_24_165(24.165001F, 73.362999F), 
  FOCAL_LENGTH_24_527(24.527F, 72.549004F), 
  FOCAL_LENGTH_25(25.0F, 71.508003F), 
  FOCAL_LENGTH_26(26.0F, 69.389999F), 
  FOCAL_LENGTH_43_456(43.456001F, 45.0F);

  private static final int OFFSET = 3;
  private float length;
  private float field;

  private FocalLength(float length, float field) { this.length = length;
    this.field = field; }

  public float getLength()
  {
    return this.length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public float getField() {
    return this.field;
  }

  public void setField(float field) {
    this.field = field;
  }

  public FocalLength toGdxFocalLength()
  {
    List lengths = Arrays.asList(values());
    int index = lengths.indexOf(this);
    if (index + 3 > values().length - 1) return this;
    return (FocalLength)lengths.get(index + 3);
  }
}