# water-collection-service
A service for submitting and retrieving water collection samples

## Background
Qualified service personnel will take a water collection from a source and submit the collection to a lab for analysis and measurement. The laboratory will analyze the water collection and perform measurements with the latest measurement type available. The measurements will be added to the water collection when the measurements are complete. Chemists can retrieve the water collection and measurement data for reporting. The chemist will eventually be able to specify an area and all water collections taken within that area will be returned.
The collection will be taken manually at first, followed by an appliance carried by the service personnel. A source in this case is one of the following:
* A well (unfiltered)
* A water faucet (filtered)
* A lake
* A pond
* A puddle

## Epic
**Description**: As a chemist, I want to:

* Allow field personnel to submit water samples.
* Allow laboratory technicians measure water samples for various compounds.
* Allow chemists to retrieve water collections taken in the field and review measurements
* Allow chemists to run reports by various water collection attributes, including measurements, to see compounds
in the water.

**Acceptance Criteria**:
1. A water collection resource represents the following:
    * Collection id (uuid; no dashes; Set by service) {read only}
    * Station id (uuid; no dashes; Could be personnel id or IoT id; Set by Reporter)
    * Collection time (ISO 8601; UTC; Set by Reporter)
    * Collection version (10 digit string; leading zeros okay; Set by Reporter)
    * Collection type (String; Set by Reporter)
    * Collection content (water or gas TDB) {String; Set by Reporter)
    * Longitude (in degrees; Set by Reporter)
    * Latitude (in degrees; Set by Reporter)
    * Quantity (in cubic centimeters; Set by Reporter)
    * Processing state (fixed set of strings; NOT_STARTED (initial state), IN_PROGRESS, DONE; Set by Server) {read only}
    * Measurement version (10 digit string; leading zeros okay; Set by Server) {read only}
    * Measurement type (description. e.g. Volumetric Gas; Set by Server) {read only}
    * Compound percentages (complex object) {read only}
        * VOC Percentage (percentage) {read only}
        * Pesticide Percentage (percentage) {read only}
        * Metal Percentage (percentage) {read only}
        * Bacteria Percentage (percentage) {read only}
        * TDB
2. The chemist can get the water collections for a given station id
3. The chemist can add a water collection
4. The service is accessed using the URI `https:/<server name>/water-collections/v1/water-collections`
5. The service is secured thru W3 basic authentication for now. The service will be subsequently secured by TLS mutual authentication to reduce nefarious use.
6. Resource is represented as JSON
7. Resource is versioned thru a URI path element
8. URI query parameters containing restricted data will be encoded using an encryption scheme common to the reporter (e.g. client) and server. (TBD)
9. URI can take the form of:
    * `https://<server-name>/water-collections/v1/water-collections?station_id=<station id>` which return a page of water collection resources for the given station id and the latest measurement version, 100 per page.
    * `https://<server-name>/water-collections/v1/water-collections?station_id=<station id>&measurement_version=<measurement version>` which return a page of water collections for the given station id and measurement version, 100 per page
    * `https://<server-name>/water-collections/v1/water-collections?collection_id=<collection id>` which returns the water collection resource

## Notes:
* A story breakdown is needed for the following:
    * ~~Resource schema creation (including regular expressions)~~ **Done**
    * ~~Service creation (framework, routing, etc)~~ **Done**
    * ~~HTTP Method (e.g. POST and GET)~~ **Done**
        * Perhaps a story per URI?
    * ~~Resource version support~~ **Done**
    * Velocity checks
    * ~~TLS mutual authentication changes~~ (**Not needed. basic auth over https is sufficient**)
    * Measurement type or version change
* No PUT, PATCH or DELETE from the public API. In other words, no update or delete. Collections are facts and will only be created and read but the API.
* Initially, the collection will be done by manually qualified service personnel. Eventually, the collection with be performed by the technician using an appliance.
* [Question] ~~Should the resource use collections or samples to avoid confusion with a list of resources AKA a collection?~~ **Answer: collections**

## Behavioral Considerations:
* The service will support HTTP requests with headers and a body.
* The service can be accessed via HTTPS (or HTTP during development).
* The service will periodically (**TBD**) run collections thru the latest measurement method. The following behaviors apply:
    * A new collection will be created when a new measurement method is used. It will update:
        * Collection id
        * Processing state
        * Measurement version
        * Measurement type
        * All compound percentages

## Performance Considerations:
* A collection POST will return in under 100ms
* A collection GET will return in under 100ms

## Security Considerations:
* The service will be secured with W3 Basic Authentication
* ~~The service will be secured with TLS mutual authentication in subsequent versions~~ (**not needed**)
* The service will validate station id and drop the request if not valid
* The service will drop requests from the same station id within 5 seconds if not valid

## Availability Considerations:
* The service will be available 99.999% of the time. This translates to ~30 seconds per year.

## Maintainability Considerations:
* The resource will be versioned by a URL path attribute.
* The service will allow any supported service version (e.g. v1, v2, etc).
* ~~The service will maintain a canonical representation of the resource.~~ **The canonical model is the resource model**
* The resource will be represented internally by a Java DTO.

## Serviceability Considerations:
* The service will log all requests
* The service will log request metrics

## Online API documentation
**http://localhost:8080/water-collection-service/swagger-ui.html**

## Deployment details

The deployment strategy is to create a Docker image of the service in a Java container. The Docker image will
automatically be created when you run `mvn clean install`. The image will be created with the repository
`gcr.io/wcs-195520/wcs` along with a version specified by the POM.

### Google Cloud Configuration

Google Cloud Platform is the current cloud provider for the service.

A project `water-collection-service` with project id `wcs-195520` already exists which can be used to create compute
instances or for creating a cluster using `kubernetes`. Googles' container optimized images with Docker support must
be used when creating instances.

* Set current project: `gcloud config set project wcs-195520`
* Push docker image to google repository: `gcloud docker -- push gcr.io/wcs-195520/wcs:<version>`

### Releases
A release build will use the `secure` Spring profile (i.e.`-Dspring.profiles=secure`) to expose the service 
via `https` on port 8443. Port 8443 will be accessible on the compute instance using firewall rules.
Use `gcloud compute firewall-rules list` to view the set of firewall rules.

#### Basic Google Compute Engine steps
* Create instance:
```
gcloud beta compute instances create-with-container wcs-compute-1 \
--container-image gcr.io/wcs-195520/wcs:<version> \
--machine-type=n1-standard-1 \
--no-restart-on-failure \
--tags=http-server,https-server
```
* Set the `JASYPT_ENCRYPTOR_PASSWORD` environment variable for the VM instance using the plaintext master password.
Use the VM instance UI to do so by editing the container properties,
* Get external IP address for testing: `gcloud compute addresses list`
* Delete instance: `gcloud compute instances delete wcs-compute-1`

#### Kubernetes steps

* Create kubernetes cluster: `gcloud container clusters create wcs-cluster --num-nodes=1`
* Set current cluster: `gcloud config set container/cluster wcs-cluster`
* Set Kubernetes credentials: `gcloud container clusters get-credentials wcs-cluster`
* Deploy service using kubernetes: `kubectl run wcs-server --image gcr.io/wcs-195520/wcs:<version> --port 8443`
* Expose service: `kubectl expose deployment wcs-server --type=LoadBalancer --port 443 --target-port 8443`
* Get service details for external IP to connect over the internet: `kubectl get service`

##### View service logs

* Run `kubectl get pods` to get the pod name
* Run `kubectl logs <pod name>`

##### Delete kubernetes service then cluster

* Run `kubectl delete service wcs-server`
* Run `gcloud container clusters delete wcs-cluster`

##### Terraform steps [**Not Working**]

Terraform can be used to setup the project and compute instances

You must run `terraform init` first to ensure the appropriate plugins are installed

The following command(s) can be used to create the project and VM instance.
```
> terraform plan
> terraform apply
```

## Security
The basic authentication mechanism relies on a master password to encrypt/decrypt values in the configuration. 
The plaintext master password is not stored anywhere in source code control. The master password must be derived at runtime
and supplied to the service to decrypt the relevant properties. Since GCP is currently the cloud provider of choice, the
Google KMS service will be used to store and retrieve the master password key for use by the water collection service.
To use the KMS service:
* Create a global keyring: `gcloud kms keyrings create master-password-keyring --location global`
* Create a key in the keyring: `gcloud kms keys create master-password --location global --keyring master-password-keyring --purpose encryption`
* Disable rotation (for now) on the key: `gcloud kms keys update master-password --remove-rotation-schedule --location global --keyring master-password-keyring`
* List the keys: `gcloud kms keys list --location global --keyring master-password-keyring`

### Encrypt/Decrypt

* Prepare plaintext file: `echo -n "master password to be encrypted" > master-password-plaintext-file`
* Encrypt: `gcloud kms encrypt --location=global --keyring=master-password-keyring --key=master-password --plaintext-file=master-password-plaintext-file --ciphertext-file=master-password-ciphertext-file.enc`
* Decrypt (to terminal): `gcloud kms decrypt --location=global --keyring=master-password-keyring --key=master-password --ciphertext-file=master-password-ciphertext-file.enc --plaintext-file=-`

### Update credentials
If you need to re-generate a master password,

1. Regenerate the encrypted properties in `credentials.yml` and `application-secure.yml`
2. Update the `JASYPT_ENCRYPTOR_PASSWORD` environment variable of Google VM instances with the plaintext master password

## Running the docker container locally
`docker run -it -p 8443:8443 --rm --env JASYPT_ENCRYPTOR_PASSWORD=$JASYPT_ENCRYPTOR_PASSWORD gcr.io/wcs-195520/wcs:<version>`
