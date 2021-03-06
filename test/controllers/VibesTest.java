package controllers;

import models.Tag;
import models.Vibe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.FunctionalTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static play.mvc.Http.Response;

public class VibesTest extends FunctionalTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void shouldRedirectToLatestAfterSave() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("vibe.message", "TheMessage");
        Response response = POST("/vibes", parameters);

        assertStatus(302, response);
        assertHeaderEquals("Location", "/vibes", response);
    }

    @Test
    public void shouldSaveMessage() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("vibe.message", "TheMessage");
        POST("/vibes", parameters);

        Vibe savedVibe = Vibe.find("byMessage", "TheMessage").first();
        Assert.assertThat(savedVibe, notNullValue());
    }

    @Test
    public void shouldFindAllMessages(){
        GET("/vibes");
        List<Vibe> vibes = (List<Vibe>) renderArgs("vibes");

        Assert.assertThat(vibes.size(), equalTo(2));
        Assert.assertThat(vibes.get(0).getMessage() , equalTo("Welcome all, how awesome is this?"));
        Assert.assertThat(vibes.get(1).getMessage() , equalTo("What is going to happen on Friday?"));
    }

    @Test
    public void shouldIncludeTagCloudWhenRenderingLatestVibes(){
        GET("/vibes");
        
        List<Tag> tagCloud = (List<Tag>) renderArgs("tagCloud");
        Assert.assertThat(tagCloud, notNullValue());
        Assert.assertThat(tagCloud.size(), is(1));
    }
    
    @Test
    public void shouldDeleteAVibe() {
        Vibe vibe = new Vibe().save();
        DELETE("/vibes/" + vibe.id);

        vibe = Vibe.find("id = ?", vibe.id).first();
        Assert.assertThat(vibe, nullValue());
    }
}