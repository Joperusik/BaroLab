package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.CreateModPostDto;
import com.volosinzena.barolab.controller.dto.ModPostDto;
import com.volosinzena.barolab.mapper.ModPostMapper;
import com.volosinzena.barolab.service.ModPostService;
import com.volosinzena.barolab.service.ModTransitionService;
import com.volosinzena.barolab.service.model.ModPost;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
public class ModPostController {

    private static final String ANON_COOKIE_NAME = "anon_id";
    private static final int ANON_COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

    private final ModPostService modPostService;
    private final ModTransitionService modTransitionService;
    private final ModPostMapper modPostMapper;

    @Autowired
    public ModPostController(ModPostService modPostService, ModTransitionService modTransitionService,
            ModPostMapper modPostMapper) {
        this.modPostService = modPostService;
        this.modTransitionService = modTransitionService;
        this.modPostMapper = modPostMapper;
    }

    @PostMapping("/mods")
    public ResponseEntity<ModPostDto> createMod(@RequestBody CreateModPostDto createModPostDto) {
        ModPost modPost = modPostService.createModPost(
                createModPostDto.getTitle(),
                createModPostDto.getContent(),
                createModPostDto.getExternalUrl()
        );
        ModPostDto responseDto = modPostMapper.toDto(modPost);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/mods")
    public ResponseEntity<List<ModPostDto>> getMods() {
        List<ModPost> mods = modPostService.getAllMods();
        List<ModPostDto> dtoList = mods.stream().map(modPostMapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/mod/{externalId}")
    public ResponseEntity<ModPostDto> getMod(@PathVariable Long externalId) {
        ModPost modPost = modPostService.getModByExternalId(externalId);
        ModPostDto responseDto = modPostMapper.toDto(modPost);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/mod/{externalId}/transition")
    public ResponseEntity<Void> transition(@PathVariable Long externalId, HttpServletRequest request,
            HttpServletResponse response) {
        String subjectKey = buildSubjectKey(request, response);
        modTransitionService.registerClick(externalId, subjectKey);
        String redirectUrl = "https://steamcommunity.com/sharedfiles/filedetails/?id=" + externalId;
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @PutMapping("/mod/{externalId}/activate")
    public ResponseEntity<ModPostDto> activateMod(@PathVariable Long externalId) {
        ModPost modPost = modPostService.activateMod(externalId);
        ModPostDto responseDto = modPostMapper.toDto(modPost);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/mod/{externalId}/block")
    public ResponseEntity<ModPostDto> blockMod(@PathVariable Long externalId) {
        ModPost modPost = modPostService.blockMod(externalId);
        ModPostDto responseDto = modPostMapper.toDto(modPost);
        return ResponseEntity.ok(responseDto);
    }

    private String buildSubjectKey(HttpServletRequest request, HttpServletResponse response) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication != null ? authentication.getName() : null;
        if (currentUserId != null && !currentUserId.equals("anonymousUser")) {
            return "user:" + currentUserId;
        }

        String anonId = getOrCreateAnonId(request, response);
        String ip = request.getRemoteAddr() == null ? "" : request.getRemoteAddr();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        if (userAgent == null) {
            userAgent = "";
        }
        String raw = ip + "|" + userAgent + "|" + anonId;
        return "anon:" + sha256Hex(raw);
    }

    private String getOrCreateAnonId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ANON_COOKIE_NAME.equals(cookie.getName()) && cookie.getValue() != null
                        && !cookie.getValue().isBlank()) {
                    return cookie.getValue();
                }
            }
        }
        String anonId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(ANON_COOKIE_NAME, anonId);
        cookie.setPath("/");
        cookie.setMaxAge(ANON_COOKIE_MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return anonId;
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
