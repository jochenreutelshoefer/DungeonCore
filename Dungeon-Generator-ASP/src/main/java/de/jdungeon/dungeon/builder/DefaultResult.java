/*
 * Copyright (C) 2021 denkbares GmbH. All rights reserved.
 */

package de.jdungeon.dungeon.builder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of a clingo reasoning result, that matches the derived facts to the knowledge base.
 *
 * @author Volker Belli (denkbares GmbH)
 * @created 11.03.2021
 */
public class DefaultResult implements Result {

	private final Collection<Fact> inputs;
	private final Collection<Fact> outputs;
	private String fullOutput;

	DefaultResult(Collection<Fact> inputs, Collection<Fact> outputs, String fullOutput) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.fullOutput = fullOutput;
	}

	/**
	 * Returns a new Result for the specified input facts, that represents that the input has been satisfied, but
	 * provide no further information about the satisfied model itself.
	 *
	 * @param inputs the inputs that have been solved
	 * @return a result that states that the model has been satisfied
	 */
	public static DefaultResult satisfiable(Collection<Fact> inputs) {
		return new DefaultResult(inputs, Collections.singleton(new Fact("satisfied")), null);
	}

	/**
	 * Returns a new Result for the specified input facts, that represents that the input could not been satisfied.
	 *
	 * @param inputs the inputs that have failed to been solved
	 * @return a result that states that the model is unsatisfiable
	 */
	public static DefaultResult unsatisfiable(Collection<Fact> inputs) {
		return new DefaultResult(inputs, Collections.emptySet(), null);
	}

	/**
	 * Returns a new Result for the specified input facts, that represents an error that prevent the solver from
	 * solving.
	 *
	 * @param inputs    the inputs that have failed to been solved
	 * @return a result that states that the model is unsatisfiable
	 */
	public static DefaultResult failed(Collection<Fact> inputs, String output) {
		return new DefaultResult(inputs, Collections.emptySet(), output);
	}

	@Override
	public boolean hasModel() {
		return (outputs != null) && !outputs.isEmpty();
	}

	@Override
	public String getFullOutput() {
		return this.fullOutput;
	}

	@Override
	@NotNull
	public Collection<Fact> getFacts() {
		return outputs;
	}
}
