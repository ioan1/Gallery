package fr.redby.gallery.service.statistics;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import fr.redby.gallery.service.statistics.service.StatisticsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceStatisticsApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger( ServiceStatisticsApplicationTests.class );


	@Autowired
	private StatisticsService service;

	@Test
	public void testExample() {
		// TODO.
	}

}
