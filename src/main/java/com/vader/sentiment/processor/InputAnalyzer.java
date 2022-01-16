/*
 * MIT License
 *
 * Copyright (c) 2021 Animesh Pandey
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.vader.sentiment.processor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class replaces the Lucene analyzer that is applied on the input string
 * in the original VaderSentimentJava.
 */
class InputAnalyzer implements InputAnalyzerInterface {

	/**
	 * This function splits a string into tokens from the white space.
	 *
	 * @param inputString   The input string to be pre-processed into tokens
	 * @param tokenConsumer The consumer of the tokens
	 * @throws IOException tokenizer encounters any error
	 */
	@Override
	public void keepPunctuation(final String inputString, final Consumer<String> tokenConsumer) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));

		String line = bufReader.readLine();

		while (line != null) {
			String[] tokens = line.split("[\\s]+");

			for (String token : tokens) {
				if (token.length() > 1)
					tokenConsumer.accept(token);
			}

			line = bufReader.readLine();
		}

		bufReader.close();
		inputStream.close();
	}

	/**
	 * This function splits a string into a tokens from the white space and removes punctuation.
	 *
	 * @param inputString   The input string to be pre-processed into tokens
	 * @param tokenConsumer The consumer of the tokens
	 * @throws IOException tokenizer encounters any error
	 */
	@Override
	public void removePunctuation(final String inputString, final Consumer<String> tokenConsumer) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));

		String line = bufReader.readLine();

		while (line != null) {
			String[] tokens;

			Matcher m = Pattern.compile("(\\p{Punct}+)(\\s+|$)").matcher(line);
			String noPunctuation = m.replaceAll(" ");

			Matcher n = Pattern.compile("(^|\\s+)(\\p{Punct}+)").matcher(noPunctuation);
			noPunctuation = n.replaceAll(" ");

			tokens = noPunctuation.split("[\\s]+");

			for (String token : tokens) {
				if (token.length() > 1)
					tokenConsumer.accept(token);
			}

			line = bufReader.readLine();
		}

		bufReader.close();
		inputStream.close();
	}
}
