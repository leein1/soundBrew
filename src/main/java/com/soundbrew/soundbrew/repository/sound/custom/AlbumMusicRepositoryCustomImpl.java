package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

@RequiredArgsConstructor
public class AlbumMusicRepositoryCustomImpl implements AlbumMusicRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Page<SearchTotalResultDto>> search(RequestDto requestDto){
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

        if(requestDto.getMore()!= null && !requestDto.getMore().isEmpty()){
            for(Map.Entry<String, String> entry : requestDto.getMore().entrySet()){
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
        if(requestDto.getType()!= null) {
            List<String> types = List.of(requestDto.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t": builder.and(music.title.contains(requestDto.getKeyword()));break;
                    case "n": builder.and(music.nickname.contains(requestDto.getKeyword())); break;
                }
            }
        }

        List<SearchTotalResultDto> results = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        music.create_date,
                        music.modify_date
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
                        music.musicId, music.title, music.filePath, music.price, music.description, music.create_date, music.modify_date
                )
                .having(havingBuilder)
                .offset(requestDto.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDto.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDto> pageResult = new PageImpl<>(results, requestDto.getPageable(), results.size());

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDto>> searchAlbum(RequestDto requestDto){
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

        if(requestDto.getMore()!= null && !requestDto.getMore().isEmpty()){
            for(Map.Entry<String, String> entry : requestDto.getMore().entrySet()){
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
        if(requestDto.getType()!= null) {
            List<String> types = List.of(requestDto.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t": builder.and(album.albumName.contains(requestDto.getKeyword()));break;
                    case "n": builder.and(album.nickname.contains(requestDto.getKeyword())); break;
                }
            }
        }

        // 쿼리 생성
        List<SearchTotalResultDto> results = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        album.create_date,
                        album.modify_date
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
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,album.create_date, album.modify_date
                )
                .having(havingBuilder)
                .offset(requestDto.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDto.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDto> pageResult = new PageImpl<>(results, requestDto.getPageable(), results.size());

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDto>> albumOne(String nickname, String albumName,RequestDto requestDto) {
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

        if(requestDto.getMore()!= null && !requestDto.getMore().isEmpty()){
            for(Map.Entry<String, String> entry : requestDto.getMore().entrySet()){
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

        List<SearchTotalResultDto> results = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        music.create_date,
                        music.modify_date
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
                        music.musicId, music.title, music.filePath, music.price, music.description, music.create_date, music.modify_date
                )
                .having(havingBuilder)
                .offset(requestDto.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDto.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        Page<SearchTotalResultDto> pageResult = new PageImpl<>(results, requestDto.getPageable(), results.size());

        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDto>> albumOne(int userId, int id,RequestDto requestDto) {
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

        if(requestDto.getMore()!= null && !requestDto.getMore().isEmpty()){
            for(Map.Entry<String, String> entry : requestDto.getMore().entrySet()){
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

        List<SearchTotalResultDto> results = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname,
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        music.create_date,
                        music.modify_date
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
                        music.musicId, music.title, music.filePath, music.price, music.description, music.create_date, music.modify_date
                )
                .having(havingBuilder)
                .offset(requestDto.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDto.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        Page<SearchTotalResultDto> pageResult = new PageImpl<>(results, requestDto.getPageable(), results.size());

        return Optional.of(pageResult);
    }

}
