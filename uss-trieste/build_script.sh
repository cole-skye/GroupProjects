#!/bin/bash
# Author: uss-trieste

# Compiles the project
clean_compile_project(){

    mvn clean

    mvn compile
}

package_Project(){

    mvn package -Dmaven.test.skip

}

# Runs the packaged project from outputs against the Acceptance tests as well as unit tests
run_Project_Tests(){

    # Runs a 2x2 server without showing its output
    nohup java -jar libs/robot-worlds-server-1.0.0-jar-with-dependencies.jar -s 2 -o 1,0 >server.out 2>server.err &

    # Runs maven test containing Acceptance tests
    mvn test

    # shellcheck disable=SC2216
    jps | awk '/jar-with-dependencies/{ print $1 }' | fuser -k 7000/tcp

}

run_api_server(){

  java -jar libs/robot-worlds-api-server.jar

}

# shellcheck disable=SC2120
kill_used_containers(){

  for id in $(sudo docker ps -q)
  do
      if [[ $(sudo docker port "${id}") == *"${1}"* ]]; then
          echo "stopping container ${id}"
          sudo docker stop "${id}"
      fi
  done

}

# Call function based on what is string is passed through as an argument
# If no functions are found then it outputs 'ENDED'
case $1 in 
    "clean_compile_project") clean_compile_project;;
    "package_project") package_Project;;
    "project_tests") run_Project_Tests;;
    "run_api_server") run_api_server;;
    "kill_used_containers") kill_used_containers;;
    *) echo "ENDED" ;;
esac
