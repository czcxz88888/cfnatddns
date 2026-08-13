import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

# Revert the release signing config
content = content.replace('signingConfig = signingConfigs.getByName("debugConfig")', 'signingConfig = signingConfigs.getByName("release")')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
