package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.ModGuideRequest;
import com.volosinzena.barolab.controller.dto.ModGuideResponse;
import com.volosinzena.barolab.mapper.ModGuideMapper;
import com.volosinzena.barolab.service.ModGuideService;
import com.volosinzena.barolab.service.model.ModGuide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ModGuideController {

    private final ModGuideService modGuideService;
    private final ModGuideMapper modGuideMapper;

    @GetMapping("/mod/{modId}/guide")
    public ResponseEntity<List<ModGuideResponse>> getGuides(@PathVariable Long modId) {
        log.info("REST request to get guides for modId: {}", modId);
        List<ModGuide> guides = modGuideService.getGuidesByModId(modId);
        return ResponseEntity.ok(guides.stream().map(modGuideMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/mod/{modId}/guide/{guideId}")
    public ResponseEntity<ModGuideResponse> getGuideById(@PathVariable Long modId, @PathVariable UUID guideId) {
        log.info("REST request to get guide for modId: {}, guideId: {}", modId, guideId);
        ModGuide modGuide = modGuideService.getGuideById(guideId);
        return ResponseEntity.ok(modGuideMapper.toDto(modGuide));
    }

    @PostMapping("/mod/{modId}/guide")
    public ResponseEntity<ModGuideResponse> createGuide(
            @PathVariable Long modId,
            @Valid @RequestBody ModGuideRequest request) {
        log.info("REST request to create guide for modId: {}", modId);
        ModGuide modGuide = modGuideService.createGuide(modId, request.getTitle(), request.getContent());
        return ResponseEntity.ok(modGuideMapper.toDto(modGuide));
    }

    @PutMapping("/mod/{modId}/guide/{guideId}")
    public ResponseEntity<ModGuideResponse> updateGuide(
            @PathVariable Long modId,
            @PathVariable UUID guideId,
            @Valid @RequestBody ModGuideRequest request) {
        log.info("REST request to update guide for modId: {}, guideId: {}", modId, guideId);
        ModGuide modGuide = modGuideService.updateGuide(guideId, request.getTitle(), request.getContent());
        return ResponseEntity.ok(modGuideMapper.toDto(modGuide));
    }

    @DeleteMapping("/mod/{modId}/guide/{guideId}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long modId, @PathVariable UUID guideId) {
        log.info("REST request to delete guide for modId: {}, guideId: {}", modId, guideId);
        modGuideService.deleteGuide(guideId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guides")
    public ResponseEntity<List<ModGuideResponse>> getAllGuides() {
        log.info("REST request to get all guides (admin)");
        List<ModGuide> guides = modGuideService.getAllGuides();
        return ResponseEntity.ok(guides.stream().map(modGuideMapper::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/guides/{guideId}/activate")
    public ResponseEntity<ModGuideResponse> activateGuide(@PathVariable UUID guideId) {
        log.info("REST request to activate guide: {}", guideId);
        ModGuide modGuide = modGuideService.activateGuide(guideId);
        return ResponseEntity.ok(modGuideMapper.toDto(modGuide));
    }

    @PutMapping("/guides/{guideId}/block")
    public ResponseEntity<ModGuideResponse> blockGuide(@PathVariable UUID guideId) {
        log.info("REST request to block guide: {}", guideId);
        ModGuide modGuide = modGuideService.blockGuide(guideId);
        return ResponseEntity.ok(modGuideMapper.toDto(modGuide));
    }
}
