package me.tisana.miniblog.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import me.tisana.miniblog.domain.Author;
import me.tisana.miniblog.service.AuthorService;
import me.tisana.miniblog.service.CardService;
import me.tisana.miniblog.service.dto.CardDTO;
import me.tisana.miniblog.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link me.tisana.miniblog.domain.Card}.
 */
@RestController
@RequestMapping("/api")
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private static final String ENTITY_NAME = "miniBlogCard";
    private static final String AUTH_REQUIRED_ERROR_KEY = "authentication required";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardService cardService;
    private final AuthorService authorService;


    public CardResource(CardService cardService, AuthorService authorService) {
        this.cardService = cardService;
        this.authorService = authorService;
    }

    /**
     * {@code POST  /cards} : Create a new card.
     *
     * @param cardDTO the cardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardDTO, or with status {@code 400 (Bad Request)} if the card has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to save Card : {}", cardDTO);
        if (cardDTO.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (cardDTO.getAuthorId() == null && cardDTO.getAuthorUsername() == null) {
            throw new BadRequestAlertException("Author is required", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        CardDTO result = cardService.save(cardDTO);
        return ResponseEntity.created(new URI("/api/cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cards} : Updates an existing card.
     *
     * @param cardDTO the cardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardDTO,
     * or with status {@code 400 (Bad Request)} if the cardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cards")
    public ResponseEntity<CardDTO> updateCard(@Valid @RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to update Card : {}", cardDTO);
        if (cardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (cardDTO.getAuthorId() == null && cardDTO.getAuthorUsername() == null) {
            throw new BadRequestAlertException("Author is required", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        if (cardDTO.getAuthorPassword() == null) {
            throw new BadRequestAlertException("Authentication is required", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        //check username + password matching
        Author author = authorService.findOneByUsername(cardDTO.getAuthorUsername());
        if (author == null || !(author.getPassword().equals(cardDTO.getAuthorPassword()))) {
            throw new BadRequestAlertException("Invalid Credential", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        CardDTO result = cardService.save(cardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cards} : get all the cards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cards in body.
     */
    @GetMapping("/cards")
    public ResponseEntity<List<CardDTO>> getAllCards(Pageable pageable) {
        log.debug("REST request to get a page of Cards");
        Page<CardDTO> page = cardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cards/:id} : get the "id" card.
     *
     * @param id the id of the cardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cards/{id}")
    public ResponseEntity<CardDTO> getCard(@PathVariable Long id) {
        log.debug("REST request to get Card : {}", id);
        Optional<CardDTO> cardDTO = cardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardDTO);
    }

    /**
     * {@code DELETE  /cards/:id} : delete the "id" card.
     *
     * @param id the id of the cardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id, @RequestParam Map<String, String> params) {
        log.debug("REST request to delete Card : {}", id);

        String username = params.get("authorUsername");
        String password = params.get("password");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new BadRequestAlertException("Authentication is required", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        //check username + password matching
        Author author = authorService.findOneByUsername(username);
        if (author == null || !(author.getPassword().equals(password))) {
            throw new BadRequestAlertException("Invalid Credential", ENTITY_NAME, AUTH_REQUIRED_ERROR_KEY);
        }

        cardService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
