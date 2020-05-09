docker build -t logbot:latest -f Dockerfile --no-cache .
docker run --name logbot --env BOT_TOKEN=%1 -v %2:/config logbot:latest