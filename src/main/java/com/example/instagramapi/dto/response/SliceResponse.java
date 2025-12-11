package com.example.instagramapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Builder
public class SliceResponse<T> {
    private List<T> content;
    private boolean hasNext;
    private int page;
    private int size;

    public static <T> SliceResponse<T> from(Slice<?> slice, List<T> content) {
        return SliceResponse.<T>builder()
                .content(content)
                .hasNext(slice.hasNext())
                .size(slice.getNumber())
                .page(slice.getSize())
                .build();
    }
}
