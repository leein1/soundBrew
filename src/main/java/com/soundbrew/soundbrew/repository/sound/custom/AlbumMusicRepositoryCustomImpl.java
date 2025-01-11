package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
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
    public Optional<Page<SearchTotalResultDTO>> search(RequestDTO requestDTO){
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

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

        BooleanBuilder havingBuilder = new BooleanBuilder();
        for(String instrument : instruments) {
            havingBuilder.and(Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName).matches("%"+instrument+"%"));
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
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
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
                .orderBy(orderSpecifier) // 동적 정렬 추가
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 서브쿼리를 사용하여 필터링된 ID 가져오기
        List<AlbumMusicId> filteredIds = queryFactory
                .select(albumMusic.id)  // 필요한 ID만 가져옴
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder)  // 기본 검색 조건 적용
                .groupBy(albumMusic.id)  // 그룹화하여 ID 추출
                .having(havingBuilder)  // 조건을 HAVING으로 필터링
                .fetch();  // 필터링된 ID 가져오기

        // 페이징 결과를 Page로 래핑하여 반환z
        long total = queryFactory
                .select(Expressions.asNumber(1).count())
                .from(albumMusic)
                .where(albumMusic.id.in(filteredIds))  // 서브쿼리로 필터링된 ID로 총 갯수 계산
                .fetchOne();

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> searchAlbum(RequestDTO requestDTO){
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

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

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
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname, album.createDate, album.modifyDate
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
                .orderBy(orderSpecifier) // 동적 정렬 추가
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 서브쿼리를 사용하여 필터링된 ID 가져오기
        List<Integer> filteredAlbumIds = queryFactory
                .select(album.albumId)  // 앨범 ID만 가져옴
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder)  // 기본 검색 조건 적용
                .groupBy(album.albumId)  // 앨범 단위로 그룹화
                .having(havingBuilder)  // 조건을 HAVING으로 필터링
                .fetch();  // 필터링된 앨범 ID 가져오기
        // 페이징 결과를 Page로 래핑하여 반환z
        long total = queryFactory
                .select(album.albumId.countDistinct())  // 고유 앨범 ID 개수 계산
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .where(album.albumId.in(filteredAlbumIds))  // 필터링된 앨범 ID로 조건 추가
                .fetchOne();

        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> albumOne(String nickname, String albumName, RequestDTO requestDTO) {
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();

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

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

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
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
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
                .orderBy(orderSpecifier) // 동적 정렬 추가
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 서브쿼리를 사용하여 필터링된 ID 가져오기
        List<AlbumMusicId> filteredIds = queryFactory
                .select(albumMusic.id)  // 필요한 ID만 가져옴
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder)  // 기본 검색 조건 적용
                .groupBy(albumMusic.id)  // 그룹화하여 ID 추출
                .having(havingBuilder)  // 조건을 HAVING으로 필터링
                .fetch();  // 필터링된 ID 가져오기

        // 페이징 결과를 Page로 래핑하여 반환z
        long total = queryFactory
                .select(Expressions.asNumber(1).count())
                .from(albumMusic)
                .where(albumMusic.id.in(filteredIds))  // 서브쿼리로 필터링된 ID로 총 갯수 계산
                .fetchOne();

        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> albumOne(int userId, int id, RequestDTO requestDTO) {
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

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

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
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
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
                .orderBy(orderSpecifier) // 동적 정렬 추가
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

// 서브쿼리를 사용하여 필터링된 ID 가져오기
        List<AlbumMusicId> filteredIds = queryFactory
                .select(albumMusic.id)  // 필요한 ID만 가져옴
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder)  // 기본 검색 조건 적용
                .groupBy(albumMusic.id)  // 그룹화하여 ID 추출
                .having(havingBuilder)  // 조건을 HAVING으로 필터링
                .fetch();  // 필터링된 ID 가져오기

        // 페이징 결과를 Page로 래핑하여 반환z
        long total = queryFactory
                .select(Expressions.asNumber(1).count())
                .from(albumMusic)
                .where(albumMusic.id.in(filteredIds))  // 서브쿼리로 필터링된 ID로 총 갯수 계산
                .fetchOne();

        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> verifyAlbum(RequestDTO requestDTO){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(album.verify.eq(0));

        if(requestDTO.getType()!= null) {
            List<String> types = List.of(requestDTO.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t": builder.and(album.albumName.contains(requestDTO.getKeyword()));break;
                    case "n": builder.and(album.nickname.contains(requestDTO.getKeyword())); break;
                }
            }
        }

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

        // 쿼리 생성
        List<SearchTotalResultDTO> results = queryFactory
                .select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname, album.createDate, album.modifyDate
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
                .orderBy(orderSpecifier) // 동적 정렬 추가
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 2. 전체 레코드 수 쿼리 실행
        long total = queryFactory
                .select(albumMusic.countDistinct())
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder) // 동일 조건 사용
                .fetchOne();
        // 페이징 결과를 Page로 래핑하여 반환
        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        // Optional로 반환
        return Optional.of(pageResult);
    }

    @Override
    public Optional<Page<SearchTotalResultDTO>> verifyAlbumOne(int userId, int id, RequestDTO requestDTO){
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(album.albumId.eq(id));
        builder.and(album.userId.eq(userId));
        builder.and(album.verify.eq(0));

        // 동적 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(requestDTO);

        List<SearchTotalResultDTO> results = queryFactory
                .select(Projections.bean(
                        SearchTotalResultDTO.class,
                        Projections.bean(AlbumDTO.class,
                                album.albumId, album.albumName, album.albumArtPath, album.description, album.nickname
                        ).as("albumDTO"),
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.createDate, music.modifyDate
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
                .orderBy(orderSpecifier)
                .offset(requestDTO.getPageable().getOffset()) // 페이징 시작 위치
                .limit(requestDTO.getPageable().getPageSize()) // 페이지 크기
                .fetch(); // 여러 개의 결과를 가져옴

        // 2. 전체 레코드 수 쿼리 실행
        long total = queryFactory
                .select(albumMusic.countDistinct())
                .from(albumMusic)
                .leftJoin(album).on(albumMusic.album.eq(album))
                .leftJoin(music).on(albumMusic.music.eq(music))
                .leftJoin(musicInstrumentTag).on(musicInstrumentTag.music.eq(music))
                .leftJoin(instrumentTag).on(musicInstrumentTag.instrumentTag.eq(instrumentTag))
                .leftJoin(musicMoodTag).on(musicMoodTag.music.eq(music))
                .leftJoin(moodTag).on(musicMoodTag.moodTag.eq(moodTag))
                .leftJoin(musicGenreTag).on(musicGenreTag.music.eq(music))
                .leftJoin(genreTag).on(musicGenreTag.genreTag.eq(genreTag))
                .where(builder) // 동일 조건 사용
                .fetchOne();

        Page<SearchTotalResultDTO> pageResult = new PageImpl<>(results, requestDTO.getPageable(), total);

        return Optional.of(pageResult);
    }

    private OrderSpecifier<?> createOrderSpecifier(RequestDTO requestDTO) {
        if (requestDTO.getMore() != null) {
            String sortKey = requestDTO.getMore().get("sort");
            if (sortKey != null) {
                switch (sortKey.toLowerCase()) {
                    case "newest":
                        return album.createDate.asc(); // 최신순
                    case "oldest":
                        return album.createDate.desc(); // 오래된 순
                    case "download":
                        return album.createDate.asc();
//                                album.downloadCount.desc()
//                                ; // 다운로드 순
                    default:
                        return album.createDate.asc(); // 기본 정렬: 최신순
                }
            }
        }
        return album.createDate.desc(); // 기본 정렬: 최신순
    }
}
