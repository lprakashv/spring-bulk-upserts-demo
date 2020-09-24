package io.github.lprakashv.springbulkupsert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PriceKey implements Serializable {

    private static final long serialVersionUID = 24232431L;

    @Getter
    @Setter
    @Column(name = "upc")
    private String upc;

    @Getter
    @Setter
    @Column(name = "store_id")
    private String storeId;
}