package skillsblock.todoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skillsblock.todoproject.models.Note;

public interface NotesRepository
        extends JpaRepository<Note, Long> {
}
