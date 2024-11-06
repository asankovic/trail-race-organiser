package com.asankovic.race.command.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(secret)) {
            throw new IllegalStateException("Token secret must be provided! Make sure that you keep it in a safe place");
        }
    }

    public boolean isTokenValid(final String token) {
        try {
            final var secretPassword = Keys.password(secret.toCharArray());

            return Jwts.parser()
                    .verifyWith(secretPassword)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (final RuntimeException e) {
            LOG.info("Unable to verify token", e);
            return false;
        }
    }
}