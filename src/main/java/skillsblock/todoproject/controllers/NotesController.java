package skillsblock.todoproject.controllers;

import org.springframework.web.bind.annotation.*;
import skillsblock.todoproject.models.Note;
import skillsblock.todoproject.repository.NotesRepository;

import java.util.List;

@RestController
@CrossOrigin()
public class NotesController {

    private final NotesRepository repository;

    public NotesController(NotesRepository repository) {
        this.repository = repository;
    }

    @GetMapping("notes")
    public List<Note> listAllNotes() {
        return repository.findAll();
    }

    @PostMapping("notes")
    public Note addNote(@RequestBody Note note) {
        return repository.saveAndFlush(note);
    }

}
