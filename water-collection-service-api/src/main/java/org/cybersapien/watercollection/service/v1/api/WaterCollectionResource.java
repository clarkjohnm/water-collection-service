package org.cybersapien.watercollection.service.v1.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.cybersapien.watercollection.service.v1.model.WaterCollection;
import org.cybersapien.watercollection.service.v1.model.error.Errors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api
@Path("/v1/water-collections")
public interface WaterCollectionResource {
    /**
     * Create a water collection so it can be further analyzed
     *
     * @param waterCollection A water collection resource containing the client writable properties according to the JSON contract
     * @return the water collection with properties such as id which are set by the service.
     * @throws WebApplicationException if an exception occurred.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Create a water collection resource",
            nickname = "create-water-collection",
            notes = "Create a water collection resource",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = "serviceName", value = "water-collection-service")
                    }),
                    @Extension(name = "slo", properties = {
                            @ExtensionProperty(name = "response_time_95th_percentile", value = "1000"),
                            @ExtensionProperty(name = "error_rate", value = "0")
                    }),
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "water collection resource, where resource was already created", response = WaterCollection.class),
            @ApiResponse(code = 201, message = "Created water collection creation", response = WaterCollection.class),
            @ApiResponse(code = 400, message = "Bad request", response = Errors.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Errors.class),
            @ApiResponse(code = 415, message = "Unsupported media type", response = Errors.class),
            @ApiResponse(code = 500, message = "Internal service error", response = Errors.class)
    })
    WaterCollection postWaterCollection(@ApiParam(name = "waterCollection", value = "The water collection to create", required = true)
                                        WaterCollection waterCollection) throws WebApplicationException;

    /**
     * Get water collections
     *
     * @return a list of water collection instances
     * @throws WebApplicationException if an error occurs during processing
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get a list of water collections",
            nickname = "retrieve-water-collections",
            notes = "By default, the last 1024 submitted water collections are returned",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = "serviceName", value = "water-collection-service")
                    }),
                    @Extension(name = "slo", properties = {
                            @ExtensionProperty(name = "response_time_95th_percentile", value = "1000"),
                            @ExtensionProperty(name = "error_rate", value = "0")
                    }),
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieve response", response = List.class),
            @ApiResponse(code = 400, message = "Bad request", response = Errors.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Errors.class),
            @ApiResponse(code = 404, message = "No results found", response = Errors.class),
            @ApiResponse(code = 415, message = "Unsupported media type", response = Errors.class),
            @ApiResponse(code = 500, message = "Internal service error", response = Errors.class),
    })
    List<WaterCollection> getWaterCollections() throws WebApplicationException;

    /**
     * Get a water collection by id
     *
     * @param id The id of the water collection
     * @return a water collection instance
     * @throws WebApplicationException if an error occurs during processing
     */
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Retrieves a water collection for a given application id",
            nickname = "retrieve-water-collection-by-id",
            notes = "Retrieves a water collection for a given application id",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = "serviceName", value = "water-collection-service")
                    }),
                    @Extension(name = "slo", properties = {
                            @ExtensionProperty(name = "response_time_95th_percentile", value = "1000"),
                            @ExtensionProperty(name = "error_rate", value = "0")
                    }),
            }
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieve response", response = WaterCollection.class),
            @ApiResponse(code = 400, message = "Bad request", response = Errors.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Errors.class),
            @ApiResponse(code = 404, message = "No water collection found", response = Errors.class),
            @ApiResponse(code = 415, message = "Unsupported media type", response = Errors.class),
            @ApiResponse(code = 500, message = "Internal service error", response = Errors.class),
    })
    WaterCollection getWaterCollectionById(@PathParam("id") @ApiParam(name = "id", value = "Identifier of water collection", required = true)
                                           String id) throws WebApplicationException;
}
