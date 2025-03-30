# This is a dev README for Avaris Gates

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
It's split into four subfolders:
- `client/` - Client specific behaviour
- `core/` - General API, config and custom events
- `common/` - Everything related to mod content
- `mixin/` - Code modifying the Minecraft source, using [Mixins](https://github.com/SpongePowered/Mixin/wiki).
             Also see [References](#References).

## Server Hosting
- Inside the project root directory run `sudo docker compose up -d` to start a docker container service
- To browse files within the container, run `sudo docker exec -it avarisgates-avarisgates /bin/bash`
- To enter the server console while inside the container, run `screen -r minecraft`
- The prototype server is available at: ***f.com.hr:25569*** <br>
*The current Dockerfile includes the Fabric worldedit mod for the builders* <br>

## Other Mods Used
### 0. [Fabric API](https://github.com/FabricMC/fabric) 
Remember that it's different from **[Fabric Loader](https://github.com/FabricMC/fabric-loader)**.<br>
Which only loads mods and allows to access the mod list. <br>

#### **[Fabric API](https://github.com/FabricMC/fabric)** itself provides a lot of useful functions and concepts for basic modding.
But it doesn't have a lot of features, so we have to implement more advanced functionality ourselves.

### 1. We use [Town Crier](https://github.com/AvarisGates/TownCrier) for handling events.
It was made to split the event code from actual content, it's developed by our team.<br>
It provides an API for listening to events, it's largely based on previous event handling code in Avaris Gates.

### 2. Compatibility with [Mod Shield](https://github.com/AvarisGates/ModShield), the mod isn't strictly necessary but Avaris Gates is compatible with it.
It provides server admins with information about client mods.<br>
It also provides functionality to disallow certain mods from being used on the server by clients.

## References
1. [Fabric](https://fabricmc.net/develop/) - Check current fabric-loader, fabric-api, loom and yarn versions
2. [Fabric Wiki](https://wiki.fabricmc.net/tutorial:start) - A general knowledge base for fabric. If you want to implement a new feature
                        and don't know where to start this is always a good place.
3. [Fabric Blog](https://fabricmc.net/blog/) - Documents latest fabric and minecraft changes for specific versions.
4. [Fabric Wiki Mixin Tutorial](https://wiki.fabricmc.net/tutorial:mixin_introduction) - A good place to start with [Mixins](https://github.com/SpongePowered/Mixin/wiki).