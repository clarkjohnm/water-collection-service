package org.cybersapien.watercollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.IgniteCache;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    @Autowired
    private MockMvc mockClient;

    /**
     * WaterCollection cache
     */
    @Autowired
    private IgniteCache<String, WaterCollection> waterCollectionCache;

    /**
     * Test GET water collection
     *
     * @throws Exception if an error occurs
     */
    @Test
    @WithMockUser(username = "wcsuser", password = "usersecret", roles = "USER")
    public void getWaterCollection() throws Exception {
        final WaterCollection waterCollection = WaterCollectionCreator.buildMinimal();
        final String id = waterCollection.getId();

        // Put water collection in cache
        waterCollectionCache.put(id, waterCollection);

        this.mockClient
                .perform(get("/v1/water-collections/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
}