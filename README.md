# Backend developer technical test
[Technical test](https://github.com/dalogax/backendDevTest) developed for MCA by __Alejandro Romero Rivera__. The following considerations have been taken into account in the realization of this test:
  1. The developer must demonstrate knowledge of the Spring Framework.
  2. Knowledge of hexagonal architecture (or ports and adapters) must be demonstrated.
  3. Code cleanliness and maintainability.
  4. Additions that improve the quality of the project.

For these reasons, in the realization of this application decisions have been made that normally would not apply to an exercise of this size, such as:
  1. An architecture based on a hexagonal architecture has been implemented.
  2. Different application profiles have been defined (development, testing and production).
  3. Testing (service unit testing and controller testing)  .
  4. Customized error management.
  5. Use of WebClient and Reactive API (Reactor) in repositories and services.
  6. The project has been dockerized, both the compilation and the deployment of the server.

## Project structure
The project structure is divided into the following packages:
  - `service`: Package that contains input interfaces (or ports) to the application core (or business logic) and implementations of that interfaces.
    These interfaces define the _API_ to call the business logic and each one has its implementation, so forming the business logic.
    Under this package, other packages are created to form groups of services with logical meaning, for example the package `products` groups services that operates on products like `SimilarProductsQueryService`.
    - `internal`: A service can be formed by parts of common logic that can be shared with other services. To allow re-usage of that logic, this package composed of internal services has been defined. Internal services hold common logic reusable between services.
  - `repository`: This package contains data sources defined by means of interfaces (or ports) and implementations (or adapters) of that data sources.
  - `web`: The web package contains classes related to the web part of the application. 
    - `controller`: Contains the controllers that define the _API REST_. 
    - `error`: Contains the web common error management of the application.

Note that a similar structure is formed under `test` package.

### Profiles
The different profiles that have been defined for the application are:
- `development`: Profile whose purpose is to be used during the development of the project.
  In this profile _CORS_ requests are enabled for all domains, this allows to test the _API REST_ from a web browser (for example with _Swagger_).
- `testing`: Profile used during unit tests execution. Its main feature is the use of a mock adapter for the product repository.
- `production`: Final environment, without development or testing flags. 

## Getting Started (With Docker)

### Prerequisites
  - [Docker](https://www.docker.com/), used to build and execute the server that exposes the new _API REST_.
#### Example of installation in Arch Linux:
```shell
# Installs it
sudo pacman -S docker docker-compose

# Starts the required docker service
sudo systemctl start docker.service
```

### Usage
To start the service type:
```shell
docker compose up
```
This command will start the server executing the `jar` under `/app` folder. Note that, 
in the first execution, the docker service will compile the code using maven, 
and will move the generated `jar` file to the folder `/app` of the container.

### Configuration
To configure the service, edit the `docker-compose.yaml` file before starting the server. Configurable properties are:
1. `SPRING_ACTIVE_PROFILE`, defaults to `development` profile. See [profiles](#profiles) section for more information. 
2. `SERVER_PORT`, defaults to port `5000`
3. `PRODUCTS_REPOSITORY_HOST`, the given product's server _API REST_ host name, defaults to `localhost`
4. `PRODUCTS_REPOSITORY_PORT`, the given product's server _API REST_ defaults to `3001`

Note: The default `network_mode` is `host`, that means that the server will form part of the host network.

## License
Distributed under the MIT License. See `LICENSE` for more information.