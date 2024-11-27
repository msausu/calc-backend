#! /usr/bin/bash

case ${1:-dev} in
  dev)
    docker build . -f Dockerfile-dev -t calc-dev
    ;;
  local)
    docker build . -f Dockerfile-local -t calc
    ;;
  native)
    docker build . -f Dockerfile-native -t calc
    ;;
  *)
    docker build . -t calc
    ;;
esac
