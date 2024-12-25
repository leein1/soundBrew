package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsStreamDTO;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MusicRepositoryCustomImpl implements MusicRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SearchTotalResultDTO> soundOne(String nickname, String title) {
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(music.nickname.eq(nickname));
        builder.and(music.title.eq(title));

        SearchTotalResultDTO result = queryFactory.select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.nickname, music.createDate, music.modifyDate
                        ).as("musicDTO"),
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
                .where(builder) // 조건 추가
                .groupBy(
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        music.nickname, music.createDate, music.modifyDate
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SearchTotalResultDTO> soundOne(int userId, int musicId) {
        QMusic music = QMusic.music;
        QMusicInstrumentTag musicInstrumentTag =QMusicInstrumentTag.musicInstrumentTag;
        QInstrumentTag instrumentTag = QInstrumentTag.instrumentTag;
        QMusicMoodTag musicMoodTag = QMusicMoodTag.musicMoodTag;
        QMoodTag moodTag = QMoodTag.moodTag;
        QMusicGenreTag musicGenreTag = QMusicGenreTag.musicGenreTag;
        QGenreTag genreTag = QGenreTag.genreTag;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(music.userId.eq(userId));
        builder.and(music.musicId.eq(musicId));

        SearchTotalResultDTO result = queryFactory
                .select(Projections.bean(SearchTotalResultDTO.class,
                        Projections.bean(MusicDTO.class,
                                music.musicId, music.title, music.filePath, music.price, music.description, music.nickname, music.createDate, music.modifyDate
                        ).as("musicDTO"),
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
                .where(builder) // 조건 추가
                .groupBy(
                        music.musicId, music.title, music.filePath, music.price, music.description,
                        music.nickname, music.createDate, music.modifyDate
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }
}
