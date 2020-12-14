plugins {
  id("org.siouan.frontend-jdk11") version "4.0.1"
}

frontend {
  nodeVersion.set("12.5.0")
  installScript.set("ci")
  assembleScript.set("run build")
  checkScript.set("run lint")
  publishScript.set("run ng build --prod")
}
