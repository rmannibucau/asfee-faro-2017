package org.talend.kickoff.faro2017.front.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotePage {
    private List<QuoteModel> items;
    private long total;
}
