import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

# Make both debug and release use the release signing config
new_build_types = """  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("release") }
  }"""

# Replace the buildTypes block
content = re.sub(r'  buildTypes \{.*?\n  \}', new_build_types, content, flags=re.DOTALL)

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
