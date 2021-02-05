package ru.tsar.university;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConnectionProvider {

	public String url;
	public String login;
	public String password;
	public String driver;

	public ConnectionProvider() throws IOException {
		String config = getClass().getClassLoader().getResource("config.properties").getPath().substring(1);
		FileInputStream fileInputStream = new FileInputStream(config);
		Properties properties = new Properties();
		properties.load(fileInputStream);
		url = properties.getProperty("url");
		login = properties.getProperty("login");
		password = properties.getProperty("password");
		driver = properties.getProperty("driver");
	}

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
