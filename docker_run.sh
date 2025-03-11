#!/bin/bash
#set -e
#set -x
cd /app/run/
java -Xmx1024M -Xms1024M -jar server.jar nogui
