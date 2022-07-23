package org.V.TinL.repository;

import org.V.TinL.entity.LongToShort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<LongToShort, Long> {
    LongToShort findByLongUrl(String longUrl);
    LongToShort findByShortUrl(String shortUrl);
}
