package ru.tsar.university;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSourceCreator {

	@Value("${config.driver}")
	private String driver;
	@Value("${config.url}")
	private String url;
	@Value("${config.login}")
	private String login;
	@Value("${config.password}")
	private String password;

	public JdbcTemplate getJdbcTemplate() {
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
