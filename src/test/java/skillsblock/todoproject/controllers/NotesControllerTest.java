package skillsblock.todoproject.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import skillsblock.todoproject.models.Note;
import skillsblock.todoproject.repository.NotesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NotesController.class)
public class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotesRepository repository;

    @Test
    public void testGetAllNotes() throws Exception {

        List<Note> expectedNotesList = new ArrayList<>();
        Note note = new Note();
        note.setDescription("blah");
        note.setStatus("NEW");
        expectedNotesList.add(note);

        when(repository.findAll()).thenReturn(expectedNotesList);

        MvcResult result = this.mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);

        ObjectMapper mapper = new ObjectMapper();

        List<Note> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Note>>() {});

        assertEquals(expectedNotesList.size(), actual.size());
    }

}
