import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.bee.redisflag.core.GlobalInterceptor;
import com.bee.redisflag.core.SystemDestroyer;
import com.bee.redisflag.core.SystemInitializer;

/**
 * @author weiwei 系统启动入口，Spring容器初始化入口
 */
@Configuration
@EnableWebMvc
@PropertySource({ "classpath:app.properties" })
@ComponentScan(basePackages = { "com.bee.redisflag" })
public class BootStrap implements WebApplicationInitializer {
	private Log logger = LogFactory.getLog(getClass());

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(BootStrap.class);
		rootContext.registerShutdownHook();
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext)).addMapping("/");
		servletContext.addListener(new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {

			}
		});
	}

	// mvc相关配置
	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebMvcConfigurerAdapter() {
			// Json视图解析器，使用FastJson替换spring默认的Jackson
			@Override
			public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
				converters.add(new FastJsonHttpMessageConverter());
			}

			// 拦截器配置
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
			}

			// 启用servlet内置handler处理机制(spring未配置的请求会forward至servlet，用于jsp等)
			@Override
			public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
				configurer.enable();
			}

		};
	}

	// velocity视图解析器配置，使用支持布局的LayoutViewResolver
	@Bean
	public VelocityLayoutViewResolver velocityLayoutViewResolver() {
		VelocityLayoutViewResolver viewResolver = new VelocityLayoutViewResolver();
		viewResolver.setLayoutUrl("layout/default.vm");
		viewResolver.setPrefix("page/");
		viewResolver.setSuffix(".vm");
		viewResolver.setDateToolAttribute("datetool");
		viewResolver.setNumberToolAttribute("numbertool");
		viewResolver.setContentType("text/html;charset=UTF-8");
		return viewResolver;
	}

	@Bean
	public VelocityConfigurer VelocityConfigurer() {
		VelocityConfigurer configurer = new VelocityConfigurer();
		configurer.setResourceLoaderPath("/WEB-INF/templates");
		Properties properties = new Properties();
		properties.put(Velocity.VM_LIBRARY, "velocity/macro.vm");
		properties.put(Velocity.ENCODING_DEFAULT, "UTF-8");
		properties.put(Velocity.INPUT_ENCODING, "UTF-8");
		properties.put(Velocity.OUTPUT_ENCODING, "UTF-8");
		configurer.setVelocityProperties(properties);
		return configurer;
	}

	// 系统启动监听器，用于系统启动完成后的初始化操作
	@Bean
	public ApplicationListener<ContextRefreshedEvent> ContextRefreshedEvent() {
		return new ApplicationListener<ContextRefreshedEvent>() {
			@Override
			public void onApplicationEvent(ContextRefreshedEvent event) {
				logger.info("Spring ContextRefreshed!");
				SystemInitializer systemInitializer = new SystemInitializer(event.getApplicationContext());
				try {
					systemInitializer.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	// 系统终止监听器，用于系统关闭后的操作
	@Bean
	public ApplicationListener<ContextClosedEvent> ContextClosedEvent() {
		return new ApplicationListener<ContextClosedEvent>() {
			@Override
			public void onApplicationEvent(ContextClosedEvent event) {
				logger.info("Spring ContextClosed!");
				SystemDestroyer systemDestroyer = new SystemDestroyer(event.getApplicationContext());
				try {
					systemDestroyer.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
}