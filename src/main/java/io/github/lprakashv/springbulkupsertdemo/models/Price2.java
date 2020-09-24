package io.github.lprakashv.springbulkupsert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_with_id", schema = "testingschema")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Price2 implements Serializable {

    private static final long serialVersionUID = 24233243L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private PriceKey priceKey;

    @Column(name = "price")
    private Double price;

}