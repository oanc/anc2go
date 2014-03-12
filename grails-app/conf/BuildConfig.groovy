grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
   // inherit Grails' default dependencies
   inherits("global") { // uncomment to disable ehcache
      // excludes 'ehcache'
      excludes 'xml-apis' 
	  excludes "slf4j-log4j12"
	  excludes "grails-plugin-log4j"
   }
   
   log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
   repositories {
      grailsPlugins()
      grailsHome()
      grailsCentral()

      // uncomment the below to enable remote dependency resolution
      // from public Maven repositories
      mavenLocal()
      mavenCentral()
      mavenRepo "http://www.anc.org/maven/release"
      mavenRepo "http://www.anc.org/maven/snapshot"
      mavenRepo "http://www.anc.org:8080/nexus/content/repositories/snapshots"
      mavenRepo "http://www.anc.org:8080/nexus/content/repositories/releases"
      //mavenRepo "http://snapshots.repository.codehaus.org"
      //mavenRepo "http://repository.codehaus.org"
      //mavenRepo "http://download.java.net/maven/2/"
      //mavenRepo "http://repository.jboss.com/maven2/"
   }
   def versions = [
	   //logback:'0.9.18',
	   logback:'1.0.9',
	   slf4j:'1.6.2',
	   tool:'1.0.0-SNAPSHOT',
	   graf:'1.2.0',
	   sag:'1.2.0-SNAPSHOT',
	   common:'3.1.1',
	   conf:'2.0.0' 
	   ]
   
   dependencies {
      // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

      // runtime 'mysql:mysql-connector-java:5.1.13'
      runtime "hsqldb:hsqldb:1.8.0.10"
      compile "javax.mail:mail:1.4"
      compile "org.anc:conf:${versions.conf}"
      compile "org.anc:common:${versions.common}"
      compile "org.anc.tool:tool-api:${versions.tool}"
      compile "org.anc.tool:core:${versions.tool}"
      compile "org.xces.graf.sag:sag-api:${versions.sag}"
      compile "org.xces.graf.sag:sag-impl:${versions.sag}"
      compile "org.tc37sc4.graf:graf-api:${versions.graf}"
      compile "org.tc37sc4.graf:graf-impl:${versions.graf}"
      compile "org.tc37sc4.graf:graf-io:${versions.graf}"
      compile "org.tc37sc4.graf:graf-util:${versions.graf}"
      compile "org.tc37sc4.graf:graf-i18n:${versions.graf}"
	   build "ch.qos.logback:logback-core:${versions.logback}"
	   build "ch.qos.logback:logback-classic:${versions.logback}"
      runtime "ch.qos.logback:logback-core:${versions.logback}" 
	  runtime "ch.qos.logback:logback-classic:${versions.logback}"
	  runtime "org.slf4j:log4j-over-slf4j:${versions.slf4j}"
   }
}

grails.war.resources = { stagingDir ->
	delete(file:"${stagingDir}/WEB-INF/classes/logback-test.xml")
}