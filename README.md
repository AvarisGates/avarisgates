# This is a dev README

## Project Structure
### 0. `./`
- `gradle.properties` - contains most software versions
- `build.gradle` - the build script. Sourced from fabric project template, left largely unmodified.
- `src/` - mod source code and content
### 1.  `src/`
This is the folder housing of all the mod's content and functionality.
- `java/com/avaris/avarisgates/` contains the code (behaviour and logic).
- `resource/` contains everything else - fabric, mixin, access wideners config, content (textures, models, translations).
### 2. `src/java/com/avaris/avarisgates/`
It's split into three subfolders:
- `client/` - Client specific behaviour
- `core/` - Common or server-side only
- `mixin/` - Code modifying the Minecraft source. Using 

## Server Hosting
- Inside the project root directory run `sudo docker compose up -d` to start a docker container service
- To browse files within the container, run `sudo docker exec -it avarisgates-avarisgates /bin/bash`
- To enter the server console while inside the container, run `screen -r minecraft`
- The prototype server is available at: ***f.com.hr:25569*** <br>
*The current Dockerfile includes the Fabric worldedit mod for the builders* <br>

## References
1. [Fabric](https://fabricmc.net/develop/) - Check current current fabric-loader, fabric-api, loom and yarn versions
2. [Fabric Wiki](https://wiki.fabricmc.net/tutorial:start) - A general knowledge base for fabric