package top.lothar.usercenter.util;

import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>JWT 加密 解密</h2>
 *
 * 第一段
 *
 *  {"alg":"HS256"} 加密算法
 *
 * 第二段
 *  iss：发行人
 *  exp：到期时间
 *  sub：主题
 *  aud：用户
 *  nbf：在此之前不可用
 *  iat：发布时间
 *  jti：JWT ID用于标识该JWT
 *  除以上默认字段外，我们还可以自定义私有字段
 *  {"id":"1","exp":1620126286,"iat":1618916686,"username":"LT"}
 *
 * @author zhaolutong
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("WeakerAccess")
@Component
public class JwtOperator {
    /**
     * 秘钥默认
     */
    @Value("${secret:aaabbbcccdddeeefffggghhhiiijjjkkklllmmmnnnooopppqqqrrrsssttt}")
    private String secret;
    /**
     * 有效期，单位秒
     * - 默认2周
     */
    @Value("${expire-time-in-second:1209600}")
    private Long expirationTimeInSecond;

    /**
     * 从token中获取claim
     *
     * @param token token
     * @return claim {包含用户信息, 过期时间等}
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }

    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token)
            .getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 判断token是否非法
     *
     * @param token token
     * @return 未过期返回true，否则返回false
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 计算token的过期时间
     *
     * @return 过期时间
     */
    public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + this.expirationTimeInSecond * 1000);
    }


    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息 {@link Map 包含用户信息}
     * @return token
     */
    public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = this.getExpirationTime();
        // 签名使用的算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        // 对设置的密钥设置
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
            .setClaims(claims)
            // 发行时间
            .setIssuedAt(createdTime)
            .setExpiration(expirationTime)
            // 你也可以改用你喜欢的算法
            // 支持的算法详见：https://github.com/jwtk/jjwt#features
            .signWith(SignatureAlgorithm.HS256, signingKey)
            .compact();
    }


//    public static void main(String[] args) {
//        // 1. 初始化
//        JwtOperator jwtOperator = new JwtOperator();
//        jwtOperator.expirationTimeInSecond = 1209600L;
//        jwtOperator.secret = "aaabbbcccdddeeefffggghhhiiijjjkkklllmmmnnnooopppqqqrrrsssttt";
//
//        // 2.设置用户信息
//        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
//        objectObjectHashMap.put("id", "1");
//        objectObjectHashMap.put("username", "LT");
//
//        // 测试1: 生成token
//        String token = jwtOperator.generateToken(objectObjectHashMap);
//        System.out.println(token);
//
//        // 测试2: 如果能token合法且未过期，返回true
//        Boolean validateToken = jwtOperator.validateToken(token);
//        System.out.println(validateToken);
//
//        // 测试3: 获取用户信息
//        Claims claims = jwtOperator.getClaimsFromToken(token);
//        System.out.println(claims);
//
//        String[] split = token.split("[.]");
//
//        // 测试4: 生成的token的第一段 解密Header
//        byte[] header = Base64.decodeBase64(split[0].getBytes());
//        System.out.println("第一段解密："+new String(header));
//
//        // 生成的token的第二段 解密Payload
//        byte[] payload = Base64.decodeBase64(split[1].getBytes());
//        System.out.println("第二段解密："+new String(payload));
//
//        // 测试6: 这是一个被篡改的token，因此会报异常，说明JWT是安全的
//        jwtOperator.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE1NjU1ODk3MzIsImV4cCI6MTU2Njc5OTMzMn0.nDv25ex7XuTlmXgNzGX46LqMZItVFyNHQpmL9UQf-aUxxx");
//    }
}