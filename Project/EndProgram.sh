#!/bin/bash
RED='\033[0;31m'
NC='\033[0m' # No Color
while true; do
    read -p "/!\ WARNING /!\: Running this script will terminate all Node.js and Python processes running on this machine, do you wish to proceed? [Y/n]" yn
    case $yn in
        [Yy]* ) killall -KILL python && killall -KILL node && exit;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done
