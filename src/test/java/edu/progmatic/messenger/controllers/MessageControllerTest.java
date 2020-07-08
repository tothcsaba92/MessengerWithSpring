package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.TopicService;
import edu.progmatic.messenger.services.UserService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;
    @MockBean
    private TopicService topicService;
    @MockBean
    private UserService userService;

    @Test
    public void testShownNonDeletedMessagesForUsers() throws Exception {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message());
        Mockito.when(messageService.showMessages(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(msgs);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.view().name("messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", msgs));
        Mockito.verify(messageService, Mockito.times(1))
                .showMessages(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @WithUserDetails("admin")
    @Test
    public void testShowMessagesForAdmin() throws Exception {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message());
        Mockito.when(messageService.showMessages(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(),
                Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(msgs);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.view().name("messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("messages", msgs));
        Mockito.verify(messageService, Mockito.times(1))
                .showMessages(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testShownASingleMessageForUsers() throws Exception {
        Message msg = new Message();
        Long msgId = 2L;
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