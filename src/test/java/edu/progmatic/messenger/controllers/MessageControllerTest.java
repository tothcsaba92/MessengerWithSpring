package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.services.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;

    @Test
    public void testShownNonDeletedMessagesForUsers() throws Exception {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message("hello", "aladár"));
        Mockito.when(messageService.showNonDeletedMessages(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(msgs);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.view().name("messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", msgs));
        Mockito.verify(messageService, Mockito.times(1))
                .showNonDeletedMessages(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @WithUserDetails("user")
    @Test
    public void testShowMessagesForAdmin() throws Exception {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message("hello", "aladár"));
        Mockito.when(messageService.filterByStatus(Mockito.any(), Mockito.any())).thenReturn(msgs);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.view().name("messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", msgs));
        Mockito.verify(messageService, Mockito.times(1))
                .filterByStatus(Mockito.any(), Mockito.any());
    }

    @Test
    public void testShownASingleMessageForUsers() throws Exception {
        Message msg = new Message("asédflkj", "aladár");

        //mvcResult = mockMvc.perform(put("/some/uri/{foo}/{bar}", "foo", ""))
        int msgId = 2;
        msg.setId(msgId);
        Mockito.when(messageService.showSelectedMessageById(msgId)).thenReturn(msg);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("single_message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", msg));
        Mockito.verify(messageService, Mockito.times(1))
                .showSelectedMessageById(msgId);
    }

}