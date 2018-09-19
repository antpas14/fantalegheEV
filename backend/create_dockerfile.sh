#!/bin/bash

TARGET_PATH=target
create_dockerfile() {
	ms_name=$1
	version=$2
	cat <<- EOF > Dockerfile
FROM openjdk:8-alpine3.8
VOLUME /tmp

ADD $TARGET_PATH/${ms_name}-${version}.jar app.jar
ENTRYPOINT ["java", "-Dspring.application.name=$ms_name", "-jar", "/app.jar"]
RUN sh -c 'touch /app.jar'
EOF
}

main() {
	create_dockerfile $@
}

main $@
