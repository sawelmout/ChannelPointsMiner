package fr.raksrinana.channelpointsminer.util;

import fr.raksrinana.channelpointsminer.Main;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.Properties;

@Log4j2
public class GitProperties{
	private static Properties gitProperties;
	
	public static String getBranch(){
		return getProperties().getProperty("git.branch", "UnknownBranch");
	}
	
	public static String getCommitId(){
		return getProperties().getProperty("git.commit.id.abbrev", "UnknownCommit");
	}
	
	public static String getVersion(){
		return getProperties().getProperty("git.build.version", "UnknownVersion");
	}
	
	private static Properties getProperties(){
		if(Objects.isNull(gitProperties)){
			gitProperties = loadProperties("/git.properties");
		}
		return gitProperties;
	}
	
	@NotNull
	private static Properties loadProperties(@NotNull String resource){
		var properties = new Properties();
		try{
			var versionPropertiesFile = Main.class.getResource(resource);
			if(Objects.nonNull(versionPropertiesFile)){
				try(var is = versionPropertiesFile.openStream()){
					properties.load(is);
				}
			}
		}
		catch(Exception e){
			log.warn("Error reading properties from {}", resource, e);
		}
		return properties;
	}
}
