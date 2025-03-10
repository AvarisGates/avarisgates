# This is a dev README

## Project Structure
### 0.
- `gradle.properties` - contains most software versions
- `build.gradle` - the build script. Sourced from fabric project template, left largely unmodified.
- `src/` - mod source code and content
### 1.  `src/`
This is the folder housing of all the mod's content and functionality.
- `java/com/avaris/avarisgates/` contains the code (behaviour and logic).
- `resource/` contains everything else - fabric, mixin, access wideners config, content (textures, models, translations).
### 2. `java/com/avaris/avarisgates/`
It's split into three subfolders:
- `client/` - Client specific behaviour
- `core/` - Common or server-side only
- `mixin/` - Code modifying the Minecraft source. Using 

## References
[Fabric](https://fabricmc.net/develop/) - Check current current fabric-loader, fabric-api, loom and yarn versions
[Fabric Wiki](https://wiki.fabricmc.net/tutorial:start) - A general knowledge base for fabric