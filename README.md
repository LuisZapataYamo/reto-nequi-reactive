# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**


## Installation

Required technologies:

- JDK 21
- Gradle
- Podman or Docker
- AWS CLI
- Terraform

## Build Commands

### 1. Clone the Repository

```bash
    git clone <url_repo>
```

### 2. Build and Tag the Docker Image

> If you're using Podman, replace `docker` with `podman`.
```bash
    docker build -t webflux-api:latest ./deployment/  
    docker tag webflux-api:latest <account_id>.dkr.ecr.us-east-1.amazonaws.com/webflux-api:latest
```
### 3. Create ECR Repository with Terraform

> **Important:** Authenticate with AWS CLI before running these commands.

```bash
    cd terraform/  
    terraform validate  
    terraform plan -refresh=true  
    terraform apply -target=aws_ecr_repository.webflux_api
```
### 4. Push the Docker Image to ECR
```bash
    docker push <account_id>.dkr.ecr.us-east-1.amazonaws.com/webflux-api:latest
```
### 5. Deploy the Remaining Infrastructure
```bash
    terraform apply
```
## Verify Installation

To confirm the deployment:

1. Go to the [AWS Console](https://console.aws.amazon.com/)
2. Navigate to **ECS > Clusters > Services > Tasks**
3. Copy the public IP of the running task
4. Open the following URL in your browser:

http://<public_task_ip>:8080/swagger-ui.html
