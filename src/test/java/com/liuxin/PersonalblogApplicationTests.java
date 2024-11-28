package com.liuxin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class PersonalblogApplicationTests {
    @Test
    public void testGenJWT() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "liuxin");
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "liuxin")//设置签名算法
                .setClaims(claims)//设置信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))//毫秒值，过期时间
                .compact();
        System.out.println(jwt);


    }

    @Test
    public void testParseJWT() {
        Claims claims = Jwts.parser()
                .setSigningKey("liuxin")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoibGl1eGluIiwiaWQiOjEsImV4cCI6MTczMjYwNDY2NX0.5QjyoO8b2hh5U89pvfgFkltKljThU3BPpeDEf2lHn_Y")
                .getBody();
        System.out.println(claims);

    }


}
