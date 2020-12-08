package ru.tsar.university;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.Id;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
	// throws IllegalAccessException, InvocationTargetException,
	// InstantiationException

	@Bean
	public JdbcTemplate jdbcTemplate()
			throws IllegalAccessException, InvocationTargetException, InstantiationException {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

		dataSourceBuilder.driverClassName(driver);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(login);
		dataSourceBuilder.password(password);
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSourceBuilder.build());
		return jdbcTemplate;
	}

}
