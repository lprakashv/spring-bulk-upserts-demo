package io.github.lprakashv.springbulkupsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PriceIngestService {
    @Autowired
    private Price1Repo price1Repo;

    @Autowired
    private Price2Repo price2Repo;

    @Autowired
    private EntityManager entityManager;

    /*
     * private AtomicBoolean keepIngesting = new AtomicBoolean(true);
     * 
     * public void ingestRandomPrice1Batches(Supplier<List<Price1>>
     * randomPriceBatchSupplier) { int batchNo = 1; while (keepIngesting.get()) {
     * long startMillis = System.currentTimeMillis();
     * 
     * List<Price1> batch = randomPriceBatchSupplier.get();
     * 
     * price1Repo.saveAll(batch);
     * 
     * log.info("Persisted batch {} ( {} ) for price1 in {} sec", batchNo,
     * batch.size(), (System.currentTimeMillis() - startMillis) / 1000 ); batchNo++;
     * } }
     */

    public void ingestRandomPrice1s() {
        Random r = new Random(0);

        int batch = 1;
        while (true) {
            List<Price1> l = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                l.add(new Price1(new PriceKey("abc" + r.nextInt(1000), String.valueOf(r.nextInt(10))), r.nextDouble()));
            }
            long startMillis = System.currentTimeMillis();
            price1Repo.saveAll(l);
            log.info("Persisted batch {} ( 1000 ) for price1 in {} sec", batch,
                    (System.currentTimeMillis() - startMillis) / 1000);
            batch++;
        }
    }

    public void ingestRandomPrice2s() {
        Random r = new Random(0);

        int batch = 1;
        while (true) {
            List<Price2> l = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                l.add(new Price2(null, new PriceKey("abc" + r.nextInt(1000), String.valueOf(r.nextInt(10))),
                        r.nextDouble()));
            }
            long startMillis = System.currentTimeMillis();
            price2Repo.saveAll(l);
            log.info("Persisted batch {} ( 1000 ) for price2 in {} sec", batch,
                    (System.currentTimeMillis() - startMillis) / 1000);
            batch++;
        }
    }

    public String multiRowImsertStatement(List<Price2> batch) {
        StringBuilder insertSQLBuilder = new StringBuilder(
                "insert into testingschema.price_with_id (upc, store_id, price) values ");
        batch.forEach((Price2 row) -> {
            insertSQLBuilder.append("(");

            insertSQLBuilder
                    .append(row.getPriceKey().getUpc() == null ? "null" : "'" + row.getPriceKey().getUpc() + "'");
            insertSQLBuilder.append(",");
            insertSQLBuilder.append(
                    row.getPriceKey().getStoreId() == null ? "null" : "'" + row.getPriceKey().getStoreId() + "'");
            insertSQLBuilder.append(",");
            insertSQLBuilder.append(row.getPrice() == null ? "null" : row.getPrice());

            insertSQLBuilder.append(")");
            insertSQLBuilder.append(",");
        });
        String insertSQL = insertSQLBuilder.toString();
        return insertSQL.substring(0, insertSQL.length() - 1); // to remove the extra comma ',' at the end
    }

    @Transactional
    public void insertBatchNative(List<Price2> batch, int batchNo) {
        String insertSQL = multiRowImsertStatement(batch);
        long startMillis = System.currentTimeMillis();
        entityManager.createNativeQuery(insertSQL).executeUpdate();
        log.info("Inserted batch {} ( 1000 ) for price2 in {} millis", batchNo,
                (System.currentTimeMillis() - startMillis));
    }

    @Transactional
    public void insertRandomPrice2s() {
        Random r = new Random(0);

        int batch = 1;
        while (true) {
            List<Price2> l = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                l.add(new Price2(null, new PriceKey("abc" + r.nextInt(1000), String.valueOf(r.nextInt(10))),
                        r.nextDouble()));
            }

            String insertSQL = multiRowImsertStatement(l);

            long startMillis = System.currentTimeMillis();
            entityManager.createNativeQuery(insertSQL).executeUpdate();
            log.info("Inserted batch {} ( 1000 ) for price2 in {} millis", batch,
                    (System.currentTimeMillis() - startMillis));
            batch++;
        }
    }
}