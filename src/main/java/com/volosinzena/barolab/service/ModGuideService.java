package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.ModGuide;

import java.util.List;
import java.util.UUID;

public interface ModGuideService {

    List<ModGuide> getGuidesByModId(Long modId);

    ModGuide getGuideById(UUID guideId);

    ModGuide createGuide(Long modId, String title, String content);

    ModGuide updateGuide(UUID guideId, String title, String content);

    List<ModGuide> getAllGuides();

    ModGuide activateGuide(UUID guideId);

    ModGuide blockGuide(UUID guideId);

    void deleteGuide(UUID guideId);
}
