#!/bin/bash -x

docker run --rm -ti \
   -p "${ELASTICMQ_PORT:-9324}:9324" \
   --env ELASTICMQ_PORT="${ELASTICMQ_PORT:-9324}" \
   --env ELASTICMQ_HOST="${ELASTICMQ_HOST}" \
   "${@:2}" "meteogroup/sqsmock" ${@:1:1}
