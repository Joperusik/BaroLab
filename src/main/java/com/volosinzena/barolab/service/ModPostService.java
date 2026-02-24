package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.ModPost;

import java.util.List;

public interface ModPostService {
    List<ModPost> getAllMods();

    ModPost getModByExternalId(Long externalId);

    ModPost createModPost(String title, String content, String externalUrl);

    ModPost activateMod(Long externalId);

    ModPost blockMod(Long externalId);
}
