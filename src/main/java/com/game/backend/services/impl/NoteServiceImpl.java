package com.game.backend.services.impl;

import com.game.backend.models.Note;
import com.game.backend.repositories.NoteRepository;
import com.game.backend.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Override
    @Transactional
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note updateNoteForUser(Long noteId, String content, String username) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteNoteForUser(Long noteId, String username) {
        noteRepository.deleteById(noteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findByOwnerUsername(username);
    }
}
