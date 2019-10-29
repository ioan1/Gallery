package fr.redby.gallery.service.statistics;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.service.StatisticsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceStatisticsApplicationTests {

	@Autowired
	private StatisticsService service;

	@Test
	public void testDiskUsage() {
		System.setProperty("GALLERY_PATH", File.listRoots()[0].getAbsolutePath());

		DiskUsage diskUsage = service.getDiskUsage();

		System.out.println("Available: " + diskUsage.getAvailable());
		System.out.println("Used: " + diskUsage.getUsed());
		System.out.println("Unit: " + diskUsage.getUnit());

		Assert.assertTrue(diskUsage.getAvailable() > 0);
		Assert.assertTrue(diskUsage.getUsed() > 0);
		Assert.assertNotNull(diskUsage.getUnit());


	}

}
