package py.org.fundacionparaguaya.pspserver.config;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
/**
 *
 * @author mgonzalez
 *
 */

public class I18n implements MessageInterpolator {

    private MessageSource messageSource;

    public I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(final String key, final Object... params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(final String messageTemplate,
            final Context context) {
        return messageSource.getMessage(messageTemplate, null,
                LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(final String messageTemplate,
            final Context context, final Locale locale) {
        return messageSource.getMessage(messageTemplate, null, locale);
    }
}
