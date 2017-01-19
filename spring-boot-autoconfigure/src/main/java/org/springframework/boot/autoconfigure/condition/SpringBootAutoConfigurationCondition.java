/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.condition;

import org.springframework.context.annotation.ConditionContext;

/**
 * A special {@link SpringBootCondition SpringBootConditions} that can also be evaluated
 * when auto-configuration classes are first imported. Allows early evaluation saving ASM
 * bytecode parsing entirely.
 *
 * @author Phillip Webb
 * @since 1.5.0
 */
public abstract class SpringBootAutoConfigurationCondition extends SpringBootCondition {

	public final void apply(ConditionContext context, String[] autoConfigurationClasses,
			boolean[] skip, ConditionEvaluationReport report) {
		ConditionOutcome[] outcomes = new ConditionOutcome[skip.length];
		apply(context, autoConfigurationClasses, skip, outcomes);
		for (int i = 0; i < outcomes.length; i++) {
			if (outcomes[i] != null) {
				skip[i] = !outcomes[i].isMatch();
				if (skip[i]) {
					logOutcome(autoConfigurationClasses[i], outcomes[i]);
					if (report != null) {
						report.recordConditionEvaluation(autoConfigurationClasses[i],
								this, outcomes[i]);
					}
				}
			}
		}
	}

	protected abstract void apply(ConditionContext context,
			String[] autoConfigurationClasses, boolean[] skip,
			ConditionOutcome[] outcomes);

	// private void split(final ConditionContext context,
	// final String[] autoConfigurationClasses, final boolean[] skip,
	// final ConditionOutcome[] outcomes) {
	// final int split = skip.length / 2;
	// Thread t = new Thread() {
	//
	// @Override
	// public void run() {
	// for (int i = 0; i < split; i++) {
	// outcomes[i] = (skip[i] ? null
	// : getMatchOutcome(context, autoConfigurationClasses[i]));
	// }
	//
	// };
	//
	// };
	// t.start();
	// for (int i = split; i < outcomes.length; i++) {
	// outcomes[i] = (skip[i] ? null
	// : getMatchOutcome(context, autoConfigurationClasses[i]));
	// }
	// try {
	// t.join();
	// }
	// catch (InterruptedException e) {
	// Thread.currentThread().interrupt();
	// }
	// }
	//
	// /**
	// * Determine the outcome of the match along with suitable log output.
	// * @param context the condition context
	// * @param autoConfigurationClass the auto configuration class
	// * @return the condition outcome
	// */
	// protected abstract ConditionOutcome getMatchOutcome(ConditionContext context,
	// String autoConfigurationClass);

}
