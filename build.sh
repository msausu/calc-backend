#! /usr/bin/bash

pushd database
bash build-container.sh
popd

pushd backend
bash build-container.sh ${1:-dev}
popd
