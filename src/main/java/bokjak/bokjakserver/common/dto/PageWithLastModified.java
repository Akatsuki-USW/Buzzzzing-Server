package bokjak.bokjakserver.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
public class PageWithLastModified<T> {
    PageResponse<T> pageResponse;
    LocalDateTime lastModified;

    public static <T> PageWithLastModified<T> of(Page<T> page, LocalDateTime lastModified) {
        return PageWithLastModified.<T>builder()
                .pageResponse(PageResponse.of(page))
                .lastModified(lastModified)
                .build();
    }
}
