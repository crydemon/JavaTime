package stock

import org.apache.spark.sql.SparkSession

case class Module(year: Int, rate: Double, dec: Double, cash:Double)

object Zhexian extends App {
  val spark = SparkSession.builder()
    .appName("dataSkew")
    .master("local[*]")
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._
  import org.apache.spark.sql.functions
  val rates = Array(0.2, 0.4, 0.5, 0.6, 0.7,0.8)
  val decs = Array(0.1, 0.07)
  val years = Array.range(2021, 2026)
  val cur = 90
  val tot = for {
      r <- rates
      d <- decs
      y <- years
    }yield  Module(y, r, d, cur * Math.pow(1 + r, y - 2021 + 1) / Math.pow(1 + d, y - 2021 + 1))


  val df = tot.toSeq.toDF("year", "rate", "dec", "cash")
  df.orderBy("rate", "dec", "year").show(300, false)

  df.groupBy("rate", "dec")
    .agg(functions.sum("cash").alias("sum_cash"))
    .withColumn("normal_pe", $"sum_cash"/37.72/2.16)
    .orderBy("rate", "dec")
    .show(100, false)





}
