package wolper.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.spring4.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring4.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import java.io.IOException;



@Configuration
@ComponentScan("wolper.controller")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {



	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}



	// Настройка Таймлиф
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new AjaxThymeleafViewResolver();
		resolver.setViewClass(FlowAjaxThymeleafView.class);
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("utf-8");
		return resolver;
	}
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new SpringSecurityDialect());
		return engine;
	}
	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}


	//Настройка резолва по имени класса для РЕСТ сервиса
	@Bean
	public ViewResolver beanNameViewResolver() {
		return new BeanNameViewResolver();
	}


	// Настройка загрузки статических ресурсов
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//JSON конвертер
	@Bean
	MappingJackson2HttpMessageConverter converter() {
		return new MappingJackson2HttpMessageConverter();
	}


	// Настройка аплоада файлов
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
		return new StandardServletMultipartResolver();
	}

}
