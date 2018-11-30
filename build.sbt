
name := "sparkml"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies += "io.undertow" % "undertow-core" % "2.0.14.Final"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25" % Test
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.39"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"
libraryDependencies += "org.apache.maven.plugins" % "maven-shade-plugin" % "2.4.3"
//libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.2"
//libraryDependencies += "org.json4s" %% "json4s-core" % "3.6.2"
//libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.10"
//libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.10"


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter { f =>
    f.data.getName == "json4s-core_2.11:3.5.3"
  }
}
/*

com.fasterxml.jackson.core:jackson-core:2.7.9 is selected over 2.6.7
  [info] 	    +- org.apache.arrow:arrow-vector:0.10.0               (depends on 2.7.9)
[info] 	    +- com.fasterxml.jackson.module:jackson-module-scala_2.11:2.6.7.1 (depends on 2.6.7)
[info] 	    +- com.fasterxml.jackson.core:jackson-databind:2.6.7.1 (depends on 2.6.7)
[info] 	    +- org.apache.spark:spark-kvstore_2.11:2.4.0          (depends on 2.6.7)


com.thoughtworks.paranamer:paranamer:2.8 is selected over 2.7
  [info] 	    +- com.fasterxml.jackson.module:jackson-module-paranamer:2.7.9 (depends on 2.8)
  [info] 	    +- org.json4s:json4s-core_2.11:3.5.3                  (depends on 2.8)
  [info] 	    +- org.apache.avro:avro:1.8.2


com.fasterxml.jackson.core:jackson-annotations:2.6.7 is selected over 2.6.0
  [info] 	    +- com.fasterxml.jackson.module:jackson-module-scala_2.11:2.6.7.1 (depends on 2.6.7)
[info] 	    +- org.apache.spark:spark-network-common_2.11:2.4.0   (depends on 2.6.7)
[info] 	    +- org.apache.spark:spark-kvstore_2.11:2.4.0          (depends on 2.6.7)
[info] 	    +- com.fasterxml.jackson.core:jackson-databind:2.6.7.1 (depends on 2.6.0)
[info] 	* com.fasterxml.jackson.core:jackson-databind:2.6.7.1 is selected over {2.6.7, 2.4.2}
  [info] 	    +- org.apache.spark:spark-network-common_2.11:2.4.0   (depends on 2.6.7.1)
[info] 	    +- org.apache.spark:spark-core_2.11:2.4.0             (depends on 2.6.7.1)
[info] 	    +- org.apache.spark:spark-sql_2.11:2.4.0              (depends on 2.6.7.1)
[info] 	    +- org.apache.spark:spark-kvstore_2.11:2.4.0          (depends on 2.6.7.1)
[info] 	    +- io.dropwizard.metrics:metrics-json:3.1.5           (depends on 2.4.2)
[info] 	    +- com.fasterxml.jackson.module:jackson-module-scala_2.11:2.6.7.1 (depends on 2.6.7)*/
