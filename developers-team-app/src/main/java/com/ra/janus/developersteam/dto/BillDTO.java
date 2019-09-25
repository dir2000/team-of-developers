package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class BillDTO {
    private long id;
    @NotNull
    private Date docDate;

    public BillDTO(Bill bill) {
        this(bill.getId(), bill.getDocDate());
    }
}
