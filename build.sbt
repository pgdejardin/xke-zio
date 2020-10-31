import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import Dependencies._
import sbtassembly.AssemblyPlugin.autoImport.MergeStrategy.discard

lazy val sharedDependencies = Seq(`logback-classic`, `scala-logging`, zio, `zio-stream`, `zio-interop-cats`)

lazy val compilerOptions: Seq[String] = Seq(
  "-deprecation",                              // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8",                                     // Specify character encoding used by source files.
  "-explaintypes",                             // Explain type errors in more detail.
  "-feature",                                  // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",                    // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",             // Allow macro definition (besides implementation and application). Disabled, as this will significantly change in Scala 3
  "-language:higherKinds",                     // Allow higher-kinded types
  "-language:implicitConversions",             // Allow definition of implicit functions called views. Disabled, as it might be dropped in Scala 3. Instead use extension methods (implemented as implicit class Wrapper(val inner: Foo) extends AnyVal {}
  "-unchecked",                                // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit",                               // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",                          // Fail the compilation if there are any warnings.
  "-Xlint:adapted-args",                       // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant",                           // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select",                 // Selecting member of DelayedInit.
  "-Xlint:doc-detached",                       // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible",                       // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",                          // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator",               // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override",                   // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit",                       // Warn when nullary methods return Unit.
  "-Xlint:option-implicit",                    // Option.apply used implicit view.
  "-Xlint:package-object-classes",             // Class or object defined in package object.
  "-Xlint:poly-implicit-overload",             // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",                     // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",                        // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow",              // A local type parameter shadows a type already in scope.
  "-Xlint:nonlocal-return",                    // A return statement used an exception for flow control.
  "-Xlint:implicit-not-found",                 // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:serial",                             // @SerialVersionUID on traits and non-serializable classes.
  "-Xlint:valpattern",                         // Enable pattern checks in val definitions.
  "-Xlint:eta-zero",                           // Warn on eta-expansion (rather than auto-application) of zero-ary method.
  "-Xlint:eta-sam",                            // Warn on eta-expansion to meet a Java-defined functional interface that is not explicitly annotated with @FunctionalInterface.
  "-Xlint:deprecation",                        // Enable linted deprecations.
  "-Wdead-code",                               // Warn when dead code is identified.
  "-Wextra-implicit",                          // Warn when more than one implicit parameter section is defined.
  "-Wmacros:both",                             // Lints code before and after applying a macro
  "-Wnumeric-widen",                           // Warn when numerics are widened.
  "-Woctal-literal",                           // Warn on obsolete octal syntax.
  "-Wunused:imports",                          // Warn if an import selector is not referenced.
  "-Wunused:patvars",                          // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates",                         // Warn if a private member is unused.
  "-Wunused:locals",                           // Warn if a local definition is unused.
  "-Wunused:explicits",                        // Warn if an explicit parameter is unused.
  "-Wunused:implicits",                        // Warn if an implicit parameter is unused.
  "-Wunused:params",                           // Enable -Wunused:explicits,implicits.
  "-Wunused:linted",
  "-Wvalue-discard",                           // Warn when non-Unit expression results are unused.
  "-Ybackend-parallelism",
  "8",                                         // Enable paralellisation — change to desired number!
  "-Ycache-plugin-class-loader:last-modified", // Enables caching of classloaders for compiler plugins
  "-Ycache-macro-class-loader:last-modified"   // and macro definitions. This can lead to performance improvements.
)

val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")
val buildDate     = LocalDateTime.now().format(dateFormatter)

inThisBuild(
  List(
    scalaVersion := "2.13.3",
    version := s"$buildDate-${git.gitHeadCommit.value.map(_.substring(0, 7)).getOrElse("1.0-SNAPSHOT")}",
    organization := "com.pg.ked",
    scalacOptions ++= compilerOptions,
    scalafmtOnCompile := true,
    libraryDependencies ++= sharedDependencies
  )
)

lazy val domain = project.settings(assemblyJarName in assembly := s"pg-ked-domain-${version.value}.jar", name := "domain")

lazy val infrastructure = project
  .dependsOn(domain)
  .settings(
    assemblyJarName in assembly := s"pg-ked-${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("logback-test.xml") => discard
      case "module-info.class"          => discard
      case x                            => (assemblyMergeStrategy in assembly).value(x)
    },
    name := "infrastructure",
    buildInfoKeys := Seq[BuildInfoKey](BuildInfoKey.constant("service", "pg-ked-api"), version),
    buildInfoPackage := s"${organization.value}.${name.value}.buildinfo",
    libraryDependencies ++= Seq(
      akka,
      `akka-http`,
      `akka-http-json4s`,
      `akka-stream`,
      `cats-core`,
      `doobie-core`,
      `doobie-postgres`,
      `json4s-ext`,
      `json4s-jackson`,
      `typesafe-config`
    )
  )

lazy val root = (project in file("."))
  .aggregate(domain, infrastructure)
  .settings(name := "xke-zio")

Global / onChangedBuildSource := WarnOnSourceChanges
