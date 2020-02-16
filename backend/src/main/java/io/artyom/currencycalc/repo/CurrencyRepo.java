package io.artyom.currencycalc.repo;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Repository
public class CurrencyRepo{
    private Set<Currency> storage;

    @PostConstruct
    public void init(){
        storage = new HashSet<>(256);
    }

    //return false if currency already registered
    public boolean add(Currency currency){
        return storage.add(currency);
    }

    public boolean addAll(Collection <Currency>currency){
        return storage.addAll(currency);
    }

    public Set<Currency> findAll(){
        return storage;
    }

    public void remove(Currency currency){
        storage.remove(currency);
    }

    //return records count
    public int count(){
        return storage.size();
    }

}
