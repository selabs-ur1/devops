#!/bin/bash

sudo chmod 666 /var/run/docker.sock

gnome-terminal --tab --title="Back" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchBackEnd_devMode.sh'; $SHELL'"
gnome-terminal --tab --title="Front" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchFront.sh'; $SHELL'"
gnome-terminal --tab --title="Comment" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchComment.sh'; $SHELL'"
gnome-terminal --tab --title="Meal" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchMeal.sh'; $SHELL'"
gnome-terminal --tab --title="Carpooling" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchCarpooling.sh'; $SHELL'"
gnome-terminal --tab --title="Chat" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchChat.sh'; $SHELL'"
gnome-terminal --tab --title="Account" --command="bash -c '/bin/sh -ec '~/Documents/gl/doodlestudent-main/scripts/launchAccount.sh'; $SHELL'"
