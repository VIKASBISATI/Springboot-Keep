package com.fundoo.project.Repositories;

import com.fundoo.project.model.NoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteModel,Long> {
    List<NoteModel> find(Long id);
}
