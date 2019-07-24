#!/usr/bin/env bash

if [ "$1" != "" ]; then
    command1="kubectl scale --replicas=$1 -n ns-workshop deployment/greetings-v1"
else
    command1="kubectl scale --replicas=1 -n ns-workshop deployment/greetings-v1"
fi

echo $command1
$command1