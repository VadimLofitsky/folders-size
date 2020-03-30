 @echo off
rem Steps:
rem 1. (Re)build jar-file of the project
rem 2. If successfully, copy it to docker-compose folder of the project
rem 3. Run app in docker-compose
rem 4. Open web view in browser

del mvn.report>nul 2>nul
echo Rebuilding Jar...

call mvn clean
call mvn package -Dmaven.test.skip=true
rem call mvn package -DskipTests   - also works
if errorlevel 1 (echo Error occured while building jar. Exiting
    goto exit)
echo Done

echo Copying app jar-file into the container...
copy target\folders-size-0.0.1-SNAPSHOT.jar docker-compose\>nul 2>nul
if errorlevel 1 (echo Copying jar failed. Exiting
    goto exit)
echo Done

echo Starting services...
cd docker-compose

echo getting Docker machine IP
for /F %%a in ('docker-machine ip') do (set DOCKER_MACHINE_IP=%%a)
set docker_machine_ip

rem The `docker-compose up` command aggregates the output of each container. When
rem the command exits, all containers are stopped. Running `docker-compose up -d`
rem starts the containers in the background and leaves them running.
rem docker-compose up -d
rem docker-compose run --rm --publish 8080:8080 web-app
docker-compose run --rm --publish 8080:8080 web-app
if errorlevel 1 (echo Error occurred while running docker-compose. Exiting : goto exit)
rem cd ..
rem echo Ready


rem echo Opening view in browser
rem explorer http://%docker_machine_ip%:8080

docker rmi web-app-image>nul

:exit
cd ..
@echo on