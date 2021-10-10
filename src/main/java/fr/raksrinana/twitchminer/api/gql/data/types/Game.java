package fr.raksrinana.twitchminer.api.gql.data.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonTypeName("Game")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Game extends GQLType{
	@JsonProperty("id")
	@NotNull
	private String id;
	@JsonProperty("displayName")
	@Nullable
	private String displayName;
	@JsonProperty("name")
	@NotNull
	private String name;
}
