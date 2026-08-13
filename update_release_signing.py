import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

old_release_config = """    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }"""

new_release_config = """    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD") ?: "cfipscanner"
      keyAlias = System.getenv("KEY_ALIAS") ?: "upload"
      keyPassword = System.getenv("KEY_PASSWORD") ?: "cfipscanner"
    }"""

content = content.replace(old_release_config, new_release_config)

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
