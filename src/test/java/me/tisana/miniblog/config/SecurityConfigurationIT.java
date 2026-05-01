package me.tisana.miniblog.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import me.tisana.miniblog.management.SecurityMetersService;
import me.tisana.miniblog.security.AuthoritiesConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.config.JHipsterProperties;

@AutoConfigureMockMvc
@ImportAutoConfiguration(WebMvcAutoConfiguration.class)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        SecurityConfigurationIT.TestController.class,
        SecurityConfigurationIT.TestMeterRegistryConfiguration.class,
    }
)
class SecurityConfigurationIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void apiPostRequestsDoNotRequireCsrfToken() throws Exception {
        mvc.perform(post("/api/csrf-test")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void nonApiPostRequestsRequireCsrfToken() throws Exception {
        mvc.perform(post("/management/csrf-test")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void nonApiPostRequestsAcceptValidCsrfToken() throws Exception {
        mvc.perform(post("/management/csrf-test").with(csrf())).andExpect(status().isNoContent());
    }

    @RestController
    static class TestController {

        @PostMapping({ "/api/csrf-test", "/management/csrf-test" })
        ResponseEntity<Void> csrfTest() {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @TestConfiguration
    static class TestMeterRegistryConfiguration {

        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }
}
