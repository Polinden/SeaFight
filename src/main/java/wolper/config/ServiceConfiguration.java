package wolper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import wolper.logic.*;
import javax.sql.DataSource;
import org.springframework.core.env.Environment;

@Configuration
@EnableTransactionManagement
@EnableAsync
@ComponentScan(value = "wolper.logic")
@PropertySource("classpath:app.properties")

public class ServiceConfiguration {



    @Autowired
    Environment env;


    // Настройка загрухзки файла со свойствами
    @Bean
    public
    static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    //Настройка соединений с базой данных
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();



        String host = env.getProperty("databeseHost");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(host);
        dataSource.setUsername(env.getProperty("databaseUsername"));
        dataSource.setPassword(env.getProperty("databasePassword"));


        return dataSource;
    }


    //Настройка ДАО для регистрационных данных игрока
    @Bean GamerDAO gamerDAO() {
        GamerDAO gamerDAO= new GamerDAO();
        gamerDAO.setDataSource(dataSource());
        return gamerDAO;
    }


    //Поддержка транзакций для JDBC
    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }


    // Тестовая конфигурация

//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.DERBY)
//                .build();
//    }

}
