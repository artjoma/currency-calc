package io.artyom.currencycalc.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@ToString
@Entity
@Table (name = "fees")
public class FeeEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "updated_at")
    private Instant updated;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pair_id")
    private CurrencyPairEntity pair;

}
