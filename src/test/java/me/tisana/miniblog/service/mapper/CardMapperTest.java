package me.tisana.miniblog.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    public void setUp() {
        cardMapper = new CardMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cardMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cardMapper.fromId(null)).isNull();
    }
}
