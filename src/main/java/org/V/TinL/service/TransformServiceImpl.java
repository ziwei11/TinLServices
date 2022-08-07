package org.V.TinL.service;

import org.V.TinL.entity.LongToShort;
import org.V.TinL.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import static org.V.TinL.transform_function.HashCode.getShortUrlByHashCode;
import static org.V.TinL.validate.UrlValidate.isValidUrl;


@Service
public class TransformServiceImpl implements TransformService {

    String tinyUrlBase = "https://www.TinL.com/";

    @Autowired
    private UrlRepository urlRepository;
    private final RedisService redisService;

    public TransformServiceImpl(UrlRepository urlRepository, RedisService redisService) {
        this.urlRepository = urlRepository;
        this.redisService = redisService;
    }


    @Override
    @Transactional
    public String longToShort(String longUrl) {

        // check if the longUrl is not validated
        if (!isValidUrl(longUrl)){
            System.out.println("URL is invalid.");
            System.out.println("==============================");
            return null;
        }

        // check if the shortUrl is already in Cache
        if (fetchShortUrlFromCache(longUrl) != null) {
            String hashcodePart = fetchShortUrlFromCache(longUrl);
            showExpireTime(longUrl, hashcodePart);
            System.out.println("The short URL is already in Cache.");
            System.out.println("==============================");
            return tinyUrlBase + hashcodePart;
        }

        // check if the shortUrl is already in Database
        if (urlRepository.findByLongUrl(longUrl) != null){
            LongToShort longToShortTemp = urlRepository.findByLongUrl(longUrl);
            System.out.println("The short URL is already in the Database.");

            // save shortUrl and longUrl to Cache
            String hashcodePart = longToShortTemp.getShortUrl();
            saveUrlToCache(longUrl, hashcodePart);
            System.out.println("==============================");
            return tinyUrlBase + hashcodePart;
        }

        // create a new instance longToShort
        LongToShort longToShort = new LongToShort();

        // set String longUrl to longToShort
        longToShort.setLongUrl(longUrl);

        // set Timestamp timeStamp to longToShort
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        longToShort.setCreatedAt(timeStamp);

        // First Way: Hashcode
        // transfer longUrl to shortUrl
        // set String shortUrl to longToShort
        String hashcodePart = getShortUrlByHashCode(longUrl);
        longToShort.setShortUrl(hashcodePart);

        // save longToShort to database (UrlRepository)
        urlRepository.save(longToShort);
        System.out.println("Save the longToShort to Database.");

        // save shortUrl and longUrl to Cache (Redis)
        saveUrlToCache(longUrl, hashcodePart);

        // String shortUrl = tinyUrlBase + hashcodePart;
        System.out.println("==============================");
        return tinyUrlBase + hashcodePart;
    }


    @Override
    @Transactional
    public String shortToLong(String shortUrl) {

        // get the hashcode part of the shortUrl
        String hashcodePart = shortUrl.substring(21);

        // check if the longUrl can be get in Cache
        if (fetchLongUrlFromCache(hashcodePart) != null) {
            String longUrl = fetchLongUrlFromCache(hashcodePart);
            showExpireTime(longUrl, hashcodePart);
            System.out.println("The long URL is already in Cache.");
            System.out.println("==============================");
            return longUrl;
        }

        // use UrlRepository.findByShortUrl to find the corresponding data
        LongToShort longToShort = urlRepository.findByShortUrl(hashcodePart);
        System.out.println("The long URL is already in Database.");

        // save shortUrl and longUrl to Cache (Redis)
        String longUrl = longToShort.getLongUrl();
        saveUrlToCache(longUrl, hashcodePart);

        System.out.println("==============================");
        return longToShort.getLongUrl();
    }


    private String fetchShortUrlFromCache(String longUrl) {
        String shortUrl = (String)redisService.get(longUrl);
        // change the expire time
        if (shortUrl != null) {
            redisService.expire(longUrl, 40);
            //redisService.expire(shortUrl, 45);
        }
        return shortUrl;
    }


    private String fetchLongUrlFromCache(String shortUrl) {
        String longUrl = (String)redisService.get(shortUrl);
        // change the expire time
        if (longUrl != null) {
            //redisService.expire(longUrl, 50);
            redisService.expire(shortUrl, 55);
        }
        return longUrl;
    }


    private void saveUrlToCache(String longUrl, String shortUrl) {
        int time = 30;
        redisService.setLTS(longUrl, shortUrl, time);
        redisService.setSTL(shortUrl, longUrl, time);
        System.out.println("Save the shortUrl and longUrl to Cache.");
    }


    private void showExpireTime(String longUrl, String shortUrl) {
        //System.out.println("Expire Time: ");
        System.out.println("longUrl expire time: " + redisService.getExpire(longUrl));
        System.out.println("shortUrl expire time: " + redisService.getExpire(shortUrl));
    }


}

