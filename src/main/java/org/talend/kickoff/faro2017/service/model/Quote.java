package org.talend.kickoff.faro2017.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kickoff_quote")
@NamedQueries({
        @NamedQuery(name = "Quote.countAll", query = "select count(q) from Quote q"),
        @NamedQuery(name = "Quote.findAll", query = "select q from Quote q order by q.symbol")
})
public class Quote {
    @Id
    private String symbol;

    private double price;

    @Override
    public boolean equals(final Object other) {
        return this == other || other != null && getClass() == other.getClass() && Objects.equals(symbol,
                Quote.class.cast(other).symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
