package dev.journey.movieapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.core.io.FileSystemResource;

import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FileUploadTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private FileController fileController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void shouldUploadFile() throws Exception {
        Resource resource = new ClassPathResource("test.jpg");

        Resource fileResource = new FileSystemResource(resource.getFile());
        assertNotNull(fileResource);

        MockMultipartFile firstFile = new MockMultipartFile(
                "attachments", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream());

        assertNotNull(firstFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/file/upload")
                .file("file", firstFile.getBytes())
                .headers(headers);

        this.mockMvc.perform(servletRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Disabled
    void shouldGetFileAsResource() throws Exception {
        MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders
                .get("/file/")
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("fileName", "test.jpg");

        this.mockMvc.perform(servletRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
