package com.soundbrew.soundbrew.dto.sound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.soundbrew.soundbrew.dto.BaseEntityDTO;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchTotalResultDTO extends BaseEntityDTO {
    private AlbumDTO albumDTO;
    private MusicDTO musicDTO;
    private TagsStreamDTO tagsStreamDTO;


}
