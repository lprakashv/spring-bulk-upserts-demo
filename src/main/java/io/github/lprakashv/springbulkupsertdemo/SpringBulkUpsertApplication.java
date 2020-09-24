package io.github.lprakashv.springbulkupsert;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringBulkUpsertApplication implements CommandLineRunner {

	@Autowired
	private PriceIngestService priceIngestService;

	/*
	 * @Autowired EntityManager entityManager;
	 */

	// private final AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) {
		SpringApplication.run(SpringBulkUpsertApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//priceIngestService.ingestRandomPrice1s();
		//priceIngestService.ingestRandomPrice2s();
		priceIngestService.insertRandomPrice2s();
	}

	@PreDestroy
	public void onExit() {
		// log.info("Persisted {} records in {} sec", count.get(),
		// (System.currentTimeMillis()-startMillis)/1000);
	}

}
