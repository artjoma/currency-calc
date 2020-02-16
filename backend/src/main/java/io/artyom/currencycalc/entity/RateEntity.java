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
@Table(name = "rates")
public class RateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "created_at")
    private Instant created;

    @OneToOne
    @JoinColumn (name = "pair_id")
    private CurrencyPairEntity currencyPair;
}
