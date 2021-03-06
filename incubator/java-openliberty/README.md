# Open Liberty Stack

The Open Liberty stack provides a consistent way of developing microservices based upon the [Jakarta EE](https://jakarta.ee/) and [Eclipse MicroProfile](https://microprofile.io) specifications. This stack lets you use [Maven](https://maven.apache.org) to develop applications for [Open Liberty](https://openliberty.io) runtime, that is running on OpenJDK with container-optimizations in OpenJ9.

The Open Liberty stack uses a parent Maven project object model (POM) to manage dependency versions and provide required capabilities and plugins.

This stack is based on OpenJDK with container-optimizations in OpenJ9 and `Open Liberty v20.0.0.3`. It provides live reloading during development by utilizing the "dev mode" capability in the liberty-maven-plugin.  To see dev mode in action (though not in Appsody) check out this [shorter demo](https://openliberty.io/blog/2019/10/22/liberty-dev-mode.html) and this  [a bit longer demo](https://blog.sebastian-daschner.com/entries/openliberty-plugin-dev-mode).

**Note:** Maven is provided by the Appsody stack container, allowing you to build, test, and debug your Java application without installing Maven locally. However, we recommend installing Maven locally for the best IDE experience.


## Prerequisites

This stack requires the [Open Liberty Operator](https://github.com/OpenLiberty/open-liberty-operator) to be installed in the cluster prior to deploying the stack.

Operator user guide can be viewed [here](https://github.com/OpenLiberty/open-liberty-operator/blob/master/doc/user-guide.md)

Aside from `OpenLibertyApplication` CRD used to deploy the application, Open Liberty Operator provides day-2 operations such as `OpenLibertyDump` and `OpenLibertyTrace`

These additional [day-2 operations](https://github.com/OpenLiberty/open-liberty-operator/blob/master/doc/user-guide.md#day-2-operations) make it easier to collect debug data from the running Open Liberty pods in the Kubernetes cluster.


## Templates

Templates are used to create your local project and start your development. When initializing your project you will be provided with an Open Liberty template application.

The default template provides a `pom.xml` file that references the parent POM defined by the stack and enables Liberty features that support [Eclipse MicroProfile 3.2](https://openliberty.io/docs/ref/feature/#microProfile-3.2.html). Specifically, this template includes:

### Health

The `mpHealth` feature allows services to report their readiness and liveness status - UP if it is ready or alive and DOWN if it is not ready/alive. It publishes two corresponding endpoints to communicate the status of liveness and readiness. A service orchestrator can then use the health statuses to make decisions.

Liveness endpoint: http://localhost:9080/health/live
Readiness endpoint: http://localhost:9080/health/ready

### Metrics

The `mpMetrics` feature enables MicroProfile Metrics support in Open Liberty. Note that this feature requires SSL and the configuration has been provided for you. You can monitor metrics to determine the performance and health of a service. You can also use them to pinpoint issues, collect data for capacity planning, or to decide when to scale a service to run with more or fewer resources.

Metrics endpoint: http://localhost:9080/metrics

#### Metrics Password

Log in as the `admin` user to see both the system and application metrics in a text format.   The password for this `admin` user will be generated by the container.  

To get the generated password for project **my-project**, you can exec in the container like this, for example:

    $ docker exec -it my-project-dev  bash -c "grep keystore /opt/ol/wlp/usr/servers/defaultServer/server.env"

    keystore_password=2r1aquTO3VVUVON7kCDdzno

So in the above example the password value would be: `2r1aquTO3VVUVON7kCDdzno`

### OpenAPI

The `mpOpenAPI` feature provides a set of Java interfaces and programming models that allow Java developers to natively produce OpenAPI v3 documents from their JAX-RS applications. This provides a standard interface for documenting and exposing RESTful APIs.

OpenAPI endpoints:
- http://localhost:9080/openapi (the RESTful APIs of the inventory service)
- http://localhost:9080/openapi/ui (Swagger UI of the deployed APIs)

### Junit 5

The default template uses JUnit 5. You may be used to JUnit 4, but here are some great reasons to make the switch https://developer.ibm.com/dwblog/2017/top-five-reasons-to-use-junit-5-java/


## Getting Started

1. Create a new folder in your local directory and initialize it using the Appsody CLI, e.g.:
    ```bash
    mkdir my-project
    cd my-project
    appsody init java-openliberty
    ```

    This will initialize an Open Liberty project using the default template. This will also install all parent pom dependencies into your local .m2 directory.

1. Once your project has been initialized, you can run your application using the following command:

    ```bash
    appsody run
    ```

    This launches a Docker container that starts your application in the foreground, exposing it on port 9080.

    You can continue to edit the application in your preferred IDE (Eclipse, VSCode or others) and your changes will be reflected in the running container within a few seconds.

1. You should be able to access the following endpoints, as they are exposed by your template application by default:

    - Readiness endpoint: http://localhost:9080/health/ready
    - Liveness endpoint: http://localhost:9080/health/live
    - Metrics endpoint: http://localhost:9080/metrics (login as `admin` user with password obtained as mentioned [here](#Metrics-Password).
    - OpenAPI endpoint: http://localhost:9080/openapi
    - Swagger UI endpoint: http://localhost:9080/openapi/ui

## Appsody local development operations: (run/debug/test )

### RUN
If you launch via `appsody run` then the liberty-maven-plugin will launch dev mode in "hot test" mode, where unit tests and integration tests get automatically re-executed after each detected change.  

You can alternatively launch the container with `appsody run --interactive`, in which case the tests will only execute after you input `<Enter>` from the terminal, allowing you to make a set of application and/or test changes, and only execute the tests by pressing `<Enter>` when ready.
### DEBUG
The `appsody debug` launches the Open Liberty server in debug mode, listening for the debugger on port 7777 (but not waiting, suspended).  Otherwise it allows you to perform the same iteractive, interactive testing as the `appsody run` command.

### TEST
The command `appsody test` launches the Open Liberty server, runs integration tests, and then exists with a "success" or "failure" return code (and message).  If you want to run tests interactively, then just use `appsody run`, since dev mode will run allow you to iteratively test and develop interactively.

## Notes to Windows 10 Users

### Shared Drive and Permission Setup
* See the instructions [here](https://appsody.dev/docs/docker-windows-aad/) for information on setting up "Shared Drives" and permissions to enable mounting the host filesystem from the appsody container.

### Changes from Windows 10 host side not detected within container
* Because of an issue in Docker for Windows 10, changes made from the host side to the application may not be detected by the liberty-maven-plugin dev mode watcher running inside the Appsody Docker container, and thus the normal expected compile, app update, test execution etc. may not run.
* At the time of the release of this java-openliberty stack, this problem seems to be getting the active attention of the Docker Desktop for Windows developement team, (e.g. see [this issue](https://github.com/docker/for-win/issues/5530)). Naturally, updating your Docker Desktop for Windows installation might help, however, we can not simply point to a recommended version that is known to work for all users.   
* **Workaround**: This may be worked around by making the changes from the host, and then doing a `touch` of the corresponding files from within the container.

## Other externals and usage notes

The stack itself defines an externals worth noting here, which is not apparent from the default template:

### POM property **wlp.install.dir**  

This can be used to abstract over the differences between the install location in each of the local development (`appsody run/debug/test`) vs. image build (`appsody build`) scenarios.

### Config dropin: **quick-start-security.xml**

The metrics endpoint is secured with a userid and password enabled through the config dropin included in the default template at path:
**src/main/liberty/config/configDropins/defaults/quick-start-security.xml**.

In order to lock down the production image built via `appsody build` this file is deleted during the Docker build of your application production image.  (The same file would be deleted if you happened to create your own file at this location as well).

## Stack development

The Java Open Liberty stack is fully functional out of the box. However, depending on business needs and requirements, it may become necessary to create a custom stack with additional features and enhancements. This can be done by following the steps outlines here: https://appsody.dev/docs/stacks/develop

Additionally, the Java Open Liberty stack includes a set of custom variables and enforcement rules that can be leveraged when modifying the stack.

### Stack template variables

Custom template variables are defined in the `stack.yaml` file. The Java Open Liberty stack includes the following that are propagated when the stack is packaged.

* **libertyversion** - The version of the Open Liberty runtime to be used for development and production images. Without any other modifications to the Dockerfiles in this project, this version must be a quarterly release (x.0.0.3, x.0.0.6, x.0.0.9, x.0.0.12)

* **parentpomgroup** - The group id used for the parent pom definition and a prefix for the default template group id.
* **parentpomid** - The artifact id used for the parent pom definition
* **parentpomrange** - The version range used within the template project definitions. e.g. [0.2, 0.3). This allows micro version updates to be automatically picked up by users of this stack.

Notes:

1. The stack version must be within the range defined by the 'parentpomrange' variable.

2. When updating the stack version, a new Maven artifact for the parent pom will be built in the stack developers local .m2 directory. Any locally inited Appsody projects will pick up this latest stack. If you want to go back to using a previous parent pom, this latest artifact will either need to be removed from the local .m2 directory (maven-metadata-local.xml and resolver-status.properties files), OR the parent pom version can be hardcoded in the Appsody project instead of the parent pom range.

### Maven Enforcer plugin rules

* **Liberty Maven plugin version** - The version of the Liberty Maven plugin used by the default stack is restricted to the value of the Maven parent pom property *version.liberty-maven-plugin*.

* **Liberty runtime version** - The version of the Liberty runtime is restricted to the value of the *libertyversion* stack template variable defined in `stack.yaml`.

## License

This stack is licensed under the [Apache 2.0](./image/LICENSE) license
