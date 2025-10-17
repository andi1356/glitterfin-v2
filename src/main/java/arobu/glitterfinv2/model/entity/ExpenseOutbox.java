package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseOutboxEvent;
import arobu.glitterfinv2.model.entity.meta.ExpenseOutboxStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


public class ExpenseOutbox {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    ExpenseOutboxEvent event;

    @OneToOne
    ExpenseEntry expenseId;

    @Enumerated(EnumType.STRING)
    ExpenseOutboxStatus status;

    @CreatedDate
    ZonedDateTime timestamp;
}
