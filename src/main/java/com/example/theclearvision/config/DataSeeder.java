package com.example.theclearvision.config;

import com.example.theclearvision.domain.*;
import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.AppointmentStatus;
import com.example.theclearvision.feature.appointment.repository.AppointmentRepository;
import com.example.theclearvision.feature.auth.repository.RoleRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.branch.Branch;
import com.example.theclearvision.feature.branch.repository.BranchRepository;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderItem;
import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.order.repository.OrderRepository;
import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import com.example.theclearvision.feature.payment.domain.PaymentStatus;
import com.example.theclearvision.persistence.ProductCategoryRepository;
import com.example.theclearvision.persistence.ProductRepository;
import com.example.theclearvision.persistence.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    private final com.example.theclearvision.feature.blog.repository.BlogRepository blogRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Checking database seeding...");

        seedRoles();

        if (userRepository.count() == 0) {
            log.info("Seeding users and other data...");
            seedUsers();
            seedBranches();
            seedProducts();
            seedOrders();
            seedAppointments();
            log.info("Main data seeding completed.");
        }

        if (blogRepository.count() == 0) {
            log.info("Seeding blogs...");
            seedBlogs();
            log.info("Blog seeding completed.");
        }
    }

    private void seedRoles() {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
            log.info("Created ADMIN role");
        }

        if (roleRepository.findByName("CUSTOMER").isEmpty()) {
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            roleRepository.save(customerRole);
            log.info("Created CUSTOMER role");
        }
    }

    private void seedUsers() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role userRole = roleRepository.findByName("CUSTOMER").orElseThrow();

        // Admin User
        if (userRepository.findByEmail("admin@theclearvision.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@theclearvision.com");
            admin.setPhone("+1234567890");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setIsActive(true);
            userRepository.save(admin);
            log.info("Created admin user");
        }

        // Customer 1
        if (userRepository.findByEmail("john@example.com").isEmpty()) {
            User customer1 = new User();
            customer1.setFirstName("John");
            customer1.setLastName("Doe");
            customer1.setEmail("john@example.com");
            customer1.setPhone("1234567890");
            customer1.setPassword(passwordEncoder.encode("password"));
            customer1.setRole(userRole);
            customer1.setIsActive(true);
            userRepository.save(customer1);
        }

        // Customer 2
        if (userRepository.findByEmail("jane@example.com").isEmpty()) {
            User customer2 = new User();
            customer2.setFirstName("Jane");
            customer2.setLastName("Smith");
            customer2.setEmail("jane@example.com");
            customer2.setPhone("0987654321");
            customer2.setPassword(passwordEncoder.encode("password"));
            customer2.setRole(userRole);
            customer2.setIsActive(true);
            userRepository.save(customer2);
        }
    }

    private void seedBranches() {
        Branch branch = new Branch();
        branch.setName("Main Branch");
        branch.setAddress("123 Main St, City");
        branch.setPhone("555-0101");
        branch.setEmail("main@example.com");
        branchRepository.save(branch);
    }

    private void seedProducts() {
        // Check if category already exists
        ProductCategory category = categoryRepository.findByName("Eyeglasses")
                .orElseGet(() -> {
                    ProductCategory newCategory = new ProductCategory();
                    newCategory.setName("Eyeglasses");
                    newCategory.setSlug("eyeglasses");
                    newCategory.setDescription("Prescription eyeglasses");
                    return categoryRepository.save(newCategory);
                });

        Product product = new Product();
        product.setTitle("Classic Aviator");
        product.setSku("AV-001");
        product.setSlug("classic-aviator");
        product.setDescription("Timeless aviator style");
        product.setCategory(category);
        product.setBasePrice(150.00);
        product.setGender(Product.Gender.UNISEX);
        product.setIsActive(true);
        product = productRepository.save(product);

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setVariantName("Gold / Large");
        variant.setFrameColor("Gold");
        variant.setFrameSize("Large");
        variant.setSkuSuffix("GLD-L");
        variant.setPrice(150.00);
        variant.setStockQuantity(50);
        variant.setIsDefault(true);
        variant = productVariantRepository.save(variant);

        ProductImage image1 = new ProductImage();
        image1.setProduct(product);
        image1.setVariant(variant);
        image1.setImageUrl(
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        image1.setAltText("Classic Aviator Gold Large");
        image1.setIsPrimary(true);
        image1.setDisplayOrder(0);
        product.getImages().add(image1);
        variant.getImages().add(image1);

        // Product 2
        Product product2 = new Product();
        product2.setTitle("Modern Wayfarer");
        product2.setSku("WF-002");
        product2.setSlug("modern-wayfarer");
        product2.setDescription("A modern take on the classic wayfarer.");
        product2.setCategory(category);
        product2.setBasePrice(120.00);
        product2.setGender(Product.Gender.UNISEX);
        product2.setIsActive(true);
        product2 = productRepository.save(product2);

        ProductVariant variant2 = new ProductVariant();
        variant2.setProduct(product2);
        variant2.setVariantName("Black / Medium");
        variant2.setFrameColor("Black");
        variant2.setFrameSize("Medium");
        variant2.setSkuSuffix("BLK-M");
        variant2.setPrice(120.00);
        variant2.setStockQuantity(30);
        variant2.setIsDefault(true);
        variant2 = productVariantRepository.save(variant2);

        ProductImage image2 = new ProductImage();
        image2.setProduct(product2);
        image2.setVariant(variant2);
        image2.setImageUrl(
                "https://images.unsplash.com/photo-1572635196237-14b3f281503f?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        image2.setAltText("Modern Wayfarer Black Medium");
        image2.setIsPrimary(true);
        image2.setDisplayOrder(0);
        product2.getImages().add(image2);
        variant2.getImages().add(image2);

        // Product 3
        Product product3 = new Product();
        product3.setTitle("Rimless Titanium");
        product3.setSku("RT-003");
        product3.setSlug("rimless-titanium");
        product3.setDescription("Lightweight and durable titanium frames.");
        product3.setCategory(category);
        product3.setBasePrice(200.00);
        product3.setGender(Product.Gender.MEN);
        product3.setIsActive(true);
        product3 = productRepository.save(product3);

        ProductVariant variant3 = new ProductVariant();
        variant3.setProduct(product3);
        variant3.setVariantName("Silver / Small");
        variant3.setFrameColor("Silver");
        variant3.setFrameSize("Small");
        variant3.setSkuSuffix("SLV-S");
        variant3.setPrice(200.00);
        variant3.setStockQuantity(20);
        variant3.setIsDefault(true);
        variant3 = productVariantRepository.save(variant3);

        ProductImage image3 = new ProductImage();
        image3.setProduct(product3);
        image3.setVariant(variant3);
        image3.setImageUrl(
                "https://images.unsplash.com/photo-1574258495973-f010dfbb5371?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        image3.setAltText("Rimless Titanium Silver Small");
        image3.setIsPrimary(true);
        image3.setDisplayOrder(0);
        product3.getImages().add(image3);
        variant3.getImages().add(image3);
    }

    private void seedOrders() {
        User customer = userRepository.findByEmail("john@example.com").orElseThrow();
        ProductVariant variant = productVariantRepository.findAll().get(0);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(customer);
        order.setStatus(OrderStatus.PENDING); // Changed from PAID to PENDING since no payment
        order.setTotal(BigDecimal.valueOf(150.00));

        // Removed Payment creation - causing TransientPropertyValueException
        // Payment should be handled through proper order flow, not in seed data

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductVariant(variant);
        item.setQuantity(1);
        item.setUnitPrice(BigDecimal.valueOf(150.00));
        item.setLineTotal(BigDecimal.valueOf(150.00));

        order.setItems(List.of(item));

        orderRepository.save(order);
    }

    private void seedAppointments() {
        User customer1 = userRepository.findByEmail("john@example.com").orElseThrow();
        User customer2 = userRepository.findByEmail("jane@example.com").orElseThrow();
        Branch branch = branchRepository.findAll().get(0);

        Appointment apt1 = new Appointment();
        apt1.setUser(customer1);
        apt1.setBranch(branch);
        apt1.setAppointmentTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        apt1.setDurationMinutes(30);
        apt1.setStatus(AppointmentStatus.SCHEDULED);
        apt1.setNotes("Eye exam");
        appointmentRepository.save(apt1);

        Appointment apt2 = new Appointment();
        apt2.setUser(customer2);
        apt2.setBranch(branch);
        apt2.setAppointmentTime(LocalDateTime.now().minusDays(1).withHour(14).withMinute(0));
        apt2.setDurationMinutes(60);
        apt2.setStatus(AppointmentStatus.COMPLETED);
        apt2.setNotes("Contact lens fitting");
        appointmentRepository.save(apt2);
    }

    private void seedBlogs() {
        com.example.theclearvision.feature.blog.domain.Blog blog1 = new com.example.theclearvision.feature.blog.domain.Blog();
        blog1.setTitle("Understanding Vision Health");
        blog1.setSlug("understanding-vision-health");
        blog1.setContent(
                "Vision health is crucial for overall well-being. Regular eye exams can detect early signs of eye conditions...");
        blog1.setSummary("Learn why regular eye exams are essential for maintaining good vision and overall health.");
        blog1.setAuthor("Dr. Sarah Johnson");
        blog1.setCoverImage(
                "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        blog1.setIsActive(true);
        blogRepository.save(blog1);

        com.example.theclearvision.feature.blog.domain.Blog blog2 = new com.example.theclearvision.feature.blog.domain.Blog();
        blog2.setTitle("Choosing the Right Frames");
        blog2.setSlug("choosing-the-right-frames");
        blog2.setContent(
                "Selecting the perfect frames involves considering your face shape, skin tone, and personal style...");
        blog2.setSummary("A guide to finding the perfect eyeglass frames that complement your face shape and style.");
        blog2.setAuthor("Michael Chen");
        blog2.setCoverImage(
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        blog2.setIsActive(true);
        blogRepository.save(blog2);

        com.example.theclearvision.feature.blog.domain.Blog blog3 = new com.example.theclearvision.feature.blog.domain.Blog();
        blog3.setTitle("The Importance of UV Protection");
        blog3.setSlug("importance-of-uv-protection");
        blog3.setContent(
                "UV rays can be harmful to your eyes. Wearing sunglasses with 100% UV protection is essential...");
        blog3.setSummary("Why you should always wear sunglasses to protect your eyes from harmful UV rays.");
        blog3.setAuthor("Dr. Emily White");
        blog3.setCoverImage(
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        blog3.setIsActive(true);
        blogRepository.save(blog3);

        com.example.theclearvision.feature.blog.domain.Blog blog4 = new com.example.theclearvision.feature.blog.domain.Blog();
        blog4.setTitle("Digital Eye Strain: Tips for Relief");
        blog4.setSlug("digital-eye-strain-tips");
        blog4.setContent("In today's digital age, eye strain is common. Follow these tips to reduce discomfort...");
        blog4.setSummary("Simple tips to reduce digital eye strain from prolonged screen use.");
        blog4.setAuthor("Dr. Mark Davis");
        blog4.setCoverImage(
                "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80");
        blog4.setIsActive(true);
        blogRepository.save(blog4);
    }
}
