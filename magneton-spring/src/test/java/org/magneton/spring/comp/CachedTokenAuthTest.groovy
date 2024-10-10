package org.magneton.spring.comp

import org.magneton.enhance.auth.MTokenAuth
import org.magneton.spring.TestApplication
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.annotation.Resource

/**
 *
 * @author zhangmsh
 * @since
 */
@SpringBootTest(classes = TestApplication)
class CachedTokenAuthTest extends Specification {

    @Resource
    private MTokenAuth tokenAuth;

    def "test"() {
        given:
        def user = new User(1, "zms");

        when:
        def token = tokenAuth.login(user);
        println "token: $token";
        then:
        tokenAuth.isLogin(token)
        tokenAuth.getLogin(token, User) == user

        cleanup:
        tokenAuth.logout(token)
    }


}
