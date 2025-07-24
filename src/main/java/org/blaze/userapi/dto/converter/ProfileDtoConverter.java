package org.blaze.userapi.dto.converter;

import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.model.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProfileDtoConverter {

    public ProfileDto convertFrom (Profile profile) {
        return new ProfileDto(
                Objects.requireNonNull(profile.getId()),
                Objects.requireNonNull(profile.getFullName()),
                Objects.requireNonNull(Objects.requireNonNull(profile.getUser()).getEmail()),
                Objects.requireNonNull(profile.getUser().getId()),
                profile.getProfilePhoto(),
                profile.getBio(),
                profile.getBirthDate(),
                profile.getUserStatus(),
                profile.getLastSeen()
        );
    }

    public List<ProfileDto> convertFrom (List<Profile> profile) {
        return profile.stream().map(this::convertFrom).collect(Collectors.toList());
    }
}
