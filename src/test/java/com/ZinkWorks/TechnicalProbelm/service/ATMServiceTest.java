package com.ZinkWorks.TechnicalProbelm.service;

import com.ZinkWorks.TechnicalProblem.entities.Account;
import com.ZinkWorks.TechnicalProblem.entities.CurrencyUnit;
import com.ZinkWorks.TechnicalProblem.repositories.AccountsRepository;
import com.ZinkWorks.TechnicalProblem.repositories.CurrencyUnitRepository;
import com.ZinkWorks.TechnicalProblem.service.ATMService;
import org.aspectj.lang.annotation.Before;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.newCapture;
import static org.junit.jupiter.api.Assertions.*;

public class ATMServiceTest {
    private ATMService atmService;
    private AccountsRepository accountsRepository = EasyMock.createMock(AccountsRepository.class);
    private CurrencyUnitRepository currencyUnitRepository = EasyMock.createMock(CurrencyUnitRepository.class);

    @BeforeEach
    public void setUp(){
        atmService = new ATMService();
        atmService.setAccountsRepository(accountsRepository);
        atmService.setCurrencyUnitRepository(currencyUnitRepository);
    }

    @Test
    public void userLogin_userIdAndPinCorrect_returnTrue(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId", "1234", BigDecimal.ZERO, BigDecimal.ZERO)));
        EasyMock.replay(accountsRepository);
        assertTrue(atmService.userLogin("userId","1234"));
        EasyMock.verify();
    }

    @Test
    public void userLogin_userIdCorrectPinIncorrect_returnFalse(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId","",BigDecimal.ZERO,BigDecimal.ZERO)));
        EasyMock.replay(accountsRepository);
        assertFalse(atmService.userLogin("userId","1234"));
        EasyMock.verify();
    }

    @Test
    public void userLogin_userIdIncorrectPincorrect_returnFalse(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.empty());
        EasyMock.replay(accountsRepository);
        assertFalse(atmService.userLogin("userId","1234"));
        EasyMock.verify();
    }

    @Test
    public void getBalance_userIdExistsBalancePositive_return100(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId","",new BigDecimal(100),BigDecimal.ZERO)));
        EasyMock.replay(accountsRepository);
        assertEquals(new BigDecimal(100),atmService.getBalance("userId"));
        EasyMock.verify();
    }

    @Test
    public void getBalance_userIdExistsBalance0_return0(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId","",BigDecimal.ZERO,BigDecimal.ZERO)));
        EasyMock.replay(accountsRepository);
        assertEquals(new BigDecimal(0),atmService.getBalance("userId"));
        EasyMock.verify();
    }

    @Test
    public void getBalance_userIdExistsBalanceNegative_returnNeg100(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId","",new BigDecimal(-100),BigDecimal.ZERO)));
        EasyMock.replay(accountsRepository);
        assertEquals(new BigDecimal(-100),atmService.getBalance("userId"));
        EasyMock.verify();
    }

    @Test
    public void getBalance_userIdDoesNotExists_return0(){
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.empty());
        EasyMock.replay(accountsRepository);
        assertEquals(BigDecimal.ZERO,atmService.getBalance("userId"));
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_100_returntwo50() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(2, 0, 0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(100), BigDecimal.ZERO);
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(100));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);

        assertEquals(Arrays.asList(50,50),atmService.cashWithDraw("userId",new BigDecimal(100)));
        assertEquals(BigDecimal.ZERO,account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(0,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_60_returnthree20() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(1, 3, 0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(100), BigDecimal.ZERO);
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        currencyUnits = createCurrencyUnitList(currencyAmounts);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(110));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);

        assertEquals(Arrays.asList(20,20,20),atmService.cashWithDraw("userId",new BigDecimal(60)));
        assertEquals(new BigDecimal(40),account.getOpeningBalance());
        assertEquals(1,currencyUnits.get(0).getQuantity());
        assertEquals(0,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_overdrawbalanceUnderOverDraft_balanceNeg10() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(0, 3,5, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(100), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(110));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);

        assertEquals(Arrays.asList(20, 20, 20, 10, 10, 10, 10, 10),atmService.cashWithDraw("userId",new BigDecimal(110)));
        assertEquals(new BigDecimal(-10),account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(0,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_requestOverBalance_ReturnError() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(0, 3,5, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(90), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(110));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);
        try{
        atmService.cashWithDraw("userId",new BigDecimal(110));
        }catch(Exception e){
            assertEquals(e.getMessage(),"Requested amount exceeds user account balance");
        }
        assertEquals(new BigDecimal(90),account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(3,currencyUnits.get(1).getQuantity());
        assertEquals(5,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_requestOverATMBalance_ReturnError() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(0, 3,0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(90), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(60));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);
        try{
            atmService.cashWithDraw("userId",new BigDecimal(80));
        }catch(Exception e){
            assertEquals(e.getMessage(),"Requested amount exceeds current ATM balance");
        }
        assertEquals(new BigDecimal(90),account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(3,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_requestBelowMinimumUnitSize_ReturnError() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(0, 3,0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(90), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5).times(2);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(60));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);
        try{
            atmService.cashWithDraw("userId",new BigDecimal(3));
        }catch(Exception e){
            assertEquals(e.getMessage(),"Requested amount must be greater than 0 and divisible by the minimum currency unit: 5");
        }
        assertEquals(new BigDecimal(90),account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(3,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_requestNegAmount_ReturnError() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(0, 3,0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(90), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5).times(2);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(60));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);
        try{
            atmService.cashWithDraw("userId",new BigDecimal(-100));
        }catch(Exception e){
            assertEquals(e.getMessage(),"Requested amount must be greater than 0 and divisible by the minimum currency unit: 5");
        }
        assertEquals(new BigDecimal(90),account.getOpeningBalance());
        assertEquals(0,currencyUnits.get(0).getQuantity());
        assertEquals(3,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }

    @Test
    public void cashWithDrawTests_requestCurrencyUnavailable_ReturnError() throws Exception{
        List<Integer> currencyAmounts = Arrays.asList(1, 0,0, 0);
        List<CurrencyUnit> currencyUnits = createCurrencyUnitList(currencyAmounts);
        Account account = createAccount("userId", "userPin", new BigDecimal(90), new BigDecimal(10));
        EasyMock.expect(currencyUnitRepository.getMinimumCurrencyUnit()).andReturn(5);
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(account));
        EasyMock.expect(accountsRepository.getById("userId")).andReturn(account);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        currencyUnits = createCurrencyUnitList(currencyAmounts);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        currencyUnits = createCurrencyUnitList(currencyAmounts);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        currencyUnits = createCurrencyUnitList(currencyAmounts);
        EasyMock.expect(currencyUnitRepository.findAll(Sort.by(Sort.Direction.DESC, "unit"))).andReturn(currencyUnits);
        EasyMock.expect(currencyUnitRepository.getTotalAvailableCashAmount()).andReturn(new BigDecimal(50));
        EasyMock.expect(currencyUnitRepository.saveAll(currencyUnits)).andReturn(currencyUnits);
        EasyMock.expect(accountsRepository.save(account)).andReturn(account);
        EasyMock.replay(currencyUnitRepository,accountsRepository);
        try{
            atmService.cashWithDraw("userId",new BigDecimal(40));
        }catch(Exception e){
            assertEquals(e.getMessage(),"The requested amount was unavailable with the available currency");
        }
        assertEquals(new BigDecimal(90),account.getOpeningBalance());
        assertEquals(1,currencyUnits.get(0).getQuantity());
        assertEquals(0,currencyUnits.get(1).getQuantity());
        assertEquals(0,currencyUnits.get(2).getQuantity());
        assertEquals(0,currencyUnits.get(3).getQuantity());
        EasyMock.verify();
    }


    @Test
    public void testTotalAvailableAmount_postiveBalanceAndOverDraft() {
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId", "1234", new BigDecimal(100), BigDecimal.TEN)));
        EasyMock.replay(accountsRepository);
        assertEquals(110,atmService.getTotalAvailableAmount("userId").intValue());
        EasyMock.verify();
    }

    @Test
    public void testTotalAvailableAmount_NegativeBalanceAndPositiveOverDraft() {
        EasyMock.expect(accountsRepository.findById("userId")).andReturn(Optional.of(createAccount("userId", "1234", new BigDecimal(-100), BigDecimal.TEN)));
        EasyMock.replay(accountsRepository);
        assertEquals(-90,atmService.getTotalAvailableAmount("userId").intValue());
        EasyMock.verify();
    }

    private List<CurrencyUnit> createCurrencyUnitList(List<Integer> notes){
        List<CurrencyUnit> currencyList = new ArrayList<CurrencyUnit>();
        int[] currencies = new int[]{50, 20, 10, 5};
        for(int i=0;i<currencies.length;i++){
                currencyList.add(new CurrencyUnit(currencies[i],notes.get(i)));
        }
        return currencyList.stream().sorted(Comparator.comparingInt(CurrencyUnit::getUnit).reversed()).collect(Collectors.toList());
    }

    private Account createAccount(String userId, String userPin, BigDecimal balance, BigDecimal overDraft){
        Account account = new Account();
        account.setUserId(userId);
        account.setUserPin(userPin);
        account.setOpeningBalance(balance);
        account.setOverdraft(overDraft);
        return account;
    }
}
