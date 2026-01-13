package com.socialseed.apiresponse.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageResolver {

    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolve(String key, Object... params) {
        return messageSource.getMessage(
                key,
                params,
                key, // fallback seguro
                LocaleContextHolder.getLocale()
        );
    }
}
