package com.meizhi;

import com.meizhi.common.util.JwtUtil;
import com.meizhi.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 测试生成jwt
     */
    @Test
    public void testCreateJWT(){
        User user = new User();
        user.setId(1);
        user.setUsername("许强");
        user.setPassword("xq85782621");
        user.setEmail("85782621@qq.com");
        String jwt = jwtUtil.createJWT(user);
        System.err.println(jwt);
    }



}
