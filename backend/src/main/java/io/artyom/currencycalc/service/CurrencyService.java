package io.artyom.currencycalc.service;

import io.artyom.currencycalc.repo.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Currency;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyRepo currencyRepo;

    public Set<Currency> findAll(){
        return currencyRepo.findAll();
    }

    public int count(){
        return currencyRepo.count();
    }

    public boolean addAll(Collection<Currency> currency){
        return currencyRepo.addAll(currency);
    }

}
