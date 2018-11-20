package org.cybersapien.watercollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cybersapien.watercollection.processors.ProcessingState;
import org.cybersapien.watercollection.service.v1.model.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.cache.Cache;
import javax.inject.Inject;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for the Water Collection Service Controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class WaterCollectionControllerTest {
    /**
     * Jackson ObjectMapper to convert water collection to/from JSON
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mock client
     */
    @Inject
    private MockMvc mockClient;

    /**
     * Water collection cache
     */
    @Inject
    private Cache<String, WaterCollection> waterCollectionCache;

    /**
     * Controller
     */
    @Inject
    private WaterCollectionController waterCollectionController;

    /**
     * Test GET a list of water collections
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void testGetWaterCollections() throws Exception {
        final WaterCollection inputWaterCollection = WaterCollectionCreator.buildMinimal();
        inputWaterCollection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        inputWaterCollection.setProcessingState(ProcessingState.NOT_STARTED.name());

        // Put water collection in cache
        waterCollectionCache.put(inputWaterCollection.getId(), inputWaterCollection);

        this.mockClient
                .perform(get("/v1/water-collections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test GET a water collection by id
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void testGetWaterCollectionById() throws Exception {
        final WaterCollection inputWaterCollection = WaterCollectionCreator.buildMinimal();
        inputWaterCollection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        inputWaterCollection.setProcessingState(ProcessingState.NOT_STARTED.name());

        // Put water collection in cache
        waterCollectionCache.put(inputWaterCollection.getId(), inputWaterCollection);

        this.mockClient
                .perform(get("/v1/water-collections/" + inputWaterCollection.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test GET a water collection by id with unauthorized user
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "steven", password = "king", roles = "NOVELIST")
    public void testGetWaterCollectionByIdWithUnauthorizedUser() throws Exception {
        final WaterCollection inputWaterCollection = WaterCollectionCreator.buildMinimal();
        inputWaterCollection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        inputWaterCollection.setProcessingState(ProcessingState.NOT_STARTED.name());

        // Put water collection in cache
        waterCollectionCache.put(inputWaterCollection.getId(), inputWaterCollection);

        this.mockClient
                .perform(get("/v1/water-collections/" + inputWaterCollection.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /**
     * Test POST a water collection
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void postWaterCollection() throws Exception {
        this.mockClient
                .perform(post("/v1/water-collections/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WaterCollectionCreator.buildMinimal())))
                .andExpect(status().isOk());
    }

    /**
     * Test POST an invalid water collection
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void postWaterCollectionWithMissingRequiredField() throws Exception {
        this.mockClient
                .perform(post("/v1/water-collections/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WaterCollectionCreator.buildWithMissingRequiredField())))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST a water collection with an unsupported media type
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void postWaterCollectionWithUnsupportedMediaType() throws Exception {
        this.mockClient
                .perform(post("/v1/water-collections/")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(WaterCollectionCreator.buildWithMissingRequiredField())))
                .andExpect(status().isUnsupportedMediaType());
    }
}