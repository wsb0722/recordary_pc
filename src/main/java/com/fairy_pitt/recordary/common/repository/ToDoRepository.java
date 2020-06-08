package com.fairy_pitt.recordary.common.repository;

import com.fairy_pitt.recordary.common.entity.ToDoEntity;
import com.fairy_pitt.recordary.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoEntity, Long> {
    ToDoEntity findByToDoCd(Long cd);
    List<ToDoEntity> findByUserFKAndToDoEndDateAfter(UserEntity user, Date curr);
    List<ToDoEntity> findByUserFKAndToDoEndDateBefore(UserEntity user, Date toDoEndDate);

}
