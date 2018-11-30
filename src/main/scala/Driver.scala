import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import org.apache.commons.io.IOUtils
import Query._

object Driver{// extends App {
  val server: Undertow = Undertow.builder().addHttpListener(8081, "localhost").setHandler(new HttpHandler() {
    override def handleRequest(exchange: HttpServerExchange): Unit = {
      if(exchange.getRequestMethod.toString.equals("GET")){
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain")
        exchange.getResponseSender().send("Hello")

      }
      else if (exchange.isInIoThread) {
        exchange.dispatch(this)
        return
      }

      exchange.startBlocking
      val afterBlockStartTime:Long=System.currentTimeMillis()
      val userId = new String(IOUtils.toByteArray(exchange.getInputStream))
      val out:String=getRecommendations(userId.toInt)
      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain")
      exchange.getResponseSender().send(out.toString)
      val afterBlockEndTime:Long=System.currentTimeMillis()
      println("Block End-Start= "+(afterBlockEndTime-afterBlockStartTime))
    }
  }).build()

  server.start()
}

