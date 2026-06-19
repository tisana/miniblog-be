package me.tisana.miniblog.repository;

import java.util.List;
import java.util.Optional;
import me.tisana.miniblog.domain.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Card entity.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    default Optional<Card> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Card> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Card> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select card from Card card left join fetch card.author left join fetch card.category",
        countQuery = "select count(card) from Card card"
    )
    Page<Card> findAllWithToOneRelationships(Pageable pageable);

    @Query("select card from Card card left join fetch card.author left join fetch card.category")
    List<Card> findAllWithToOneRelationships();

    @Query("select card from Card card left join fetch card.author left join fetch card.category where card.id =:id")
    Optional<Card> findOneWithToOneRelationships(@Param("id") Long id);
}
