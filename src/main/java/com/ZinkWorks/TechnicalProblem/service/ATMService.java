package com.ZinkWorks.TechnicalProblem.service;

import com.ZinkWorks.TechnicalProblem.entities.Account;
import com.ZinkWorks.TechnicalProblem.entities.CurrencyUnit;
import com.ZinkWorks.TechnicalProblem.repositories.AccountsRepository;
import com.ZinkWorks.TechnicalProblem.repositories.CurrencyUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ATMService {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;

    public Boolean userLogin(String userId, String userPin) {
        Optional<Account> account = accountsRepository.findById(userId);
        if (account.isPresent()) {
            Account userAccount = account.get();
            if (userPin.equals(userAccount.getUserPin())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public BigDecimal getBalance(String userId) {
        Optional<Account> account = accountsRepository.findById(userId);
        if (account.isPresent()) {
            return account.get().getOpeningBalance();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalAvailableAmount(String userId) {
        Optional<Account> account = accountsRepository.findById(userId);
        if (account.isPresent()) {
            BigDecimal totalBalance = account.get().getOpeningBalance().add(account.get().getOverdraft());
            return totalBalance;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public List<Integer> cashWithDraw(String userId,BigDecimal amount) throws Exception{
        if (isValidAmount(amount)){
            if(getTotalAvailableAmount(userId).compareTo(amount)>=0){
                if (currencyUnitRepository.getTotalAvailableCashAmount().compareTo(amount) >=0) {
                        List<Integer> notesList = calculateNotes(amount);
                        Account userAccount = accountsRepository.getById(userId);
                        BigDecimal newBalance = userAccount.getOpeningBalance().subtract(amount);
                        userAccount.setOpeningBalance(newBalance);
                        accountsRepository.save(userAccount);
                        return notesList;
                } else {
                    throw new Exception("Requested amount exceeds current ATM balance");
                }
            }else{
                throw new Exception("Requested amount exceeds user account balance");
            }
        } else {
            throw new Exception("Requested amount must be greater than 0 and divisible by the minimum currency unit: "+currencyUnitRepository.getMinimumCurrencyUnit());
        }
    }

    private List<Integer> calculateNotes(BigDecimal amount) throws Exception {
        List<CurrencyUnit> availableNotesList = currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"));
        List<Integer> finalNotesList= new ArrayList<Integer>();
        Integer total = 0;
        for(int i=0;i<availableNotesList.size();i++) {
            total = 0;
            finalNotesList = new ArrayList<Integer>();
            availableNotesList = resetAvailableCurrencies(availableNotesList, i);
            for (int j=i;j<availableNotesList.size();j++) {
                CurrencyUnit unit = availableNotesList.get(j);
                while (total + unit.getUnit() <= amount.intValue() && unit.getQuantity() > 0) {
                    total += unit.getUnit();
                    finalNotesList.add(unit.getUnit());
                    unit.setQuantity(unit.getQuantity() - 1);
                }
            }
            if (total.equals(amount.intValue())) {break;}
        }
        if (total.equals(amount.intValue())) {
            availableNotesList.stream().forEach(t -> System.out.println(t.getQuantity()));
            currencyUnitRepository.saveAll(availableNotesList);
            System.out.println(finalNotesList);
            return finalNotesList;
        }else {
            throw new Exception("The requested amount was unavailable with the available currency");
        }

    }

    private List<CurrencyUnit> resetAvailableCurrencies(List<CurrencyUnit> availableNotesList, int i) {
        return i > 0 ? currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit")) : availableNotesList;
    }

    private Boolean isValidAmount(BigDecimal amount) {
        Integer minCurrencyUnit = currencyUnitRepository.getMinimumCurrencyUnit();
        if (amount.intValue() > 0 && amount.remainder(new BigDecimal(minCurrencyUnit)).equals(BigDecimal.ZERO)) {
            return true;
        } else {
            return false;
        }

    }

    public AccountsRepository getAccountsRepository() {
        return accountsRepository;
    }

    public void setAccountsRepository(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public CurrencyUnitRepository getCurrencyUnitRepository() {
        return currencyUnitRepository;
    }

    public void setCurrencyUnitRepository(CurrencyUnitRepository currencyUnitRepository) {
        this.currencyUnitRepository = currencyUnitRepository;
    }
}
