package fr.redby.gallery.servicepictures;

import com.drew.imaging.ImageProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.redby.gallery.servicepictures.bean.ExifData;
import fr.redby.gallery.servicepictures.service.ExifService;
import fr.redby.gallery.servicepictures.service.PicturesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicePicturesApplicationTests {

	@Autowired
	private ExifService exifService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testReadExifInformation() throws IOException, ImageProcessingException, URISyntaxException {
		ExifData metadata = exifService.readExifMetadata(new File(this.getClass().getResource("IMG_20191111_172129.jpg").getFile()));
		Assert.assertNotNull(metadata);
		Assert.assertEquals("HUAWEI", metadata.getCameraMake());
		Assert.assertEquals("EML-L29", metadata.getCameraModel());
		Assert.assertEquals(1573492892000l, metadata.getDateTaken().getTime());
	}

}
