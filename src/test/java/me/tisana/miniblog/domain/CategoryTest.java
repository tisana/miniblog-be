package me.tisana.miniblog.domain;

import static me.tisana.miniblog.domain.CardTestSamples.*;
import static me.tisana.miniblog.domain.CategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import me.tisana.miniblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void cardTest() {
        Category category = getCategoryRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        category.addCard(cardBack);
        assertThat(category.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getCategory()).isEqualTo(category);

        category.removeCard(cardBack);
        assertThat(category.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getCategory()).isNull();

        category.cards(new HashSet<>(Set.of(cardBack)));
        assertThat(category.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getCategory()).isEqualTo(category);

        category.setCards(new HashSet<>());
        assertThat(category.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getCategory()).isNull();
    }
}
