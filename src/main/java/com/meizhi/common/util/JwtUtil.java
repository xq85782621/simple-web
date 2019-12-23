package com.meizhi.common.util;

import com.meizhi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    // 生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
    private  String KEY  ;
    // 过期时间
    private long TTL ;

    /**
     * 用户登录成功后生成Jwt
     *
     * @param user      登录成功的user对象
     * @return
     */
    public  String createJWT( User user) {

        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());

        // 存入用户的权限,实际环境应该从数据库查询
        String apis = "user-info , user-add ";
        claims.put("apis", apis);

        //签发人
        String subject = user.getUsername();

        // 下面就是在为payload添加各种标准声明和私有声明了
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的 '签名算法' 和签名使用的 '秘钥'
                .signWith(signatureAlgorithm, KEY);
        if (TTL >= 0) {
            long expMillis = nowMillis + TTL;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();

    }

    /**
     * token 过期超过1个小时,根据旧的token生成新token
     * @return
     */
    public String getNewToken(Claims claims){
        // 从旧的 token 中的 claims 中获取用户id,然后查到 user 对象, 根据新的user 对象生成新的token
        // 这里只是模拟,实际要从数据库中查询
        Integer userId = (Integer) claims.get("id");
        User user = new User();
        user.setId(userId);
        user.setPassword("xq85782621");
        user.setPhone("18656831100");
        user.setUsername("许强");
        user.setEmail("85782621@qq.com");
        String newJwt = createJWT(user);
        return newJwt;
    }


    /**
     * Token的解密
     * @param token 加密后的token
     * @return
     */
    public  Claims parseJWT(String token) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(KEY)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }



}
