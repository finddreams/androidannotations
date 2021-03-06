/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.handler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import org.androidannotations.annotations.PreferenceHeaders;
import org.androidannotations.helper.AnnotationHelper;
import org.androidannotations.helper.IdValidatorHelper;
import org.androidannotations.holder.HasPreferenceHeaders;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.rclass.IRClass;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JVar;

public class PreferenceHeadersHandler extends BaseAnnotationHandler<HasPreferenceHeaders> {

	private final AnnotationHelper annotationHelper;

	public PreferenceHeadersHandler(ProcessingEnvironment processingEnvironment) {
		super(PreferenceHeaders.class, processingEnvironment);
		annotationHelper = new AnnotationHelper(processingEnvironment);
	}

	@Override
	protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
		validatorHelper.isPreferenceFragmentClassPresent(element, valid);
		validatorHelper.extendsPreferenceActivity(element, valid);
		validatorHelper.hasEActivity(element, validatedElements, valid);
		validatorHelper.resIdsExist(element, IRClass.Res.XML, IdValidatorHelper.FallbackStrategy.NEED_RES_ID, valid);
	}

	@Override
	public void process(Element element, HasPreferenceHeaders holder) throws Exception {
		JFieldRef headerId = annotationHelper.extractAnnotationFieldRefs(processHolder, element, getTarget(), rClass.get(IRClass.Res.XML), false).get(0);

		JBlock block = holder.getOnBuildHeadersBlock();
		JVar targetParam = holder.getOnBuildHeadersTargetParam();
		block.invoke("loadHeadersFromResource").arg(headerId).arg(targetParam);
		block.invoke(JExpr._super(), "onBuildHeaders").arg(targetParam);
	}
}
