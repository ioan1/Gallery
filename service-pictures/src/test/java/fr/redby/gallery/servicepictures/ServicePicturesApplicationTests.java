package fr.redby.gallery.servicepictures;

import fr.redby.gallery.servicepictures.service.PicturesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicePicturesApplicationTests {

	@Autowired
	private PicturesService service;

	@Test
	public void contextLoads() {
	}



}
