package local.coupersb.controller;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
    @Profile("prod") // Set cookies same-site attribute to None in production only
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                Rfc6265CookieProcessor rfc6265CookieProcessor = new Rfc6265CookieProcessor();
                rfc6265CookieProcessor.setSameSiteCookies("None");
                context.setCookieProcessor(rfc6265CookieProcessor);
            }
        };
    }
    
}
