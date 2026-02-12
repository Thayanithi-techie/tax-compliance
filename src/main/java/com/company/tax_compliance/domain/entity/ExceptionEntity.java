package com.company.tax_compliance.domain.entity;

import com.company.tax_compliance.domain.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exceptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionEntity {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String transactionId;

	    @Column(nullable = false)
	    private String customerId;

	    @Column(nullable = false)
	    private String ruleName;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Severity severity;

	    @Column(nullable = false)
	    private String message;

	    private LocalDateTime createdAt;
}

