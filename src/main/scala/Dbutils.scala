import java.sql.{Connection,DriverManager}
object Dbutils {
  val url = "jdbc:mysql://localhost:3306/sparkdata"
  val driver = "com.mysql.jdbc.Driver"
  val username = "rochan"
  val password = "roch@123"
  var connection:Connection = _
  try {
  Class.forName(driver)
  connection = DriverManager.getConnection(url, username, password)
  connection
  } catch {
    case e: Exception => e.printStackTrace
  }
}
