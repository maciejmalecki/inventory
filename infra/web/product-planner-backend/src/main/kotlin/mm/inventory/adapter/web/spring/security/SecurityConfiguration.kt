package mm.inventory.adapter.web.spring.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfiguration {
    @Bean
    fun securityGuard() = SecurityGuardImpl()
}