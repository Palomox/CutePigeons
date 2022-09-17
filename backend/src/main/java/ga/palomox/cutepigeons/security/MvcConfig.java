package ga.palomox.cutepigeons.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new OryMiddleware());
	}
}
