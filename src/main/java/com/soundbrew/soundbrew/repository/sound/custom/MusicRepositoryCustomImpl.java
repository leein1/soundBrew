package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class MusicRepositoryCustomImpl implements MusicRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QAlbum album = QAlbum.album;
    private final QAlbumMusic albumMusic = QAlbumMusic.albumMusic;
    private final QMusic music = QMusic.music;
    private final QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
    private final QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
    private final QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
    private final QMoodTag moodTag = QMoodTag.moodTag;
    private final QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
    private final QGenreTag genreTag = QGenreTag.genreTag;

    @Override
    public Optional<List<SearchTotalResultDTO>> getAllTags(RequestDTO requestDTO){
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        if(requestDTO.getMore()!= null && !requestDTO.getMore().isEmpty()){
            for(Map.Entry<String, String> entry : requestDTO.getMore().entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key.toLowerCase()){
                    case "instrument": instruments = Arrays.asList(value.split(","));break;
                    case "genre": genres = Arrays.asList(value.split(","));break;
                    case "mood": moods = Arrays.asList(value.split(","));break;
                }
            }
        }

        BooleanBuilder havingBuilder = new BooleanBuilder();
        for(String instrument : instruments) {
            havingBuilder.and(Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName).contains(instrument));
        }
        for(String genre: genres){
            havingBuilder.and(Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName).contains(genre));
        }
        for(String mood : moods){
            havingBuilder.and(Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName).contains(mood));
        }

        List<SearchTotalResultDTO> result = queryFactory.select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(TagsStreamDTO.class,
                                Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName).as("instrumentTagName"),
                                Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName).as("moodTagName"),
                                Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName).as("genreTagName")
                        ).as("tagsStreamDTO")
                ))
                .from(music)
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .groupBy(music.musicId) // Add GROUP BY clause
                .having(havingBuilder) // Now the HAVING clause can be used correctly
                .fetch();


        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SearchTotalResultDTO> soundOne(String nickname, String title) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(music.nickname.eq(nickname));
        builder.and(music.title.eq(title));

        SearchTotalResultDTO result = queryFactory.select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumArtPath
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.nickname, music.createDate, music.modifyDate
                        ).as("musicDTO"),
                        Projections.bean(TagsStreamDTO.class,
                            Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName).as("instrumentTagName"),
                            Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName).as("moodTagName"),
                            Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName).as("genreTagName")
                        ).as("tagsStreamDTO")
                ))
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder) // 조건 추가
                .groupBy(
                        album.albumArtPath,
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        music.nickname, music.createDate, music.modifyDate, music.download
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SearchTotalResultDTO> soundOne(int userId, int musicId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(music.userId.eq(userId));
        builder.and(music.musicId.eq(musicId));

        SearchTotalResultDTO result = queryFactory
                .select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumArtPath
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.nickname, music.createDate, music.modifyDate
                        ).as("musicDTO"),
                        Projections.bean(TagsStreamDTO.class,
                                Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName).as("instrumentTagName"),
                                Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName).as("moodTagName"),
                                Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName).as("genreTagName")
                        ).as("tagsStreamDTO")
                ))
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder) // 조건 추가
                .groupBy(
                        album.albumArtPath,
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        music.nickname, music.createDate, music.modifyDate, music.download
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }
}
