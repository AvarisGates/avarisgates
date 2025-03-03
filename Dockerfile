# Base image: Ubuntu latest
FROM ubuntu:latest

# Set environment variables to avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Update and install necessary dependencies
RUN apt update && apt install -y \
    curl \
    unzip \
    wget \
    git \
    screen \
    openjdk-21-jdk \
    && rm -rf /var/lib/apt/lists/*

# Install Gradle 8.12.1 manually
WORKDIR /opt
RUN wget https://services.gradle.org/distributions/gradle-8.12.1-bin.zip \
    && unzip gradle-8.12.1-bin.zip \
    && rm gradle-8.12.1-bin.zip \
    && ln -s /opt/gradle-8.12.1/bin/gradle /usr/bin/gradle

# Set Gradle and Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$PATH:/opt/gradle-8.12.1/bin

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project from the correct build context
COPY . .

RUN chmod +x /app/docker_run.sh

RUN pwd
RUN ls
RUN ls ..

# Ensure the Gradle Wrapper uses the correct Gradle version
RUN echo "distributionUrl=https\://services.gradle.org/distributions/gradle-8.12.1-bin.zip" > gradle/wrapper/gradle-wrapper.properties

# Grant execution permission to Gradle Wrapper
RUN chmod +x gradlew

# **Run Gradle Build with Debugging**
RUN ./gradlew build --stacktrace --info --no-daemon

# **Check if JAR file exists before copying**
RUN ls -l /app/build/libs/ || (echo "No JAR file found!" && exit 1)

# **Copy the built JAR file into the run directory**
RUN mkdir -p /app/run/mods && cp /app/build/libs/*.jar /app/run/mods
RUN curl https://piston-data.mojang.com/v1/objects/4707d00eb834b446575d89a61a11b5d548d8c001/server.jar --output server.jar
RUN echo "eula=true">eula.txt

# Expose the Minecraft server port
EXPOSE 25565

# Set the entrypoint to run the Minecraft server with the mod
CMD ["sh", "docker_run.sh"]
