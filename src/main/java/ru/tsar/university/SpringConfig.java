package ru.tsar.university;

import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


@Configuration
@ComponentScan("ru.tsar.university")
@PropertySource("classpath:config.properties")
public class SpringConfig {

	@Value("${driver}")
	public String driver;
	@Value("${url}")
	public String url;
	@Value("${login}")
	public String login;
	@Value("${password}")
	public String password;
	
	

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource)
			throws IllegalAccessException, InvocationTargetException, InstantiationException {

		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();	
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(login);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	  public DatabasePopulator databasePopulator(DataSource dataSource) {
	    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	    populator.addScripts(new ClassPathResource("/schema.sql"));
	    DatabasePopulatorUtils.execute(populator,  dataSource);
	    return populator;
	}
	
}
