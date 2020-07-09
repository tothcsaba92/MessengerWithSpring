//package edu.progmatic.messenger.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = HomeController.class)
//public class HomeControllerTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    //@WithUserDetails ez kell ha specifikalni akarjuk az usert!
//    public void testHomePage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
//                .andExpect(MockMvcResultMatchers.view().name("home"));
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                .andExpect(MockMvcResultMatchers.view().name("home"));
//    }
//
//
//}