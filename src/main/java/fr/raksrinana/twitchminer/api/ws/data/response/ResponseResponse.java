package fr.raksrinana.twitchminer.api.ws.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.util.Objects;

@JsonTypeName("RESPONSE")
@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class ResponseResponse extends TwitchWebSocketResponse{
	@JsonProperty("error")
	private String error;
	@JsonProperty("nonce")
	private String nonce;
	
	public boolean hasError(){
		return Objects.nonNull(getError()) && !getError().isBlank();
	}
}
