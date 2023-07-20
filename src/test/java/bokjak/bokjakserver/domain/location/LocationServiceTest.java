package bokjak.bokjakserver.domain.location;

import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.location.dto.LocationDto;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import bokjak.bokjakserver.domain.location.service.LocationService;
import bokjak.bokjakserver.domain.user.UserTemplate;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {// BDD
    @Mock
    UserService userService;

    @Mock
    LocationRepository locationRepository;

    @InjectMocks
    LocationService locationService;

    static User mockUser = UserTemplate.makeDummyUserA();
    static Location mockLocation = LocationMockUtils.makeDummyLocation();

    @Test
    @DisplayName("북마크 - 등록")
    public void bookmark_enroll() {
        // given
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(locationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockLocation));

        // when
        LocationDto.BookmarkResponse response = locationService.bookmark(1L, 1L);

        // then
        Assertions.assertThat(response.isBookmarked()).isTrue();
    }

    @Test
    @DisplayName("북마크 - 취소")
    public void bookmark_cancel() {
        // given
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(locationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockLocation));

        mockUser.addLocationBookmark(LocationBookmark.builder() // 북마크 데이터 삽입
                .user(mockUser)
                .location(mockLocation)
                .build());

        // when
        LocationDto.BookmarkResponse response = locationService.bookmark(1L, 1L);

        // then
        Assertions.assertThat(response.isBookmarked()).isFalse();
    }
}
