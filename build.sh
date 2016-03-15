#!/bin/bash -ex

TIMESTAMP="$(date '+%Y-%m-%d')"

docker build -t "meteogroup/sqsmock" "$(dirname "$0")/docker"
