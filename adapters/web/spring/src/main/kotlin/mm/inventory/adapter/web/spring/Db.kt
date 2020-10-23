package mm.inventory.adapter.web.spring

import io.r2dbc.client.R2dbc
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Db {

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