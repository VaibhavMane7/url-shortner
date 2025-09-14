package com.url.shortner.repository;

import com.url.shortner.models.URLMapping;
import com.url.shortner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<URLMapping, Long> {

    /**
     * Finds a URLMapping by its original URL.
     * @param originalURl The original long URL.
     * @return An Optional containing the URLMapping if found.
     */
    Optional<URLMapping> findByOriginalURl(String originalURl);

    /**
     * Finds a URLMapping by the generated short URL.
     * This is used to check for uniqueness when generating a new short URL
     * and to retrieve the original URL for redirection.
     * @param shortUrl The generated short URL.
     * @return An Optional containing the URLMapping if found.
     */
    Optional<URLMapping> findByShortUrl(String shortUrl);

    /**
     * Finds a list of URLMappings created by a specific user.
     * @param user The User entity.
     * @return A list of URLMappings associated with the user.
     */
    List<URLMapping> findByUser(User user);

    /**
     * Finds a URLMapping by the original URL and the user who created it.
     * This prevents a user from creating a new short URL for an original one
     * they have already shortened.
     * @param originalURl The original long URL.
     * @param user The User entity.
     * @return An Optional containing the URLMapping if found.
     */
    Optional<URLMapping> findByOriginalURlAndUser(String originalURl, User user);
}
