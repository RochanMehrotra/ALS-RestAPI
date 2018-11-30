
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.types._

import scala.collection.mutable



object SparkALS {
  val conf = new SparkConf().setMaster("local[4]").setAppName("KafkaWordCount")
  val spark = SparkSession.builder.config(conf).getOrCreate()
  val ratings = spark.read.format("csv").option("sep", ",").option("inferSchema", "true").option("header", "true").load("/home/rochan/Downloads/ratings.csv").select("userId", "movieId", "rating").repartition(15).repartition(100)

  val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

  val als = new ALS().setMaxIter(5).setRegParam(0.01).setUserCol("userId").setItemCol("movieId").setRatingCol("rating")
  val model = als.fit(ratings)
  model.setColdStartStrategy("drop")


  val predictions = model.transform(test)
  val userRecs = model.recommendForAllUsers(10)
  val tmp = userRecs.select("userId", "recommendations.movieId").rdd.map(row => (row.getInt(0), row.get(1).asInstanceOf[mutable.WrappedArray[Int]].mkString(",")))
  val movieRecs = model.recommendForAllItems(10)
  val schema = StructType(List(StructField("UserId", IntegerType, true), StructField("moviesIds", StringType, true)))

  val prop = new java.util.Properties
  prop.setProperty("driver", "com.mysql.jdbc.Driver")
  prop.setProperty("user", "rochan")
  prop.setProperty("password", "roch@123")
  val url = "jdbc:mysql://localhost:3306/sparkdata"
  val table = "recommend"
  spark.createDataFrame(tmp).write.mode("append").jdbc(url, table, prop)

  println("*******************TASK finished***************")


}
//model.save("/home/rochan/Sparkout/model")
//spark.sparkContext.parallelize(Seq(model),1).saveAsObjectFile("/home/rochan/Sparkout/modelObj")
/*val evaluator = new RegressionEvaluator().setMetricName("rmse").setLabelCol("rating").setPredictionCol("prediction")
 val rmse = evaluator.evaluate(predictions)
 println(s"Root-mean-square error = $rmse")
 */
