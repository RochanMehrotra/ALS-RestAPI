import io.undertow.Undertow
import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.Headers
import org.apache.commons.io.IOUtils
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable._
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.SparkSession

import scala.collection.mutable
object RestML extends App {
  val conf = new SparkConf().setMaster("local[4]").setAppName("recommendation")
  val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("Recommendations"))
  val spark = SparkSession.builder.config(conf).getOrCreate()
  case class User(userId: Int)
  val userList = mutable.MutableList[User]()
  val model1 = ALSModel.load("/home/rochan/Sparkout/model")
  val server = Undertow.builder.addHttpListener(8081, "localhost").setHandler(new HttpHandler() {
    @throws[Exception]
    override def handleRequest(exchange: HttpServerExchange): Unit = {
      if (exchange.getRequestMethod.toString == "GET") {
        exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
        exchange.getResponseSender.send("welcome")
      }
      else {
        if (exchange.isInIoThread) {
          exchange.dispatch(this)
        }else try {
          exchange.startBlocking
          val userId = new String(IOUtils.toByteArray(exchange.getInputStream))
          //val model=spark.sparkContext.objectFile[org.apache.spark.ml.recommendation.ALSModel]("/home/rochan/Sparkout/modelObj").first()

          println("String:  " + userId + "Int:  " + userId)
          val param = User(userId.toInt)
          userList += param
          val params = spark.sqlContext.createDataFrame(userList)
          val queryTime = System.currentTimeMillis()
          val recommendations = model1.recommendForUserSubset(params, 10)
            .select("userId", "recommendations.movieId").rdd
            .map(row => (row.get(1).asInstanceOf[WrappedArray[Int]].mkString(",")))
            .collect().mkString(",")
          val totalTime = System.currentTimeMillis()-queryTime
          println(recommendations)


          exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
          exchange.getResponseSender.send(totalTime+" Recommended movies for userID" + userId + "are: " + recommendations)
        }catch {
          case e:NumberFormatException => {
            exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
            exchange.getResponseSender.send("Number format exception")
          }
          case e:NumberFormatException => {
            e.printStackTrace()
            exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
            exchange.getResponseSender.send("Server Error")
          }
        }
      }
    }
  }).build
  server.start()


}
