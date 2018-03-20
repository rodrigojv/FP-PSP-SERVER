package py.org.fundacionparaguaya.pspserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class PspServerApplication extends SpringBootServletInitializer {

    private static final String DEFAULT_LANGUAGE = "es";
    private static final String DEFAULT_COUNTRY = "PY";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PspServerApplication.class);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY));
        return slr;
    }

    public static void main(String[] args) {

        SpringApplication.run(PspServerApplication.class, args);
    }

}