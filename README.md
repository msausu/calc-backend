# NTD Calculator Backend

- see other specific implementation requirements in the [backend](backend/README.md) folder

## Tech Stack

- choose from: Java, Clojure, MySQL, Node.js, Go, Python, Vue.js/React.js, AWS.

- [x] AWS

## Repos

- [x] Frontend and Backend should be on separate stacks.
- [x] Frontend and Backend should be on separate repos.

## Technical requirements

## Containers or Serverless

- [x] may adopt a containerized approach, provided the back-end logic is properly separated into service layers
- [x] include the Docker Compose files.

- the `database` directory contains the container build files for mysql
- the `backend` directory contains the container build files for the backend. There are 3 files depending on the desired build
    - Dockerfile for production
    - Dockerfile-dev for development
    - Dockerfile-local and Dockerfile-native for experimentation
- run `bash build.sh dev` for development and `bash build.sh` for production
- run `bash run.sh dev` for development and `bash run.sh` for production
- since the environment for the free tier is limited the containers and docker-compose files have been constrained accordingly

## Improvements (unspecified need more time)

- [ ] change http to https
- [ ] setup IAM/SSO
- [ ] setup load balancer
- [ ] move from Docker to K8s
