package com.sree.payment.service;

import com.sree.payment.dto.PaymentRequestDTO;
import com.sree.payment.dto.PaymentResponseDTO;
import com.sree.payment.dto.PaymentStatus;
import com.sree.payment.entity.Payment;
import com.sree.payment.exception.InSufficientAmountException;
import com.sree.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.sree.payment.dto.PaymentStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponseDTO debit(PaymentRequestDTO paymentRequestDTO) {
        Optional<Payment> userPayment = paymentRepository.findByUserId(paymentRequestDTO.userId());
        if (userPayment.isEmpty()) {
            return new PaymentResponseDTO(
                    paymentRequestDTO.userId(),
                    paymentRequestDTO.orderId(),
                    paymentRequestDTO.amount(),
                    paymentRequestDTO.quantity(),
                    PAYMENT_REJECTED
            );
        } else {
            Payment payment = userPayment.get();
            log.info("Inside Payment MS - Balance : {}", payment.getAmount());
            if (payment.getAmount() >= (paymentRequestDTO.amount() * paymentRequestDTO.quantity())) {
                Payment paymentObject = new Payment();
                paymentObject.setPaymentId(payment.getPaymentId());
                paymentObject.setOrderId(paymentRequestDTO.orderId());
                paymentObject.setUserId(paymentRequestDTO.userId());
                paymentObject.setQuantity(payment.getQuantity());
                paymentObject.setAmount(payment.getAmount() - (paymentRequestDTO.amount() * paymentRequestDTO.quantity()));
                paymentRepository.save(paymentObject);
                log.info("Payment Approved");
                return new PaymentResponseDTO(
                        paymentRequestDTO.userId(),
                        paymentRequestDTO.orderId(),
                        paymentRequestDTO.amount(),
                        paymentRequestDTO.quantity(),
                        PAYMENT_APPROVED
                );
            } else {
                log.info("Payment Rejected");
                return new PaymentResponseDTO(
                        paymentRequestDTO.userId(),
                        paymentRequestDTO.orderId(),
                        paymentRequestDTO.amount(),
                        paymentRequestDTO.quantity(),
                        PAYMENT_REJECTED
                );
            }
        }
    }

    public String credit(PaymentRequestDTO paymentRequestDTO) {
        Optional<Payment> payment = paymentRepository.findByUserId(paymentRequestDTO.userId());
        if (payment.isEmpty()) {
            paymentRepository.save(
              new Payment(
                      null,
                      paymentRequestDTO.userId(),
                      paymentRequestDTO.orderId(),
                      paymentRequestDTO.quantity(),
                      paymentRequestDTO.amount()
              )
            );
            return "Credit Added Successfully";
        } else {
            Payment paymentResponse = payment.get();
            paymentRepository.save(
                    new Payment(
                            paymentResponse.getPaymentId(),
                            paymentRequestDTO.userId(),
                            paymentRequestDTO.orderId(),
                            paymentRequestDTO.quantity(),
                            paymentResponse.getAmount() + (paymentRequestDTO.amount() * paymentRequestDTO.quantity())
                    )
            );
            return "Credit Updated Successfully";
        }
    }
}
