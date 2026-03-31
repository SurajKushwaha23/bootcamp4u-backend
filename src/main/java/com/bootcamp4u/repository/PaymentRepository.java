package com.bootcamp4u.repository;

import com.bootcamp4u.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Used by webhook listeners to update payment status based on gateway callbacks
    Optional<Payment> findByPaymentGatewayRef(String paymentGatewayRef);
}
