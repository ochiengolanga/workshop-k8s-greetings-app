#!/usr/bin/env bash
kubectl apply -f ./gateway.yaml
kubectl apply -f ./destination-rules.yaml

sleep 5

kubectl get gateway -n ns-workshop
istioctl get destinationrules -n ns-workshop