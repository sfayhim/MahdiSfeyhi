package garages;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.PrintStream;
import java.util.*;

/**
 * Représente une voiture qui peut être stationnée dans des garages.
 */
@RequiredArgsConstructor
@ToString
public class Voiture {

	@Getter
	@NonNull
	private final String immatriculation;
	@ToString.Exclude // On ne veut pas afficher les stationnements dans toString
	private final List<Stationnement> myStationnements = new LinkedList<>();

	/**
	 * Fait rentrer la voiture au garage
	 * Précondition : la voiture ne doit pas être déjà dans un garage
	 *
	 * @param g le garage où la voiture va stationner
	 * @throws IllegalStateException Si déjà dans un garage
	 */
	public void entreAuGarage(Garage g) throws IllegalStateException {
		// Vérifier si la voiture est déjà dans un garage
		if (estDansUnGarage()) {
			throw new IllegalStateException("La voiture est déjà dans un garage");
		}

		Stationnement s = new Stationnement(this, g);
		myStationnements.add(s);
	}

	/**
	 * Fait sortir la voiture du garage
	 * Précondition : la voiture doit être dans un garage
	 *
	 * @throws IllegalStateException si la voiture n'est pas dans un garage
	 */
	public void sortDuGarage() throws IllegalStateException {
		// Vérifier si la voiture est dans un garage
		if (!estDansUnGarage()) {
			throw new IllegalStateException("La voiture n'est pas dans un garage");
		}
		
		// Trouver le dernier stationnement de la voiture et le terminer
		Stationnement dernierStationnement = myStationnements.get(myStationnements.size() - 1);
		dernierStationnement.terminer();
	}

	/**
	 * Calcule l'ensemble des garages visités par cette voiture
	 *
	 * @return l'ensemble des garages visités par cette voiture
	 */
	public Set<Garage> garagesVisites() {
		Set<Garage> garages = new HashSet<>();
		for (Stationnement stationnement : myStationnements) {
			garages.add(stationnement.getGarageVisite());
		}
		return garages;
	}

	/**
	 * Détermine si la voiture est actuellement dans un garage
	 *
	 * @return vrai si la voiture est dans un garage, faux sinon
	 */
	public boolean estDansUnGarage() {
		// Vrai si il y a des stationnements et le dernier stationnement est en cours
		return !myStationnements.isEmpty() && 
			   myStationnements.get(myStationnements.size() - 1).estEnCours();
	}

	/**
	 * Pour chaque garage visité, imprime le nom de ce garage suivi de la liste des
	 * stationnements dans ce garage
	 * <br>
	 * Exemple :
	 *
	 * <pre>
	 * Garage(name=Universite Champollion Albi):
	 * 		Stationnement{ entree=13/11/2024, sortie=13/11/2024 }
	 * Garage(name=ISIS Castres):
	 * 		Stationnement{ entree=13/11/2024, sortie=13/11/2024 }
	 * 		Stationnement{ entree=13/11/2024, en cours }
	 * </pre>
	 *
	 * @param out l'endroit où imprimer (ex: System.out pour imprimer dans la
	 *            console)
	 */

	/**
	 * Pour chaque garage visité, imprime le nom de ce garage suivi de la liste des
	 * stationnements dans ce garage
	 * <br>
	 * Exemple :
	 *
	 * <pre>
	 * Garage(name=Universite Champollion Albi):
	 * 		Stationnement{ entree=13/11/2024, sortie=13/11/2024 }
	 * Garage(name=ISIS Castres):
	 * 		Stationnement{ entree=13/11/2024, sortie=13/11/2024 }
	 * 		Stationnement{ entree=13/11/2024, en cours }
	 * </pre>
	 *
	 * @param out l'endroit où imprimer (ex: System.out pour imprimer dans la
	 *            console)
	 */
	public void imprimeStationnements(PrintStream out) {
		// Grouper les stationnements par garage
		Map<Garage, List<Stationnement>> stationnementsParGarage = new LinkedHashMap<>();
		
		// Parcourir tous les stationnements dans l'ordre chronologique
		for (Stationnement stationnement : myStationnements) {
			Garage garage = stationnement.getGarageVisite();
			stationnementsParGarage.computeIfAbsent(garage, k -> new ArrayList<>()).add(stationnement);
		}
		
		// Imprimer chaque garage et ses stationnements
		for (Map.Entry<Garage, List<Stationnement>> entry : stationnementsParGarage.entrySet()) {
			out.println(entry.getKey().toString() + ":");
			for (Stationnement stationnement : entry.getValue()) {
				out.println("\t\t" + stationnement.toString());
			}
		}
	}

}
