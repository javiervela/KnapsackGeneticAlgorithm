package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public interface Problem {
	double evaluate (Individual individual);
	Individual generateRandomIndividual(Random rnd);
}
