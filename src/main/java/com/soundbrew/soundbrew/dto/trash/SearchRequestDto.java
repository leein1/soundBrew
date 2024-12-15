package com.soundbrew.soundbrew.dto.trash;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Setter
public class SearchRequestDto  {
    private List<String> instrument;
    private List<String> mood;
    private List<String> genre;
}
