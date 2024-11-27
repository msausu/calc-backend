#! /usr/bin/bash

RUNNER={2:-docker}-compose

case ${1:-dev} in
  dev) 
    ${RUNNER} -f docker-compose-dev.yaml up
    ;;
  *)
    ${RUNNER} -f docker-compose.yaml up
    ;;
esac
