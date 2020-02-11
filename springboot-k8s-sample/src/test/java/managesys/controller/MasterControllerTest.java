package managesys.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import managesys.UinitTestSupport;
import managesys.WebTestSupport;
import managesys.model.Category;
import managesys.model.Format;
import managesys.service.MasterService;

@RunWith(SpringRunner.class)
public class MasterControllerTest extends WebTestSupport {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MasterService masterService;

    private List<Category> categoryList;

    private List<Format> formatList;

    @Before
    public void setup() {
        categoryList = UinitTestSupport.generateCategoriesData();
        formatList = UinitTestSupport.generateFormatsData();
    }

    @Test
    public void listCategory() throws JsonProcessingException, Exception {
        when(masterService.findAllCategories()).thenReturn(categoryList);

        ResponseEntity<String> res = get("/api/master/category", String.class);

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(res.getBody(), mapper.writeValueAsString(categoryList));

        verify(masterService, atLeastOnce()).findAllCategories();
    }

    @Test
    public void listFormat() throws JsonProcessingException, Exception {
        when(masterService.findAllFormats()).thenReturn(formatList);

        ResponseEntity<String> res = get("/api/master/format", String.class);

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(res.getBody(), mapper.writeValueAsString(formatList));

        verify(masterService, atLeastOnce()).findAllFormats();
    }
}
