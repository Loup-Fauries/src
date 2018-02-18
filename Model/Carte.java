package Model;

/**
 * Classe représentant une carte
 * 
 * @version: 1.0
 */
public class Carte {
	private boolean visible = false;
	private final Couleur couleur;
	private final Face face;
	private int valeur1;
	private int valeur2 = 11;
	
	public Carte( Face f, Couleur c) {
		couleur= c;
		face= f;
		switch (f) {
		case AS:
			valeur1 = 1;
			valeur2 = 11;
			break;
		case DEUX:
			valeur1 = 2;
			break;
		case TROIS:
			valeur1 = 3;
			break;
		case QUATRE:
			valeur1 = 4;
			break;
		case CINQ:
			valeur1 = 5;
			break;
		case SIX:
			valeur1 = 6;
			break;
		case SEPT:
			valeur1 = 7;
			break;
		case HUIT:
			valeur1 = 8;
			break;
		case NEUF:
			valeur1 = 9;
			break;
		case DIX:
		case VALET: case DAME: case ROI:
			valeur1 = 10;
		}
	}
	
	/**
	 * Permet de retourner une carte
	 * 
	 */
	public void setVisible(boolean v) {
		visible = v;
	}
	
	/**
	 * Retourne true si la carte est face visible, et false sinon
	 * 
	 */
	public boolean estVisible() {
		return visible;
	}
	
	/**
	 * Retourne la valeur de la carte, seulement si la carte est visible
	 * 
	 */
	public int getValeur() {
		if(estVisible()) return valeur1;
		return 0;
	}
	
	/**
	 * retourne la valeur de l'As
	 * 
	 */
	public int getValeurAs(int total) {
		if(estVisible() && Face.AS.equals(face)){
			if(total<10){
				return valeur2;
			}
			return valeur1;
		}
		return 0;
	}
	
	/**
	 * Retourne la face(1,2,3...Valet...) de la carte
	 * 
	 */
	public Face getFace(){
		return face;
	}
	
	/**
	 * Affiche les propriétés de la carte (Face et Couleur)
	 * Seulement si la carte est face visible, sinon affiche "Carte face cachée"
	 * 
	 */
	@Override
	public String toString() {
		if(estVisible()) {
			return face + " de " + couleur;
		}
		return "Carte face cachee";
	}
}
