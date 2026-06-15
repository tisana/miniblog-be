package me.tisana.miniblog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.tisana.miniblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorDTO.class);
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setId(1L);
        AuthorDTO authorDTO2 = new AuthorDTO();
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO2.setId(authorDTO1.getId());
        assertThat(authorDTO1).isEqualTo(authorDTO2);
        authorDTO2.setId(2L);
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
        authorDTO1.setId(null);
        assertThat(authorDTO1).isNotEqualTo(authorDTO2);
    }

    @Test
    void shouldNotSerializePassword() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setUsername("author");
        authorDTO.setPassword("secret-password");

        String json = new ObjectMapper().writeValueAsString(authorDTO);

        assertThat(json).contains("username");
        assertThat(json).doesNotContain("password");
        assertThat(json).doesNotContain("secret-password");
    }

    @Test
    void shouldDeserializePassword() throws Exception {
        String json = "{\"username\":\"author\",\"password\":\"secret-password\"}";

        AuthorDTO authorDTO = new ObjectMapper().readValue(json, AuthorDTO.class);

        assertThat(authorDTO.getUsername()).isEqualTo("author");
        assertThat(authorDTO.getPassword()).isEqualTo("secret-password");
    }
}
