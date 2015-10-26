package cn.lbgongfu.housereloadinghelper;

import java.util.HashMap;
import java.util.Map;

import cn.lbgongfu.housereloadinghelper.models.FocalLength;

public class Constants
{
  public static final Map<Float, Float> MAX_GDX_FIELD_MAP = new HashMap();

  static
  {
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_15.getLength()), Float.valueOf(84.065903F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_20.getLength()), Float.valueOf(68.2715F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_24.getLength()), Float.valueOf(58.992001F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_28.getLength()), Float.valueOf(51.722198F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_35.getLength()), Float.valueOf(42.2379F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_50.getLength()), Float.valueOf(30.225201F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_85.getLength()), Float.valueOf(18.0299F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_135.getLength()), Float.valueOf(11.4263F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_200.getLength()), Float.valueOf(7.7164F));

    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_16.getLength()), Float.valueOf(81.977127F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_20_068.getLength()), Float.valueOf(69.240494F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_22_469.getLength()), Float.valueOf(62.416939F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_22.getLength()), Float.valueOf(64.409019F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_24_49.getLength()), Float.valueOf(59.049591F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_24_165.getLength()), Float.valueOf(59.644714F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_24_527.getLength()), Float.valueOf(58.982929F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_25.getLength()), Float.valueOf(58.136585F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_26.getLength()), Float.valueOf(56.414631F));
    MAX_GDX_FIELD_MAP.put(Float.valueOf(FocalLength.FOCAL_LENGTH_43_456.getLength()), Float.valueOf(35.15625F));
  }
}