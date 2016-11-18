# !/bin/bash

node RepresentServer/bin/www &

cd RepresentClient/app

python -m SimpleHTTPServer 3001 &


echo "Ready"
URL='http://localhost:3001'
[[ -x $BROWSER ]] && exec "$BROWSER" "$URL"
path=$(which xdg-open || which gnome-open) && exec "$path" "$URL"
echo "Can't find browser"
