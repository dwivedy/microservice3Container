ECHO OFF

ECHO springboot-microservice-three image

REM creating springboot-microservice-three-app image, dot .  cause one d Dockerfile located outside of this file, - t for .tag given name for image
docker build -t springboot-microservice-three-app .

REM run springboot-microservice-three-app image
docker run --rm -p 5658:9969 -d springboot-microservice-three-app

REM for network id
REM docker run --rm -p 5658:9969 -it springboot-microservice-three-app ifconfig
