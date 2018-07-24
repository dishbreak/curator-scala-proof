name := "CuratorFramework"

version := "0.1"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.curator" % "curator-recipes" % "2.10.0"
)

mainClass in (Compile,run) := Some("com.dishbreak.leaderelection.LeaderElector")