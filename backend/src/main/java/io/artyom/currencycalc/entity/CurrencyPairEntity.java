package io.artyom.currencycalc.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Currency;

@Data
@ToString
@Entity
@Table(name = "currency_pairs")
public class CurrencyPairEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "base")
    private Currency base;

    @Column(name = "quote")
    private Currency quote;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "fee_id")
    private FeeEntity fee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn (name = "rate_id")
    private RateEntity rate;

    @Column (name = "pair_name")
    private String name;

}
