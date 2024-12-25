package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsStreamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

@RequiredArgsConstructor
public class AlbumMusicRepositoryCustomImpl implements AlbumMusicRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Page<SearchTotalResultDTO>> search(RequestDTO requestDTO){
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        QAlbum album = QAlbum.album;
        QAlbumMusic albumMusic = QAlbumMusic.albumMusic;
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

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

        BooleanBuilder builder = new BooleanBuilder();
        if(requestDTO.getType()!= null) {
            List<String> types = List.of(requestDTO.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t": builder.and(music.title.contains(requestDTO.getKeyword()));break;
                    case "n": builder.and(music.nickname.contains(requestDTO.getKeyword())); break;
                }
            }
        }

        List<SearchTotalResultDTO> results = queryFactory.select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                            album.albumId,
                            album.albumName,
                            album.albumArtPath,
                            album.description,
                            album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                            music.musicId,
                            music.title,
                            music.filePath,
                            music.price,
                            music.description,
                            music.createDate,
                            music.modifyDate
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
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
                )
                .having(havingBuilder)
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), results.size());

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> searchAlbum(RequestDTO requestDTO){
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        QAlbum album = QAlbum.album;
        QAlbumMusic albumMusic = QAlbumMusic.albumMusic;
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

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

        BooleanBuilder builder = new BooleanBuilder();
        if(requestDTO.getType()!= null) {
            List<String> types = List.of(requestDTO.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t": builder.and(album.albumName.contains(requestDTO.getKeyword()));break;
                    case "n": builder.and(album.nickname.contains(requestDTO.getKeyword())); break;
                }
            }
        }

        // 쿼리 생성
        List<SearchTotalResultDTO> results = queryFactory
                .select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumId,
                                album.albumName,
                                album.albumArtPath,
                                album.description,
                                album.nickname,
                                album.createDate,
                                album.modifyDate
                        ).as("albumDTO"),
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
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,album.createDate, album.modifyDate
                )
                .having(havingBuilder)
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), results.size());

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> albumOne(String nickname, String albumName, RequestDTO requestDTO) {
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        QAlbum album = QAlbum.album;
        QAlbumMusic albumMusic = QAlbumMusic.albumMusic;
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(album.albumName.eq(albumName));
        builder.and(album.nickname.eq(nickname));

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

        List<SearchTotalResultDTO> results = queryFactory
                .select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumId,
                                album.albumName,
                                album.albumArtPath,
                                album.description,
                                album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId,
                                music.title,
                                music.filePath,
                                music.price,
                                music.description,
                                music.createDate,
                                music.modifyDate
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
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
                )
                .having(havingBuilder)
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), results.size());

        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> albumOne(int userId, int id, RequestDTO requestDTO) {
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        QAlbum album = QAlbum.album;
        QAlbumMusic albumMusic = QAlbumMusic.albumMusic;
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

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

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(album.albumId.eq(id));
        builder.and(album.userId.eq(userId));

        List<SearchTotalResultDTO> results = queryFactory
                .select(Projections.bean(
                        SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumId,
                                album.albumName,
                                album.albumArtPath,
                                album.description,
                                album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId,
                                music.title,
                                music.filePath,
                                music.price,
                                music.description,
                                music.createDate,
                                music.modifyDate
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
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
                )
                .having(havingBuilder)
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), results.size());

        return Optional.of(pageResult);
    }

}
