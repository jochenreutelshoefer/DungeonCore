/*
 * Copyright (C) 2021 denkbares GmbH. All rights reserved.
 */

package de.jdungeon.dungeon.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interface to define a fact in ASP.
 * <p>
 * The current definition is limited to facts of depth 1, meaning that it is a predicate name and a number of literal
 * values, each a quoted string or an integer number.
 *
 * @author Volker Belli (denkbares GmbH)
 * @created 25.03.2021
 */
public final class Fact implements Comparable<Fact> {

    private final String predicate;
    private final Literal[] literals;

    private final Fact[] facts;

    private final boolean negated;
    private final String costs;
    public Fact(String predicate, Literal... literals) {
        this(predicate, false, null, literals, Collections.emptyList());
        // assert that the predicate is a valid symbol
        assertSymbol(predicate);
    }

    public Fact(String predicate, List<Fact> facts, Literal... literals) {
        this(predicate, false, null, literals, facts);
        // assert that the predicate is a valid symbol
        assertSymbol(predicate);
    }

    private Fact(String predicate, boolean negated, String costs, Literal[] literals, List<Fact> facts) {
        this.predicate = predicate;
        this.literals = literals;
        this.negated = negated;
        this.costs = costs;
        this.facts = facts.toArray(new Fact[facts.size()]);
    }

    public Fact[] getFacts() {
            return facts;
    }

    /**
     * Creates a new fact, from a specified textual expression, without performing any assertions on the text of the
     * fact. So be careful when using this factory method.
     *
     * @param factText the textual representation of the fact
     * @return the fact that compiles to the specified text
     */
    public static Fact createUnsafe(String factText) {
        factText = trim(factText);
        boolean negated = false;
        if (factText.length() > 4 && factText.startsWith("not") && Character.isWhitespace(factText.charAt(3))) {
            factText = trim(factText.substring(4));
            negated = true;
        }
        return new Fact(factText, negated, null, new Literal[0], Collections.emptyList());
    }

    /**
     * Creates a new fact, that assigns (binds) the two specified value or variable literals to each other.
     *
     * @param left  the left literal to be assigned
     * @param right the right literal to be assigned
     * @return the fact that assigns the two literals
     */
    public static Fact createAssign(Literal left, Literal right) {
        return createUnsafe(left.asASPCore2() + " = " + right.asASPCore2());
    }

    /**
     * Creates a new fact, that compares the two specified value or variable literals to each other.
     *
     * @param operator the compare operator, should be "=", "!=", "<", "<=", ">", ">="
     * @param left     the left literal to be assigned
     * @param right    the right literal to be assigned
     * @return the fact that assigns the two literals
     */
    public static Fact createCompare(String operator, Literal left, Literal right) {
        assert "=|!=|<|<=|>|>=".contains(operator);
        return createUnsafe(left.asASPCore2() + " " + operator + " " + right.asASPCore2());
    }

    /**
     * Creates a new fact, that combines the two specified value or variable literals with a mathematical operator, and
     * assigns it to the result variable.
     *
     * @param operator the compare operator, should be "=", "!=", "<", "<=", ">", ">="
     * @param left     the left literal to be assigned
     * @param right    the right literal to be assigned
     * @return the fact that assigns the two literals
     */
    public static Fact createMath(Literal result, String operator, Literal left, Literal right) {
        assert "*|/|+|-|**".contains(operator);
        return createUnsafe(result.asASPCore2() + " = " + left.asASPCore2() + " " + operator + " " + right.asASPCore2());
    }

    /**
     * Returns a negated copy of this fact. This fact will not be touched.
     */
    public Fact negate() {
        return new Fact(predicate, !negated, costs, literals, Arrays.asList(facts));
    }

    /**
     * Returns a copy of this fact with the specified costs. This fact will not be touched.
     */
    public Fact costs(int costs) {
        assert predicate.startsWith(":~");
        return new Fact(predicate, negated, String.valueOf(costs), literals, Arrays.asList(facts));
    }

    /**
     * Returns the predicate string of this fact.
     */
    public String getPredicate() {
        return predicate;
    }

    /**
     * Returns the number of literals of this predicate fact.
     */
    public int count() {
        return literals.length;
    }

    /**
     * Returns the literal at the specified index, of this predicate fact.
     */
    public Literal get(int index) {
        return literals[index];
    }

    @Override
    public String toString() {
        return asASPCore2();
    }

    /**
     * Returns the ASP code of this fact, rule, etc. If there are any costs for the fact, tehy are NOT (!) part of the
     * returned code fragment, and must be handled separately.
     *
     * @return the ASP code of this fact, without costs
     */
    public String asASPCore2() {
        String fact = (literals.length == 0) ? predicate : predicate + "(" +
                Arrays.stream(literals).map(Literal::asASPCore2).collect(Collectors.joining(", ")) + ")";
        return negated ? "not " + fact : fact;
    }

    @Nullable
    public String getCosts() {
        return costs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fact)) return false;
        Fact fact = (Fact) o;
        return Objects.equals(predicate, fact.predicate) && Arrays.equals(literals, fact.literals);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(predicate);
        result = 31 * result + Arrays.hashCode(literals);
        return result;
    }

    @Override
    public int compareTo(@NotNull Fact other) {
        Comparator<String> comparator = NumberAwareComparator.CASE_SENSITIVE;

        // compare the predicate name
        int result = comparator.compare(predicate, other.predicate);
        if (result != 0) return result;

        // compare each literal
        int len = Math.min(literals.length, other.literals.length);
        for (int i = 0; i < len; i++) {
            result = comparator.compare(literals[i].encoded, other.literals[i].encoded);
            if (result != 0) return result;
        }

        // if still the same, the shorter length is less
        return Integer.compare(literals.length, other.literals.length);
    }

    private static void assertVariable(String symbol) {
        if (!symbol.matches("(_*[A-Z][a-zA-Z0-9_']*)")) {
            throw new IllegalArgumentException("not a valid symbol: " + symbol);
        }
    }

    private static void assertSymbol(String symbol) {
        if (!symbol.matches("(_*[a-z][a-zA-Z0-9_']*)")) {
            throw new IllegalArgumentException("not a valid symbol: " + symbol);
        }
    }

    private static String trim(String s) {
        return s.trim();
    }

    private static boolean isQuoted(String s) {
        return s.startsWith("\"") && s.endsWith("\"");
    }

    private static void assertLiteral(String aspCode) {
        aspCode = trim(aspCode);
        if (aspCode.matches("(_*[a-z][a-zA-Z0-9_']*)|(-?[0-9]+(\\.\\.-?[0-9]+)?)")) return;
        if (isQuoted(aspCode)) return;
        throw new IllegalArgumentException("invalid literal: " + aspCode);
    }

    public static class Literal {
        private final String encoded;

        private Literal(String encoded) {
            this.encoded = encoded;
        }

        /**
         * Creates a new variable literal from the specified variable name.
         */
        public static Literal variable(String variableName) {
            assertVariable(variableName);
            return new Literal(variableName);
        }

        /**
         * Creates a new variable literal from the specified variable base name and a counter.
         */
        public static Literal variable(String variableName, int counter) {
            variableName += "_" + counter;
            assertVariable(variableName);
            return new Literal(variableName);
        }

        /**
         * Returns the "ignore" literal ('_').
         */
        public static Literal ignore() {
            return unsafe("_");
        }

        /**
         * Creates a new symbolic literal from the specified symbol.
         */
        public static Literal symbol(String symbol) {
            assertSymbol(symbol);
            return new Literal(symbol);
        }

        /**
         * Creates a new literal from the specified original code.
         */
        public static Literal raw(String aspCore2Code) {
            assertLiteral(aspCore2Code);
            return unsafe(aspCore2Code);
        }

        /**
         * Creates a new literal from the specified original code, without checking its validity. This may be required
         * for e.g. mathematical formulas and similar.
         */
        public static Literal unsafe(String aspCore2Code) {
            return new Literal(aspCore2Code);
        }

        /**
         * Creates a new text string literal from the specified text.
         */
        public static Literal string(String text) {
            return new Literal(quote(text));
        }

        public static String quote(String text) {
            return "\"" + text + "\"";
        }

        public static String unquote(String text) {
            if (!isQuoted(text)) return text;
            return text.substring(1, text.length() - 1);
        }

        /**
         * Creates a new integer numeric literal from the specified value.
         */
        public static Literal number(long value) {
            return new Literal(String.valueOf(value));
        }

        /**
         * Creates a new integer numeric range literal from the specified values.
         */
        public static Literal range(long from, long to) {
            if (from >= to) {
                throw new IllegalArgumentException("not a valid range: " + from + ".." + to);
            }
            return new Literal(from + ".." + to);
        }

        /**
         * Creates a new integer numeric range literal from the specified values, or a number if the range is atomic.
         */
        public static Literal rangeOrNumber(long from, long to) {
            return (from == to) ? number(from) : range(from, to);
        }

        /**
         * Returns the ASP-Core-2 source representation of the literal.
         */
        public String asASPCore2() {
            return encoded;
        }

        /**
         * Interprets the literal as a textual literal and returns the text of the symbol, the unquoted string, or the
         * number as text.
         */
        public String asString() {
            return unquote(encoded);
        }

        /**
         * Tries to interpret the literal as a number. If it is a string literal, the unquoted text will be tried to
         * parse as a number as well.
         */
        public int asNumber() {
            return Integer.parseInt(asString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Literal)) return false;
            Literal literal = (Literal) o;
            return Objects.equals(encoded, literal.encoded);
        }

        @Override
        public int hashCode() {
            return Objects.hash(encoded);
        }

        @Override
        public String toString() {
            return asASPCore2();
        }
    }

    static class NumberAwareComparator implements Comparator<String> {

        public static final Comparator<String> CASE_SENSITIVE = new NumberAwareComparator(true);
        public static final Comparator<String> CASE_INSENSITIVE = new NumberAwareComparator(false);

        private final boolean caseSensitive;

        private NumberAwareComparator(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        @SuppressWarnings("Duplicates")
        @Override
        public int compare(String s1, String s2) {
            if (s1 == null) s1 = "";
            if (s2 == null) s2 = "";
            int n1 = s1.length(), n2 = s2.length();
            int i1 = 0, i2 = 0;
            for (; i1 < n1 && i2 < n2; i1++, i2++) {

                // get character at string 1, or number (as negative number)
                int c1 = s1.charAt(i1);
                if (Character.isDigit(c1)) {
                    c1 = 0;
                    for (int c; i1 < n1 && Character.isDigit(c = s1.charAt(i1)); i1++) {
                        c1 = c1 * 10 - Character.digit(c, 10);
                    }
                    i1--;
                }

                // get character at string 2, or number (as negative number)
                int c2 = s2.charAt(i2);
                if (Character.isDigit(c2)) {
                    c2 = 0;
                    for (int c; i2 < n2 && Character.isDigit(c = s2.charAt(i2)); i2++) {
                        c2 = c2 * 10 - Character.digit(c, 10);
                    }
                    i2--;
                }

                // if both are numbers, prefer the lower one (with negation corrected)
                if (c1 < 0 && c2 < 0) {
                    int compare = Integer.compare(-c1, -c2);
                    if (compare != 0) return compare;
                    continue;
                }

                // if mixed, treat the number as a number in the alphabet
                if (c1 < 0) {
                    return Integer.compare('0', c2);
                }
                if (c2 < 0) {
                    return Integer.compare(c1, '0');
                }

                // if both are characters, do normally
                if (c1 != c2) {
                    if (caseSensitive) return c1 - c2;
                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);
                    if (c1 != c2) {
                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);
                        if (c1 != c2) {
                            return c1 - c2;
                        }
                    }
                }
            }

            // if any of the strings reach the end, the consumed one comes first
            return (n1 - i1) - (n2 - i2);
        }
    }
}
