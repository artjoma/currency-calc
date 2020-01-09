package io.artyom.currencycalc.repo;

import io.artyom.currencycalc.entity.CurrencyPair;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CurrencyPairRepo {
    //in normal case should be replaced to DB|Nosql+cache
    //foe this example used simple im mem Map
    //currency pair storage
    private Map<String, CurrencyPair> storage;

    @PostConstruct
    public void init(){
        storage = new HashMap<>(5000);
    }

    public Optional<CurrencyPair> get(String currencyPairName){
        return Optional.ofNullable(storage.getOrDefault(currencyPairName, null));
    }

    public Optional<CurrencyPair> get(Currency base,Currency quote){
        return get(CurrencyPair.name(base, quote));
    }

    public void add(CurrencyPair pair){
        storage.put(pair.getName(), pair);
    }

    public Collection<CurrencyPair> findAll(){
        return storage.values();
    }

    public int count(){
        return storage.size();
    }
}
