package com.our.socialseed.about.entry.rest.controller;

import com.our.socialseed.about.application.usecase.AboutUseCases;
import com.our.socialseed.about.entry.rest.dto.AboutResponseDTO;
import com.our.socialseed.about.entry.rest.mapper.AboutRestMapper;
import com.our.socialseed.shared.response.ApiResponse;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.entry.rest.dto.UserResponseDTO;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/about")
public class AboutController {

    private final AboutUseCases aboutUseCases;
    private final MessageSource messageSource;

    public AboutController(AboutUseCases aboutUseCases, MessageSource messageSource) {
        this.aboutUseCases = aboutUseCases;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AboutResponseDTO>> getAboutInfo() {
        Locale locale = LocaleContextHolder.getLocale();

        return aboutUseCases.getAbout().execute()
                .map(AboutRestMapper::toResponse)
                .map(responseDto -> ResponseEntity.ok(
                        ApiResponse.success(
                                responseDto,
                                messageSource.getMessage("about.success", null, locale))
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound(
                                messageSource.getMessage("about.notfound", null, locale)
                        )));
    }
}