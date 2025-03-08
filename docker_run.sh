#!/bin/bash
set -e
set -x
cd /app/run/
screen -dmS minecraft java -Xmx1024M -Xms1024M -jar server.jar nogui || (echo $? && exit 1)
/bin/bash
