#!/bin/bash
cd ../..
docker build -t logbot:latest -f ./Docker/Dockerfile --no-cache .
cmd="docker run -e BOT_TOKEN='$1' -v $2:/config logbot:latest"
eval $cmd