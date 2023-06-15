package br.com.marcioss.loans.controller;

import br.com.marcioss.loans.config.LoansServiceConfig;
import br.com.marcioss.loans.model.Customer;
import br.com.marcioss.loans.model.Loans;
import br.com.marcioss.loans.model.Properties;
import br.com.marcioss.loans.repository.LoansRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoansController {


    private Logger logger = LoggerFactory.getLogger(LoansController.class);
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private LoansServiceConfig serviceConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestHeader("marciossbank-correlation-id") String correlationId,
                                       @RequestBody Customer customer) {
        logger.info("getLoansDetails() method started");
        List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
        logger.info("getLoansDetails() method ended");
        return loans;
    }

    @GetMapping("/loans/properties")
    public String loansProperties() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(serviceConfig.getMsg(), serviceConfig.getBuildVersion(),
                serviceConfig.getMailDetails(), serviceConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }
}
