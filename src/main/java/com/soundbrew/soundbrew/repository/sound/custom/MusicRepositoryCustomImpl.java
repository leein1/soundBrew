package com.soundbrew.soundbrew.repository.sound.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MusicRepositoryCustomImpl implements MusicRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SearchTotalResultDto> soundOne(String nickname, String title) {
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

        SearchTotalResultDto result = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        music.musicId, music.title, music.filePath, music.price, music.description, music.nickname,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        music.create_date,
                        music.modify_date
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
                        music.nickname, music.create_date, music.modify_date
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SearchTotalResultDto> soundOne(int userId, int musicId) {
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

        SearchTotalResultDto result = queryFactory
                .select(Projections.constructor(
                        SearchTotalResultDto.class,
                        music.musicId, music.title, music.filePath, music.price, music.description, music.nickname,
                        Expressions.stringTemplate("group_concat_distinct({0})", instrumentTag.instrumentTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", moodTag.moodTagName),
                        Expressions.stringTemplate("group_concat_distinct({0})", genreTag.genreTagName),
                        music.create_date,
                        music.modify_date
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
                        music.nickname, music.create_date, music.modify_date
                )
                .fetchOne(); // 단일 결과 반환

        return Optional.ofNullable(result);
    }
}
