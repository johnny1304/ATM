package com.ZinkWorks.TechnicalProblem.restService;

import com.ZinkWorks.TechnicalProblem.DTO.ATMRequest;
import com.ZinkWorks.TechnicalProblem.DTO.ATMResponse;
import com.ZinkWorks.TechnicalProblem.DTO.ATMWithDrawResponse;
import com.ZinkWorks.TechnicalProblem.service.ATMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

@RestController
@Validated
public class ATMRestService {

    @Autowired
    private ATMService atmService;

    @PostMapping("/balanceCheck")
    public ResponseEntity<ATMResponse> balanceCheck(@Valid @RequestBody ATMRequest atmRequest){
        System.out.println(atmRequest);
        ATMResponse responseDTO = new ATMResponse();
         if(atmService.userLogin(atmRequest.getUserId(),atmRequest.getUserPin())){
            responseDTO.setCurrentBalance(atmService.getBalance(atmRequest.getUserId()));
            responseDTO.setTotalAvailableBalance(atmService.getTotalAvailableAmount(atmRequest.getUserId()));
        }else{
             responseDTO.addError("User ID or User Pin is incorrect please try again");
        }
       return new ResponseEntity<ATMResponse>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ATMWithDrawResponse> withDraw(@Valid @RequestBody ATMRequest atmRequest){
        System.out.println(atmRequest);
        ATMWithDrawResponse responseDTO = new ATMWithDrawResponse();
        if(atmService.userLogin(atmRequest.getUserId(),atmRequest.getUserPin())){
            if(Objects.nonNull(atmRequest.getAmount())) {
                try {
                    responseDTO.setNoteList(atmService.cashWithDraw(atmRequest.getUserId(), atmRequest.getAmount()));
                    responseDTO.setBalance(atmService.getBalance(atmRequest.getUserId()));
                }catch(Exception e){
                    responseDTO.addError(e.getMessage());
                }
            }else{
                responseDTO.addError("Amount value must not be empty");
            }
        }else{
            responseDTO.addError("User ID or User Pin is incorrect please try again");
        }
        return new ResponseEntity<ATMWithDrawResponse>(responseDTO,HttpStatus.OK);
    }
}
