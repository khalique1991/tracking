//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import java.time.OffsetDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import com.fasterxml.jackson.databind.ObjectMapper;  // Import ObjectMapper
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest // Use SpringBootTest
//public class TrackingNumberGeneratorApiTest {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TrackingNumberGenerator trackingNumberGenerator;
//
//    @Autowired
//    private ObjectMapper objectMapper; // Autowire ObjectMapper
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build(); // Setup MockMvc
//    }
//
//    @Test
//    public void testGetNextTrackingNumber_Success() throws Exception {
//        // Arrange
//        String customerId = "CUST-456";
//        String customerName = "Test Customer";
//        String trackingNumber = "TN123456789";
//        OffsetDateTime now = OffsetDateTime.now();
//
//        when(trackingNumberGenerator.generateTrackingNumber(customerId, null, customerName)).thenReturn(trackingNumber);
//
//        // Act
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/next-tracking-number")
//                        .param("customer_id", customerId)
//                        .param("customer_name", customerName)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        String responseJson = result.getResponse().getContentAsString();
//        Map<String, Object> response = objectMapper.readValue(responseJson, Map.class); // Use ObjectMapper
//
//        assertNotNull(responseJson);
//        assertEquals(trackingNumber, response.get("tracking_number"));
//        assertNotNull(response.get("created_at"));
//        assertEquals(customerId, response.get("customer_id"));
//        assertEquals(customerName, response.get("customer_name"));
//
//        verify(trackingNumberGenerator, times(1)).generateTrackingNumber(customerId, null, customerName);
//    }
//
//    @Test
//    public void testGetNextTrackingNumber_InvalidDate() throws Exception {
//        // Arrange
//        String invalidDate = "Invalid Date";
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/next-tracking-number")
//                        .param("created_at", invalidDate)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void testGetNextTrackingNumber_GeneratorReturnsInvalidTrackingNumber() throws Exception {
//        // Arrange
//        String customerId = "CUST-456";
//        String customerName = "Test Customer";
//        String invalidTrackingNumber = "INVALID-TRACKING-NUMBER";
//
//        when(trackingNumberGenerator.generateTrackingNumber(customerId, null, customerName)).thenReturn(invalidTrackingNumber);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/next-tracking-number")
//                        .param("customer_id", customerId)
//                        .param("customer_name", customerName)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    public void testGetNextTrackingNumber_SuccessWithAllParameters() throws Exception {
//        // Arrange
//        String originCountryId = "US";
//        String destinationCountryId = "CA";
//        String weight = "10.50";
//        String createdAt = "2024-08-28T12:00:00Z";
//        String customerId = "CUST-789";
//        String customerName = "Test Customer 2";
//        String customerSlug = "test-customer-2";
//        String trackingNumber = "TN987654321";
//        OffsetDateTime parsedCreatedAt = OffsetDateTime.parse(createdAt);
//
//        when(trackingNumberGenerator.generateTrackingNumber(customerId, parsedCreatedAt.toInstant().toEpochMilli(), customerName)).thenReturn(trackingNumber);
//
//        // Act
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/next-tracking-number")
//                        .param("origin_country_id", originCountryId)
//                        .param("destination_country_id", destinationCountryId)
//                        .param("weight", weight)
//                        .param("created_at", createdAt)
//                        .param("customer_id", customerId)
//                        .param("customer_name", customerName)
//                        .param("customer_slug", customerSlug)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        String responseJson = result.getResponse().getContentAsString();
//        Map<String, Object> response = objectMapper.readValue(responseJson, Map.class);
//
//        assertNotNull(responseJson);
//        assertEquals(trackingNumber, response.get("tracking_number"));
//        assertEquals(parsedCreatedAt.toString(), OffsetDateTime.parse(response.get("created_at").toString()).toString());
//        assertEquals(customerId, response.get("customer_id"));
//        assertEquals(customerName, response.get("customer_name"));
//
//        verify(trackingNumberGenerator, times(1)).generateTrackingNumber(customerId, parsedCreatedAt.toInstant().toEpochMilli(), customerName);
//    }
//}
//
