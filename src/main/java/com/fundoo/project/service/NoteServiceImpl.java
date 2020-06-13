package com.fundoo.project.service;

import com.fundoo.project.Repositories.NoteRepository;
import com.fundoo.project.Repositories.UserRepository;
import com.fundoo.project.dto.NoteDto;
import com.fundoo.project.exception.UserException;
import com.fundoo.project.model.NoteModel;
import com.fundoo.project.model.UserModel;
import com.fundoo.project.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("NoteService")
public class NoteServiceImpl implements INote {
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public NoteModel createNote(NoteDto noteDto, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        if(noteDto.getTitle().isEmpty()){
            throw new UserException("Note title can't be empty");
        }
        NoteModel noteModel= NoteModel.builder().title(noteDto.getTitle())
                .description(noteDto.getDescription())
                .color(noteDto.getColor())
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();
        NoteModel createdNote=noteRepository.save(noteModel);
        optionalUserModel.get().getNotes().add(createdNote);
        userRepository.save(optionalUserModel.get());
        return createdNote;
    }

    @Override
    public NoteModel deleteNote(Long noteId, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        if(!optionalNoteModel.isPresent()){
            throw new UserException("Note note present");
        }
        optionalUserModel.get().getNotes().stream().filter(data->{
            return data.getNoteId().equals(noteId);
        }).findFirst().orElseThrow(()-> new UserException("Note not present"));

        return optionalUserModel.filter(user->{
            return user!=null;
        }).map(user->{
            user.getNotes().remove(optionalNoteModel.get());
            userRepository.save(user);
            return optionalNoteModel.get();
        }).orElseThrow(()->new UserException("Something went wrong while deleting note"));
    }

    @Override
    public NoteModel updateNote(Long noteId, String token) {
        return null;
    }

    @Override
    public List<NoteModel> getNotes(String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        List<NoteModel> notes = noteRepository.find(id).stream().filter(data->!data.isArchived()&&!data.isTrashed())
                .collect(Collectors.toList());
        if(notes.isEmpty()){
            throw new UserException("No notes found for this user");
        }
        return notes;
    }

    @Override
    public NoteModel archiveNote(Long noteId, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        optionalUserModel.get().getNotes().stream().filter(data->{
            return data.getNoteId().equals(noteId);
        }).findFirst().orElseThrow(()-> new UserException("Note not present"));
       return optionalNoteModel.filter(note->{
            return note!=null;
        }).map(note->{
            note.setArchived(!note.isTrashed());
            note.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(optionalUserModel.get());
            return note;
       }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public NoteModel trashNote(Long noteId, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        optionalUserModel.get().getNotes().stream().filter(data->{
            return data.getNoteId().equals(noteId);
        }).findFirst().orElseThrow(()-> new UserException("Note not present"));
        return optionalNoteModel.filter(note->{
            return note!=null;
        }).map(note->{
            note.setTrashed(!note.isTrashed());
            note.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(optionalUserModel.get());
            return note;
        }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public NoteModel pinNote(Long noteId, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        optionalUserModel.get().getNotes().stream().filter(data->{
            return data.getNoteId().equals(noteId);
        }).findFirst().orElseThrow(()-> new UserException("Note not present"));
        return optionalNoteModel.filter(note->{
            return note!=null;
        }).map(note->{
            note.setPinned(!note.isPinned());
            note.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(optionalUserModel.get());
            return note;
        }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public List<NoteModel> getArchivedNotes(String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        return optionalUserModel.filter(user->{
            return user!=null;
        }).map(user->{
            List<NoteModel> archivedNotes = user.getNotes().stream().filter(data -> data.isArchived()).collect(Collectors.toList());
            if(archivedNotes.isEmpty()){
                throw new UserException("No archived notes are present");
            }
            return archivedNotes;
        }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public List<NoteModel> getTrashedNotes(String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        return optionalUserModel.filter(user->{
            return user!=null;
        }).map(user->{
            List<NoteModel> trashedNotes = user.getNotes().stream().filter(data -> data.isTrashed()).collect(Collectors.toList());
            if(trashedNotes.isEmpty()){
                throw new UserException("No archived notes are present");
            }
            return trashedNotes;
        }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public List<NoteModel> getPinnedNotes(String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }

        return optionalUserModel.filter(user->{
            return user!=null;
        }).map(user->{
            List<NoteModel> pinnedNotes = user.getNotes().stream().filter(data -> data.isPinned()).collect(Collectors.toList());
            if(pinnedNotes.isEmpty()){
                throw new UserException("No archived notes are present");
            }
            return pinnedNotes;
        }).orElseThrow(()->new UserException("note doesn't exist"));
    }

    @Override
    public List<NoteModel> getAllRemindedNotes(String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }

        return optionalUserModel.filter(user->{
            return user!=null;
        }).map(user->{
            List<NoteModel> remindedNotes = user.getNotes().stream().filter(data -> data.getReminder()!=null).collect(Collectors.toList());
            if(remindedNotes.isEmpty()){
                throw new UserException("No archived notes are present");
            }
            return remindedNotes;
        }).orElseThrow(()->new UserException("note doesn't exist"));

    }

    @Override
    public NoteModel addReminderToNotes(Long noteId, LocalDateTime reminder, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        if(!optionalNoteModel.isPresent()){
            throw new UserException("Note not present");
        }

        return optionalNoteModel.filter(note->{
            return note!=null;
        }).map(note->{
            if(LocalDateTime.now()==reminder){
                throw new UserException("Please set the reminder after current data");
            }
            note.setReminder(reminder);
            noteRepository.save(note);
            return note;
        }).orElseThrow(()-> new UserException("Something went wrong while adding reminder to note"));
    }

    @Override
    public NoteModel removeReminderToNotes(Long noteId,LocalDateTime reminder, String token) {
        Long id=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUserModel = userRepository.findById(id);
        if(!optionalUserModel.isPresent()){
            throw new UserException("Invalid token");
        }
        Optional<NoteModel> optionalNoteModel = noteRepository.findById(noteId);
        if(!optionalNoteModel.isPresent()){
            throw new UserException("Note not present");
        }
        return optionalNoteModel.filter(note->{
            return note!=null;
        }).map(note->{
            if(LocalDateTime.now()==reminder){
                throw new UserException("Please set the reminder after current data");
            }
            note.setReminder(null);
            noteRepository.save(note);
            return note;
        }).orElseThrow(()-> new UserException("Something went wrong while adding reminder to note"));
    }
}
