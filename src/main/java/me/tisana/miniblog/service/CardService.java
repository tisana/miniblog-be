package me.tisana.miniblog.service;

import me.tisana.miniblog.domain.Author;
import me.tisana.miniblog.domain.Card;
import me.tisana.miniblog.repository.AuthorRepository;
import me.tisana.miniblog.repository.CardRepository;
import me.tisana.miniblog.service.dto.CardDTO;
import me.tisana.miniblog.service.mapper.CardMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Card}.
 */
@Service
@Transactional
public class CardService {

    private final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;
    private final AuthorRepository authorRepository;

    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, AuthorRepository authorRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.authorRepository = authorRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save.
     * @return the persisted entity.
     */
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);

        //If user id does not exist, check against db with username
        if (StringUtils.isNotEmpty(cardDTO.getAuthorUsername()) && cardDTO.getAuthorId() == null) {
            Author author = authorRepository.findOneByUsername(cardDTO.getAuthorUsername());
            if (author == null) {
                //create new author
                author = new Author();
                author.setUsername(cardDTO.getAuthorUsername());
                author.setPassword(RandomStringUtils.random(12, cardDTO.getAuthorUsername()));
                author = authorRepository.save(author);
            }
            cardDTO.setAuthorId(author.getId());

        }

        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        return cardRepository.findAll(pageable)
            .map(cardMapper::toDto);
    }


    /**
     * Get one card by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id)
            .map(cardMapper::toDto);
    }

    /**
     * Delete the card by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
