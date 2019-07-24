#!/usr/bin/env bash
kubectl apply -f ./gateway.yaml
kubectl get gateway -n ns-workshop
kubectl apply -f ./destination-rules.yaml
istioctl get destinationrules