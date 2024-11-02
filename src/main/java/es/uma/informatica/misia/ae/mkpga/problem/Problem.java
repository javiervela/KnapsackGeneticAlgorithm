package es.uma.informatica.misia.ae.mkpga.problem;

import java.util.Random;

public interface Problem {
	double evaluate(Individual individual);

	Individual generateRandomIndividual(Random rnd);

	double getOptimalValue();
}
