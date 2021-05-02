/*
 * Copyright (C) 2021 denkbares GmbH. All rights reserved.
 */

package de.jdungeon.dungeon.builder.asp;

import com.denkbares.strings.QuoteSet;
import com.denkbares.strings.StringFragment;
import com.denkbares.strings.Strings;
import com.denkbares.utils.Exec;
import com.denkbares.utils.Exec.OutputBuffer;
import com.denkbares.utils.Log;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of an answer set programming solver, that uses clingo to create a valid answer set model of a asp
 * program and a set of base facts.
 *
 * @author Volker Belli (denkbares GmbH)
 * @created 25.03.2021
 */
public class ShellASPSolver {

	private final String aspProgram;

	public ShellASPSolver(String aspProgram) {
		this.aspProgram = aspProgram;
	}

	@NotNull
	public Result solve(boolean parseOutput) throws InterruptedException {
		// build description of the input base facts

		// start clingo with both, program and facts
		// TODO: use non-shell api for clingo, this is only for trial purposes
		// TODO: prepare grounded clingo instance with the program at constructor, and only apply the facts here
		String fullProgram =  aspProgram;
		Throwable error = null;
		OutputBuffer out = new OutputBuffer();
		OutputBuffer err = new OutputBuffer();
		final int[] exitCode = new int[1];
		try {
			Exec execution = new Exec("/Users/jochenreutelshoefer/Downloads/clingo-5.4.0-macos-x86_64/clingo")
					.console(false).input(fullProgram).error(err).output(out);
			execution.runAsync();

			// wait some time
			Thread.sleep(10 * 1000);

			/*
			execution.exit(integer -> {
				exitCode[0] = integer.intValue();
				Log.info("Exit code was: "+integer.intValue());});
			 */
			//String consoleOutput = out.getConsoleOutput();
			//Log.info("Console output: "+consoleOutput);
			execution.destroy();
			//if (!ExitCode.E_SAT.isSet(exitCode[0])) {
			//	return DefaultResult.failed(Collections.emptyList(), out.getConsoleOutput());}
		}
		catch (IOException e) {
			Log.severe("cannot run clingo inference", e);
			error = e;
		}

		// if we are only interested in the pure result, terminate here
		String consoleOutput = out.getConsoleOutput().trim();
		ClingoOutputParser parser = new ClingoOutputParser(consoleOutput);
		if (!parseOutput) {
			return parser.isSatisfiable()
					? DefaultResult.satisfiable(Collections.emptyList())
					: DefaultResult.unsatisfiable(Collections.emptyList());
		}

		// parse the clingo results and return a result instance here (if satisfiable
		return new DefaultResult(Collections.emptyList(), parser.getModel(), parser.fullOutput);
	}

	private static class ClingoOutputParser {

		private final String fullOutput;
		private final boolean satisfiable;
		private String modelOutput = null;
		private int solutionNumber = -1;

		public ClingoOutputParser(String fullOutput) {
			this.fullOutput = fullOutput;

			String answerBeginKey = "Answer:";
			this.satisfiable = fullOutput.contains(answerBeginKey);
			if(satisfiable) {

				int beginLastModelIndex = fullOutput.lastIndexOf(answerBeginKey);
				int optimizationKeywordIndex = fullOutput.lastIndexOf("Optimization:");
				String answerOut = fullOutput.substring(beginLastModelIndex, optimizationKeywordIndex);
				Matcher matcher = Pattern.compile(answerBeginKey + "\\s*(\\d+)\n").matcher(answerOut);
				matcher.find();
				String answerNumberString = matcher.group(1);
				this.solutionNumber = Integer.parseInt(answerNumberString);
				this.modelOutput = answerOut.substring(matcher.group().length());
			}
		}

		public boolean isSatisfiable() {
			return satisfiable;
		}

		public String getFullOutput() {
			return fullOutput;
		}

		private Collection<Fact> getModel() {
			if (!satisfiable) return Collections.emptySet();

			List<Fact> result = new ArrayList<>();
			for (StringFragment factString : Strings.splitUnquoted(modelOutput, " ")) {
				String text = factString.getContentTrimmed();

				// only accept predicate facts
				int open = text.indexOf('(');
				if (open == -1) continue;

				// collect literals
				String factContent = text.substring(open + 1, text.length() - 1);
				Fact fact = createFact(text, open, factContent);
				result.add(fact);
			}

			// sort the facts for better debugging and printing
			Collections.sort(result);
			return result;
		}
	}

	@NotNull
	private static Fact createFact(String text, int open, String factContent) {
		List<StringFragment> literals = Strings.splitUnquoted(factContent, ",", new QuoteSet(
		'(',')'));

		// create fact instance
		Fact.Literal[] factLiterals = literals.stream().filter(s -> ! Strings.containsUnquoted(s.getContent(), "(")).map(StringFragment::getContentTrimmed).map(Fact.Literal::raw).toArray(Fact.Literal[]::new);
		Fact[] subFacts = literals.stream().filter(s -> Strings.containsUnquoted(s.getContent(), "(")).map(StringFragment::getContentTrimmed).map(s -> createFact(s, s.indexOf('('), s.substring(s.indexOf('(') + 1, s.length() - 1))).toArray(Fact[]::new);
		Fact fact = new Fact(text.substring(0, open), Arrays.asList(subFacts), factLiterals);
		return fact;
	}

	/**
	 * Exit codes are taken from
	 * <a href="http://docs.ros.org/en/hydro/api/clasp/html/namespaceClasp_1_1Cli.html#a752ef91459428225c6169fb8bca75451">clasp
	 * CLI source</a>.
	 */
	public enum ExitCode {
		/**
		 * Satisfiability of problem not known; search not started.
		 */
		E_UNKNOWN(0, "Satisfiability of problem not known; search not started."),
		/**
		 * Run was interrupted.
		 */
		E_INTERRUPT(1, "Run was interrupted."),
		/**
		 * At least one model was found.
		 */
		E_SAT(10, "At least one model was found."),
		/**
		 * Search-space was completely examined.
		 */
		E_EXHAUST(20, "Search-space was completely examined."),
		/**
		 * Run was interrupted by out of memory exception.
		 */
		E_MEMORY(33, "Run was interrupted by out of memory exception."),
		/**
		 * Run was interrupted by internal error.
		 */
		E_ERROR(65, "Run was interrupted by internal error."),
		/**
		 * Search not started because of syntax or command line error.
		 */
		E_NO_RUN(128, "Search not started because of syntax or command line error.");

		private final int bitSet;
		private final String message;

		ExitCode(int bitSet, String message) {
			this.bitSet = bitSet;
			this.message = message;
		}

		/**
		 * Returns all exit code instances that are coded in the specified actual exit code.
		 *
		 * @param exitCode the actual exit code to be parsed
		 * @return the coded exit code items
		 */
		@NotNull
		public static EnumSet<ExitCode> parseExitCode(int exitCode) {
			return exitCodes(exitCode).collect(Collectors.toCollection(() -> EnumSet.noneOf(ExitCode.class)));
		}

		/**
		 * Returns all exit code instances that are coded in the specified actual exit code.
		 *
		 * @param exitCode the actual exit code to be parsed
		 * @return the coded exit code items
		 */
		@NotNull
		private static Stream<ExitCode> exitCodes(int exitCode) {
			return Arrays.stream(values()).filter(ec -> ec.isSet(exitCode));
		}

		/**
		 * Returns true if this exit code in part of the specified actual exit code.
		 *
		 * @return if this exit code is part of the specified actual exit code
		 */
		public boolean isSet(int actualExitCode) {
			// ExitCode.E_UNKNOWN hat not bitSet, so we only accept if code is 0
			if (this == E_UNKNOWN) return actualExitCode == 0;

			// ExitCode.E_INTERRUPT is a part of others, so we only accept if no other is set
			if (this == E_INTERRUPT) {
				if (E_MEMORY.isSet(actualExitCode) || E_ERROR.isSet(actualExitCode)) return false;
			}

			// otherwise check bitset
			return (actualExitCode & bitSet) == bitSet;
		}

		/**
		 * Returns the human readable message for the exit code, taken from clasp documentation.
		 *
		 * @return the human readable message text
		 */
		public String getMessage() {
			return message;
		}
	}
}
