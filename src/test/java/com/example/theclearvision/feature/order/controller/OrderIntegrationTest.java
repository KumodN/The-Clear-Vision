import com.example.theclearvision.domain.Address;
import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductCategory;
import com.example.theclearvision.domain.ProductVariant;
import com.example.theclearvision.domain.Role;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.auth.repository.AddressRepository;
import com.example.theclearvision.feature.auth.repository.RoleRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.order.dto.CreateOrderRequest;
import com.example.theclearvision.feature.order.dto.OrderItemRequest;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import com.example.theclearvision.persistence.ProductCategoryRepository;
import com.example.theclearvision.persistence.ProductRepository;
import com.example.theclearvision.persistence.ProductVariantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Address testAddress;
    private ProductVariant testVariant;

    @BeforeEach
    void setUp() {
        // Setup Role
        Role customerRole = roleRepository.findByName("CUSTOMER").orElseGet(() -> {
            Role role = new Role();
            role.setName("CUSTOMER");
            return roleRepository.save(role);
        });

        // Setup User
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPhone("1234567890");
        testUser.setRole(customerRole);
        testUser = userRepository.save(testUser);

        // Setup Address
        testAddress = new Address();
        testAddress.setUser(testUser);
        testAddress.setLabel("Home");
        testAddress.setLine1("123 Test St");
        testAddress.setCity("Test City");
        testAddress.setPostalCode("12345");
        testAddress = addressRepository.save(testAddress);

        // Setup Category
        ProductCategory category = new ProductCategory();
        category.setName("Eyeglasses");
        category.setSlug("eyeglasses");
        category = productCategoryRepository.save(category);

        // Setup Product
        Product product = new Product();
        product.setTitle("Test Frame");
        product.setSku("TEST-SKU-001");
        product.setSlug("test-frame");
        product.setBasePrice(100.0);
        product.setCategory(category);
        product = productRepository.save(product);

        // Setup Variant
        testVariant = new ProductVariant();
        testVariant.setProduct(product);
        testVariant.setVariantName("Black / Medium");
        testVariant.setFrameColor("Black");
        testVariant.setFrameSize("Medium");
        testVariant.setPrice(120.0);
        testVariant.setStockQuantity(10);
        testVariant.setSkuSuffix("BLK-MED");
        testVariant = productVariantRepository.save(testVariant);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldCreateOrderSuccessfully() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddressId(testAddress.getId());
        request.setBillingAddressId(testAddress.getId());
        request.setPaymentMethod(PaymentMethod.CARD);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductVariantId(testVariant.getId());
        itemRequest.setQuantity(2);
        request.setItems(List.of(itemRequest));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.status").value("PAID")) // Since CARD simulates success
                .andExpect(jsonPath("$.total").value(240.0));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldFailOrderWhenOutOfStock() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddressId(testAddress.getId());
        request.setBillingAddressId(testAddress.getId());
        request.setPaymentMethod(PaymentMethod.COD);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductVariantId(testVariant.getId());
        itemRequest.setQuantity(11); // Stock is 10
        request.setItems(List.of(itemRequest));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Or BadRequest if we handled it better
    }
}
