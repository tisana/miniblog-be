package me.tisana.miniblog.domain;

import static me.tisana.miniblog.domain.AuthorTestSamples.*;
import static me.tisana.miniblog.domain.CardTestSamples.*;
import static me.tisana.miniblog.domain.CategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import me.tisana.miniblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = getCardSample1();
        Card card2 = new Card();
        assertThat(card1).isNotEqualTo(card2);

        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);

        card2 = getCardSample2();
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void authorTest() {
        Card card = getCardRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        card.setAuthor(authorBack);
        assertThat(card.getAuthor()).isEqualTo(authorBack);

        card.author(null);
        assertThat(card.getAuthor()).isNull();
    }

    @Test
    void categoryTest() {
        Card card = getCardRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        card.setCategory(categoryBack);
        assertThat(card.getCategory()).isEqualTo(categoryBack);

        card.category(null);
        assertThat(card.getCategory()).isNull();
    }
}
