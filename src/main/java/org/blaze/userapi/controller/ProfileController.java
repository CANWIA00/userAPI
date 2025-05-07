package org.blaze.userapi.controller;


import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.dto.request.ProfileCreateRequest;
import org.blaze.userapi.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileCreateRequest profileCreateRequest){
        return ResponseEntity.ok(profileService.createProfile(profileCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable UUID id){
        return ResponseEntity.ok(profileService.findProfileById(id));
    }


    @GetMapping("/search/{fullName}")
    public ResponseEntity<List<ProfileDto>> searchProfile(@PathVariable String fullName){
        return ResponseEntity.ok(profileService.searchProfile(fullName));
    }

    // my profile
    @GetMapping()
    public ResponseEntity<ProfileDto> getProfileByUserId(){
        return ResponseEntity.ok(profileService.findProfileByUserId());
    }



}
