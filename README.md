# Backend developer technical test
TODO:

## Project structure
TODO: 
  - `service`: 
    - `internal`: 
  - `repository`: 
    - `impl`: 
  - `web`: 
    - `controller`: 
    - `error`: 

### Profiles
The different profiles that have been defined for the application are:
- `development`: Profile whose purpose is to be used during the development of the project.
  In this profile _CORS_ requests are enabled for all domains, this allows to test the _API REST_ from a web browser (for example with _Swagger_).
- `testing`: Profile used during unit tests execution. Its main feature is the use of a mock adapter for the product repository.
- `production`: Final environment, without development or testing flags. 

## Getting Started

### Prerequisites
  - [Docker](https://www.docker.com/), used to build and execute the server that exposes the new _API REST_.
#### Example of installation in Arch Linux:
```shell
# Installs it
sudo pacman -S docker

# Starts the required docker service
sudo systemctl start docker.service
```   

### Usage
To start the service type:
```shell
docker-compose up
```
This command will start the server. In the first execution, 
the docker service will compile the code using maven, 
and will move the generated `jar` file to the folder `/app` of the container.

### Configuration
To configure the service edit `docker-compose.yaml` file before start the server. Configurable properties are:
1. `SPRING_ACTIVE_PROFILE`, defaults to `development` profile. See [profiles](#profiles) section for more information. 
2. `SERVER_PORT`, defaults to port `5000`
3. `PRODUCTS_REPOSITORY_HOST`, the given products server _API REST_ host name, defaults to `localhost`
4. `PRODUCTS_REPOSITORY_PORT`, the given products server _API REST_ defaults to `3001`

Note: The default `network_mode` is `host`, that means that the server will form part of the host network.

## License
Distributed under the MIT License. See `LICENSE` for more information.