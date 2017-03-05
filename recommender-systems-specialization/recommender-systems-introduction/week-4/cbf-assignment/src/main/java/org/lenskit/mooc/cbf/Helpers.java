package org.lenskit.mooc.cbf;

import java.util.HashMap;
import java.util.Map;

public class Helpers {

  public static double log(double x)
  {
    return logE(x);
  }

  public static double logE(double x)
  {
    return Math.log(x);
  }

  public static double log2(double x)
  {
    return log(x,2);
  }

  public static double log10(double x)
  {
    return Math.log10(x);
  }

  private static double log(double x, int base)
  {
    return (Math.log(x) / Math.log(base));
  }

  public static void increment(Map<String, Double> map, String key, Double value) {
    Double curVal = map.get(key);
    if(curVal==null) {
      curVal = value;
    } else {
      curVal = curVal + value;
    }
    map.put(key,curVal);
  }

  public static double magnitude(Map<String, Double> vector) {
    double sum = 0.0;
    for (double e : vector.values()) {
      sum += Math.pow(e,2);
    }
    sum = Math.sqrt(sum);
    return sum;
  }

  public static void normalize(Map<String, Double> vector) {
    double sum = magnitude(vector);
    if(sum>0) {
      for (Map.Entry<String, Double> e : vector.entrySet()) {
	e.setValue(e.getValue()/sum);
      }
    }
  }

  public static void incrementAll(Map<String,Double> profile,Map<String, Double> vector) {
    for (Map.Entry<String, Double> e : vector.entrySet()) {
      increment(profile,e.getKey(),e.getValue());
    }
  }

  public static Map<String, Double> multiplyScalarAll(Map<String, Double> vector, double factor) {
    Map<String, Double> nv = new HashMap<>(vector);
    for (Map.Entry<String, Double> e : nv.entrySet()) {
      e.setValue(e.getValue() * factor);
    }
    return nv;
  }

  private static boolean isZero(double x) {
    double epsilon = 0.0000001;
    if(x<=0.0+epsilon && x>=0.0-epsilon)
      return true;
    return false;
  }

  public static Double cosine(Map<String,Double> userVector,Map<String, Double> itemVector) {
    if(isZero(magnitude(userVector)) || isZero(magnitude(itemVector))) {
      return null;
    }
    Map<String,Double> userVectorNormalized = new HashMap<>(userVector);
    normalize(userVectorNormalized);
    Map<String, Double> itemVectorNormalized = new HashMap<>(itemVector);
    normalize(itemVectorNormalized);

    double similarity = 0.0;
    for (Map.Entry<String, Double> e : userVectorNormalized.entrySet()) {
      if(e.getValue()!=null && itemVectorNormalized.get(e.getKey())!=null) {
	similarity += (e.getValue() * itemVectorNormalized.get(e.getKey()));
      }
    }
    return similarity;
  }
}
