package ru.tsar.university;

import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.jdbc.core.JdbcTemplate;

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
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driver);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(login);
		dataSourceBuilder.password(password);
		return dataSourceBuilder.build();
	}

}
