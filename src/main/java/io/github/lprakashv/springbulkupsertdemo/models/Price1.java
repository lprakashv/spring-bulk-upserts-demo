package io.github.lprakashv.springbulkupsert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price", schema = "testingschema")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Price1 implements Serializable {

    private static final long serialVersionUID = 2423243L;

    @EmbeddedId
    private PriceKey priceKey;

    @Column(name = "price")
    private Double price;

}