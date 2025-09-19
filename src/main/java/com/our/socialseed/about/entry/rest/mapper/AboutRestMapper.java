package com.our.socialseed.about.entry.rest.mapper;

import com.our.socialseed.about.domain.model.About;
import com.our.socialseed.about.entry.rest.dto.AboutResponseDTO;

public class AboutRestMapper {
    public AboutRestMapper() {    }

    public static AboutResponseDTO toResponse(About about) {
        return new AboutResponseDTO(
                about.getVersion(),
                about.getSeed(),
                about.getName(),
                about.getIcono_url(),
                about.getDescription()
        );
    }
}
