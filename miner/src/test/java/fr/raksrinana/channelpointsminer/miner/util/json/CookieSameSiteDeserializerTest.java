package fr.raksrinana.channelpointsminer.miner.util.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import fr.raksrinana.channelpointsminer.miner.tests.ParallelizableTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

@ParallelizableTest
class CookieSameSiteDeserializerTest extends DeserializerTest<String>{
	private static Stream<Arguments> mappings(){
		return Stream.of(
				Arguments.arguments("no_restriction", "None"),
				Arguments.arguments("other", "other")
		);
	}
	
	@ParameterizedTest
	@MethodSource("mappings")
	void noRestrictionValue(String value, String expected){
		assertThat(deserialize("\"%s\"".formatted(value))).isEqualTo(expected);
	}
	
	@Test
	void empty(){
		assertThat(deserialize("\"\"")).isNull();
	}
	
	@Override
	protected JsonDeserializer<String> getDeserializer(){
		return new CookieSameSiteDeserializer();
	}
}