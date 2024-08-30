package com.hokhanh.web.notification.hireConfirmation;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "hire_confirmations")
public class HireConfirmation {

	@Id
	private String id;
	private String expertId;
	private String hirerId;
	private Long jobTypeId;
	private double price;
	private LocalDate startDate;
	private LocalDate endDate;
}
