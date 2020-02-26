package pl.michal.pacholczyk.remotexmlanalyser.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {

    @NotNull(message = "URL cannot be null.")
    @Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].xml", message = "Given URL does not have valid pattern!")
    private String url;

}
