package into_spark_sql

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.spark.sql.SparkSession

import scala.util.Random

object DataSkewTest {
  val path = "E:\\datas\\test\\skew_data\\"

  def prepare = {
    productBadData.writeFile(path)
  }

  def readData = {
    val spark = SparkSession.builder()
      .appName("dataSkew")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val skewDF = spark.read
      .option("header", "false")
      .csv(path)
      .toDF("name", "age", "country", "course")
    skewDF.createOrReplaceTempView("skew_data")
    val skewQuery = spark.sql(
      """
        |select distinct name
        |from skew_data
      """.stripMargin)
    print(skewQuery.queryExecution.analyzed)
    print(skewQuery.queryExecution.optimizedPlan)
    print(skewQuery.queryExecution.executedPlan)
    skewQuery.show(20,false)
    Thread.sleep(1000000)

  }

  def main(args: Array[String]): Unit = {
    //prepare
    readData
  }
}

object productBadData {

  def writeFile(path: String) = {
    val fileName = path + Random.nextInt() + ".csv"
    val skewedData = "wangqiwangqi,1000,china,math\n"
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    (0 to 10000000).foreach(x => {
      bw.append(skewedData)
      bw.flush()
    })

    (0 to 100).foreach(x => {
      val normalData = s"wangqiwangqi${x},1000,china,math\n"
      bw.write(normalData)
      bw.flush()
    })
    bw.flush()
  }
}
