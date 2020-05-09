#!/bin/bash

docker build -t logbot:latest -f Dockerfile --no-cache .
cmd="docker run --name logbot --env BOT_TOKEN='$1' -v $2:/config logbot:latest"

eval $cmd