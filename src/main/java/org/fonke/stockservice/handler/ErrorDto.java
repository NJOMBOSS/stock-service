package org.fonke.stockservice.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fonke.stockservice.exeception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

// Cette classe nous permettra de renvoyer un message d'erreurs personnalisés au client

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private Integer httpCode;

    private ErrorCodes code;

    private String message;

    private List<String> errors = new ArrayList<>();
}
