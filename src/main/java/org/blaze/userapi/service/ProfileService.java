package org.blaze.userapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.dto.converter.ProfileDtoConverter;
import org.blaze.userapi.dto.request.ProfileCreateRequest;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.model.Status;
import org.blaze.userapi.model.User;
import org.blaze.userapi.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final ProfileDtoConverter profileDtoConverter;

    public ProfileService(ProfileRepository profileRepository, UserService userService, ProfileDtoConverter profileDtoConverter) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.profileDtoConverter = profileDtoConverter;
    }


    public ProfileDto createProfile(ProfileCreateRequest profileCreateRequest) {

        User user = userService.getUserByToken();

        Profile profile = new Profile(
                null,
                user,
                profileCreateRequest.getFullName(),
                profileCreateRequest.getProfilePhoto(),
                profileCreateRequest.getBio(),
                profileCreateRequest.getBirthDate(),
                Status.AVAILABLE,
                Instant.now(),
                new ArrayList<Friend>(),
                new ArrayList<Friend>()
        );

        return profileDtoConverter.convertFrom(profileRepository.save(profile));

    }

    public List<ProfileDto> searchProfile(String username) {
        List<Profile> profileList = profileRepository.findByFullNameStartingWithIgnoreCase(username);
        assert profileList != null;
        if(profileList.isEmpty()){
            throw new EntityNotFoundException("Profile not found");
        }
        return profileDtoConverter.convertFrom(profileList);
    }

    public ProfileDto findProfileById(UUID id) {
        return profileDtoConverter.convertFrom(getProfileById(id));
    }

    public ProfileDto findProfileByUserId() {
        return profileDtoConverter.convertFrom(getMyProfile());
    }


    ///************ Helper Methods to use also in the other classes *************//
    protected Profile getMyProfile() {
        User user = userService.getUserByToken();
        return profileRepository.findByUserId(Objects.requireNonNull(user.getId()));
    }

    protected Profile getProfileById(UUID id) {
        return profileRepository.findById(id).orElseThrow(() -> new CustomException("Profile not found exception with id : "+id));
    }
}
