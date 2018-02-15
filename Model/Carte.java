package Model;


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
	
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public boolean estVisible() {
		return visible;
	}
	
	public int getValeur() {
		if(estVisible()) return valeur1;
		return 0;
	}
	
	public int getValeurAs(int total) {
		if(estVisible() && Face.AS.equals(face)){
			if(total<10){
				return valeur2;
			}
			return valeur1;
		}
		return 0;
	}
	
	public Face getFace(){
		return face;
	}
	
	@Override
	public String toString() {
		if(estVisible()) {
			return face + " de " + couleur;
		}
		return "Carte face cachee";
	}
}
