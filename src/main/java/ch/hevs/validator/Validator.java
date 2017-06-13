package ch.hevs.validator;


//utlisé pour valider certains champs
public class Validator {
	
	public static boolean isValideField(String texte){
		if(texte.contains("/"))
			return false;
		if(!isNotEmpty(texte))
			return false;
		return true;
	}

	
	public static boolean isNotEmpty(String texte){
		if(texte.trim().isEmpty())
			return false;
		return true;
	}
	
}
