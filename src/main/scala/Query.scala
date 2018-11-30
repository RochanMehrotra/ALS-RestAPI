import Dbutils._
import java.sql._





object Query {
  val connStartTime: Long = System.currentTimeMillis()
  val pStatement = connection.prepareStatement("select _2 from recommend where _1=?")
  connection.setAutoCommit(false)
  val connEndTime:Long=System.currentTimeMillis()
  println("connection took= "+(connEndTime-connStartTime))

  @throws[Exception]
  def getRecommendations(userId: Int): String = {
    val pSStartTime: Long = System.currentTimeMillis()
    pStatement.setInt(1, userId)
    val results: ResultSet = pStatement.executeQuery()
    val pSEndTime: Long = System.currentTimeMillis()
    println("PS and RS took= " + (pSEndTime - pSStartTime))
    while (results.next()) {
      return results.getString(1)
    }
    return "No record Found"

  }
}
