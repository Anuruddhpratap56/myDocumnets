package com.mydocs.mydocs.springSecurity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.mydocs.mydocs.Repositories.userRepositories.UserRepository;
import com.mydocs.mydocs.domain.userDomain.Roles;
import com.mydocs.mydocs.domain.userDomain.Users;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenUtil {

    @Autowired
    private UserRepository userRepository;

    public static final String SECRET_KEY="f107cdb4720cff3fde260f69a28c199a252b3d3002d57d5b26800c8593d69f2f0a71f97edfbb655dba95b53155bff56614aeb5e29f7b80c5e09d6786c585e5123b704542f2d8e273059106d16b8927f215c4ed29280bd91a44641e08e9b32ac1b04808a8ebcb24f030ae6695583950f0bdace9f88d90129cbee9fa2a60756161cd464df9dd4ce070414d7d3ca9c28460ab6b61840371d669731d5475ed8a401548d361b06dd039078b6ebdcf7d4ca58d4dc4f5cd8fc27f3621778431e838b51f468d0f46b48126ccb69d22a3b497aebf2d9eb1ae724ba5fdd7a31dcd1738e35f1035927a53fcedfa7496eed08106678ce5188acd8e1f2eaa8be0451def920d30ce0249f7dd861fbfb012ec48475ae7f83357035ee0cb40b718cd1faa1966c6b60b66a5ea4158059c7e0f461e4f49028d6662cf04f308e7aae51e77d125677f1966ec217147525aa900c852d0b8dd1f7baef3a3a5d10f640a20c226101c34ac7dad4b4a7acf1058e27223a9e219602992eaa1c3b5b0f9fe245b87cd0eac99e539bc1d58a641fd904daaf46b9bd9fd5e78add8b823cde118da5c223fd7bb47175c331d57e7c6b2efe5f6ec870376e6dc777e122e4dfda671f92e040dd4a8020cb75271b649fb5d032c40cf01ef9ca10cf90745f4944069346e6ba58e6a00e7ac42c52b5650f88fe3508b204e78c5887a13654138898ca069cf497a37ab44ffb2ec";
    private static final long ACCESS_TOKEN_VALIDITY = 10 * 60 * 1000; // 5 minutes
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days
 
    public String generateAccessToken(String username) {
        System.out.println("in jwt util");

        Map<String,Object> claims = new HashMap<>();
        Users user = userRepository.findByEmail(username);
        List<String> roles = user.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        claims.put("roles",roles);
        claims.put("userId",user.getId());
        claims.put("email",user.getEmail());


        String jwt =  Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return jwt;
                
    }
 
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
 
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
 
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getJwtFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "JWT");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;

    }

    public List<String> getRolesFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

    return (List<String>) claims.get("roles");
}

}
