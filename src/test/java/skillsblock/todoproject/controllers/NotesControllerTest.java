package skillsblock.todoproject.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import skillsblock.todoproject.models.Note;
import skillsblock.todoproject.repository.NotesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        List<Note> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Note>>() {
        });

        assertEquals(expectedNotesList.size(), actual.size());
    }

    @Test
    public void addNote() throws Exception {
        Note note = new Note();
        note.setDescription("foo");
        note.setStatus("NEW");

        when(repository.saveAndFlush(any())).thenReturn(note);

        MvcResult result = this.mockMvc.perform(post("/notes")
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        verify(repository, times(1)).saveAndFlush(ArgumentMatchers.refEq(note));

        ObjectMapper mapper = new ObjectMapper();
        Note actual = mapper.readValue(result.getResponse().getContentAsString(), Note.class);

        assertEquals(note.getDescription(), actual.getDescription());
        assertEquals(note.getStatus(), actual.getStatus());
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
