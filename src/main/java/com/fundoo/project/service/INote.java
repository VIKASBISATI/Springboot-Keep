package com.fundoo.project.service;

import com.fundoo.project.dto.NoteDto;
import com.fundoo.project.model.NoteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface INote {
    NoteModel createNote(NoteDto noteDto, String token);

    NoteModel deleteNote(Long noteId, String token);

    NoteModel updateNote(Long noteId, String token);

    List<NoteModel> getNotes(String token);

    NoteModel archiveNote(Long noteId, String token);

    NoteModel trashNote(Long noteId, String token);

    NoteModel pinNote(Long noteId, String token);

    List<NoteModel> getArchivedNotes(String token);

    List<NoteModel> getTrashedNotes(String token);

    List<NoteModel> getPinnedNotes(String token);

    List<NoteModel> getAllRemindedNotes(String token);

    NoteModel addReminderToNotes(Long noteId, LocalDateTime reminder, String token);

    NoteModel removeReminderToNotes(Long noteId,LocalDateTime addReminderToNotes,String token);




}
