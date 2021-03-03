import org.apache.spark.sql.SparkSession

import org.apache.spark.sql.Column

case class GoodBoy(grade: Int, rank: Int)

object LiXin extends App {
  val path = "C:\\Users\\wqkant\\Downloads\\total.csv"
  val spark = SparkSession.builder()
    .appName("liLin")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  spark.sparkContext.setLogLevel("WARN")
  val totalDf = spark.read
    .option("header", "true")
    .option("encoding", "gbk")
    .csv(path)
  totalDf.show(20)

  val cols = Array("语文", "数学", "英语", "物理", "化学", "生物", "政治", "历史", "地理", "总分")
  println(cols.length)
  var result = Seq.empty[GoodBoy].toDF()
  totalDf.columns.foreach(println)
  cols.map(_ + "年级排名").foreach(x => {
    val tmp = totalDf.select("班号", x)
      .withColumnRenamed(x, "rank")
      .withColumnRenamed("班号", "grade")
      .filter($"rank" <= 650)
      .groupBy("grade")
      .count()
      .withColumnRenamed("count", x + "前650")
    if (result.count() == 0) {
      result = tmp
    } else {
      result = result.join(tmp, "grade")
    }

  })
  result
    .orderBy($"grade".cast("int"))
    .repartition(1)
    .write
    .option("header", "true")
    .option("delimiter", ",")
    .option("encoding", "gbk")
    .csv("C:\\Users\\wqkant\\Downloads\\result")

}
