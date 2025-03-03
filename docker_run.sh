#!/bin/bash
set -e
set -x

screen -dmS minecraft java -Xmx1024M -Xms1024M -jar server.jar nogui
/bin/bash
