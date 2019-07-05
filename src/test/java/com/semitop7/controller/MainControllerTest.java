package com.semitop7.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.semitop7.ObjectBuilderTest.testXmlFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired
    private MainController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void main() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("XML Upload")));
    }

    @Test
    public void forward() throws Exception {
        this.mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void handleFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files[]",
                "test.xml",
                "text/xml",
                testXmlFile().getBytes());
        this.mockMvc.perform(multipart("/upload")
                .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(false)));
    }

    @Test
    public void handleFileUploadError() throws Exception {
        this.mockMvc.perform(multipart("/upload"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is(true)))
                .andExpect(jsonPath("$.errorMsg", containsString("files")));
    }
}