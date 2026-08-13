import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

# Fix it back to original state
content = content.replace('debug { signingConfig = signingConfigs.getByName("release") }', 'debug { signingConfig = signingConfigs.getByName("debugConfig") }')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
