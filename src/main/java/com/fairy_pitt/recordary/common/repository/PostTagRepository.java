package com.fairy_pitt.recordary.common.repository;

import com.fairy_pitt.recordary.common.entity.PostEntity;
import com.fairy_pitt.recordary.common.entity.PostTagEntity;
import com.fairy_pitt.recordary.common.entity.UserEntity;
import com.fairy_pitt.recordary.common.pk.PostTagPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTagEntity, PostTagPK> {
    PostTagEntity findByPostFKAndUserFK(PostEntity postFK, UserEntity userFK);
    List<PostEntity> findAllByUserFK(UserEntity userFK);
    List<UserEntity> findAllByPostFK(PostEntity postFK);
}