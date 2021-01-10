package mm.inventory.adapter.web.spring.conf.jdbi

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource

@Configuration
class JdbiConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource() = DriverManagerDataSource()

    @Bean
    fun dataSourceTransactionManager() =
        DataSourceTransactionManager(dataSource())

    @Bean
    fun jdbi(): Jdbi = Jdbi.create(dataSource())
        .installPlugin(SqlObjectPlugin())
        .installPlugin(PostgresPlugin())
        .installPlugin(KotlinPlugin())
        .installPlugin(KotlinSqlObjectPlugin())
}