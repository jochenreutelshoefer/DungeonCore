/*
 * Copyright (C) 2021 denkbares GmbH. All rights reserved.
 */

package de.jdungeon.dungeon.builder.asp;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the result of a ASP solver.
 *
 * @author Volker Belli (denkbares GmbH)
 * @created 25.03.2021
 */
public interface Result extends Iterable<Fact> {

	/**
	 * Returns if the solver has found a valid model for the asp program, according to the input base facts.
	 */
	boolean hasModel();

	String getFullOutput();

	/**
	 * Returns all facts of the model. If there are no facts, an empty set is returned.
	 *
	 * @return the facts of the model
	 */
	@NotNull
	Collection<Fact> getFacts();

	int getSolutionNumber();

	/**
	 * Returns the facts of the model associated to the specified predicate. If there are no suchs facts, an empty set
	 * is returned.
	 *
	 * @param predicate     the predicate name and argument count of the facts to get
	 * @param argumentCount the number of arguments of the predicate to get the facts for
	 * @return the facts of that predicate and argument count
	 */
	@NotNull
	default Collection<Fact> getFacts(@NotNull String predicate, int argumentCount) {
		return stream().filter(fact -> argumentCount == fact.count()
				&& Objects.equals(predicate, fact.getPredicate())).collect(Collectors.toList());
	}

	@NotNull
	@Override
	default Iterator<Fact> iterator() {
		return getFacts().iterator();
	}

	@NotNull
	default Stream<Fact> stream() {
		return getFacts().stream();
	}
}
