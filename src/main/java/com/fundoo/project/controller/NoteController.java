package com.fundoo.project.controller;

import com.fundoo.project.dto.NoteDto;
import com.fundoo.project.model.NoteModel;
import com.fundoo.project.response.Response;
import com.fundoo.project.service.INote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private INote iNote;
    @PostMapping("/createNote")
    public ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader(name = "token") String token){
        NoteModel noteModel=iNote.createNote(noteDto,token);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.CREATED.value(),"Note created successfully", noteModel));
    }
}
