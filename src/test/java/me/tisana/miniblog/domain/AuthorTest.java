package me.tisana.miniblog.domain;

import static me.tisana.miniblog.domain.AuthorTestSamples.*;
import static me.tisana.miniblog.domain.CardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import me.tisana.miniblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void cardTest() {
        Author author = getAuthorRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        author.addCard(cardBack);
        assertThat(author.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getAuthor()).isEqualTo(author);

        author.removeCard(cardBack);
        assertThat(author.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getAuthor()).isNull();

        author.cards(new HashSet<>(Set.of(cardBack)));
        assertThat(author.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getAuthor()).isEqualTo(author);

        author.setCards(new HashSet<>());
        assertThat(author.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getAuthor()).isNull();
    }
}
