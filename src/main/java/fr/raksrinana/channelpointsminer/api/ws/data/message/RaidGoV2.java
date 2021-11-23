package fr.raksrinana.channelpointsminer.api.ws.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.raksrinana.channelpointsminer.api.ws.data.message.subtype.Raid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("raid_go_v2")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaidGoV2 extends IMessage{
	@JsonProperty("raid")
	@NotNull
	private Raid raid;
}