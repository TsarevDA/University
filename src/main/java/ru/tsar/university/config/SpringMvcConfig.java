package ru.tsar.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan("ru.tsar.university")
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {

		@Autowired
		private SpringTemplateEngine templateEngine;
		
		@Bean
		public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext) {
			SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
			templateResolver.setApplicationContext(applicationContext);
			templateResolver.setPrefix("/WEB-INF/views/");
			templateResolver.setSuffix(".html");
			return templateResolver;
		}
		
		@Bean
		public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
			SpringTemplateEngine templateEngine = new SpringTemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);
			templateEngine.addDialect(new Java8TimeDialect());
			templateEngine.setEnableSpringELCompiler(true);
			return templateEngine;
		}
	
		@Override
		public void configureViewResolvers(ViewResolverRegistry registry) {
			ThymeleafViewResolver resolver = new ThymeleafViewResolver();
			resolver.setTemplateEngine(templateEngine);
			registry.viewResolver(resolver);
		}
}
