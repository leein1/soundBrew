package com.soundbrew.soundbrew.repository.sound.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;
}
