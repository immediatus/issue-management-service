import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    ScalaToolsSnapshots,
    "Typesafe repo"    at "http://repo.typesafe.com/typesafe/releases/",
    "spray repo"       at "http://repo.spray.cc/",
    "scala-tools repo" at "https://oss.sonatype.org/content/groups/scala-tools/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  object V {
    val scala    = "2.9.1"
    val akka     = "2.0.2"
    val spray    = "1.0-M2.2"
    val casbah   = "2.1.5-1"
    val liftJson = "2.5-M1"
    val salat    = "1.9.1"
    val scalaz   = "6.0.4"
    val specs2   = "1.9"
  }

  val akkaActor   = "com.typesafe.akka"   %   "akka-actor"      % V.akka
  val akkaTestKit = "com.typesafe.akka"   %   "akka-testkit"    % V.akka
  val akkaSlf4j   = "com.typesafe.akka"   %   "akka-slf4j"      % V.akka
  val casbah      = "com.mongodb.casbah"  %%  "casbah"          % V.casbah
  val liftJsonExt = "net.liftweb"         %%  "lift-json-ext"   % V.liftJson
  val liftJson    = "net.liftweb"         %%  "lift-json"       % V.liftJson
  val logback     = "ch.qos.logback"      %   "logback-classic" % "1.0.0"
  val salat       = "com.novus"           %%  "salat"           % V.salat
  val scalaz      = "org.scalaz"          %%  "scalaz-core"     % V.scalaz
  val slf4j       = "org.slf4j"           %   "slf4j-api"       % "1.6.4"
  val specs2      = "org.specs2"          %%  "specs2"          % V.specs2
  val sprayBase   = "cc.spray"            %   "spray-base"      % V.spray
  val sprayCan    = "cc.spray"            %   "spray-can"       % V.spray
  val sprayServer = "cc.spray"            %   "spray-server"    % V.spray
}
