package stock;

/**
 * @ClassName ZheXian
 * @Description TODO
 * @Author wqkant
 * @Date 2021/1/23 0:42
 * @Version 1.0
 */
public class ZheXian {

  public static void main(String[] args) {
    getResult();
  }

  public static void getResult() {
    int x = 4500;
    double[] r = new double[]{1.15, 1.20, 1.30, 1.40, 1.50, 1.60, 1.70};
    double[] d = new double[]{1.1, 1.07};
    for (double v : r) {
      for (double v1 : d) {
        double sum = 0;
        for (int i = 2020; i < 2030; i++) {
          int p = (i - 2020 + 1);
          double c = x * Math.pow(v, p) / Math.pow(v1, p);
          sum += c;
          System.out.println("年份：" + i + "增长率：" + v + "折现：" + v1 + "现金流贴现：" + c);
        }
        System.out.println("总计：" + sum);
      }
    }

  }

}
