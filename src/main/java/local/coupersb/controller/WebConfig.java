package local.coupersb.controller;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer 
{
	
	@Value("${spa-app.url}")
	private String spaClienUrl;
	
	/**
	 * Allowed credentials for session based requests
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) 
    {
        registry.addMapping("/**")
        	.allowedOrigins(spaClienUrl)
        	.allowedMethods("*")
        	.allowCredentials(true);
    }
    
    /**
     * Resource locator for swagger
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
    {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    @Bean
    public CookieSerializer cookieSerializer()
    {
    	DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
    	defaultCookieSerializer.setSameSite(SameSiteCookies.NONE.getValue());
    	defaultCookieSerializer.setUseSecureCookie(true);
    	return defaultCookieSerializer;
    }
    
    @Bean
    public SpringHttpSessionConfiguration configuration()
    {
    	SpringHttpSessionConfiguration configuration = new SpringHttpSessionConfiguration();
    	configuration.setCookieSerializer(cookieSerializer());
    	return configuration;
    }
    
}
