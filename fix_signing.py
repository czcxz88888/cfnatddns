import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

# Replace the release signing config to use debugConfig
content = content.replace('signingConfig = signingConfigs.getByName("release")', 'signingConfig = signingConfigs.getByName("debugConfig")')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
