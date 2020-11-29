package mm.inventory.adapter.web.webflux.db

import io.r2dbc.client.R2dbc
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class R2dbcConfiguration {

    @Bean
    fun connectionFactory() = PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                    .host("localhost")
                    .port(5432)
                    .username("inventory")
                    .password("inventory")
                    .build())

    @Bean
    fun r2dbc() = R2dbc(connectionFactory())

}