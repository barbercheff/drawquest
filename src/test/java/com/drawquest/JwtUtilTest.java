package com.drawquest;

import com.drawquest.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String TEST_SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void validateTokenReturnsFalseWhenTokenIsExpired() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", -1000);

        String token = jwtUtil.generateToken("alice");

        assertThat(jwtUtil.validateToken(token)).isFalse();
    }

    @Test
    void validateTokenReturnsTrueWhenTokenIsNotExpired() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000);

        String token = jwtUtil.generateToken("alice");

        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("alice");
    }
}
