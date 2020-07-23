package com.piatek.rafal.account_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACCOUNT",
        uniqueConstraints = {@UniqueConstraint(columnNames={"CURRENCY", "CLIENT_ID"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @SequenceGenerator(name = "accountSeq", sequenceName = "ACCOUNT_SEQ")
    @GeneratedValue(generator = "accountSeq")
    private long id;

    @NotNull
    @EqualsAndHashCode.Include
    private String currency;

    @NotNull
    private double amount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    @EqualsAndHashCode.Include
    @JsonIgnore
    private Client client;

}
