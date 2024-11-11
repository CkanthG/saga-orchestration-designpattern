package com.sree.payment.controller;

import com.sree.payment.dto.PaymentRequestDTO;
import com.sree.payment.dto.PaymentResponseDTO;
import com.sree.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return ResponseEntity.ok(paymentService.credit(paymentRequestDTO));
    }

    @PostMapping("/debit")
    public ResponseEntity<PaymentResponseDTO> debit(@RequestBody PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.debit(paymentRequestDTO));
    }
}
