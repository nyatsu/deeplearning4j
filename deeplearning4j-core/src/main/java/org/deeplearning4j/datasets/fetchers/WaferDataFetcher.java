/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.datasets.fetchers;

import java.io.IOException;

import org.deeplearning4j.base.WaferUtils;


public class WaferDataFetcher extends BaseDataFetcher {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4566329799221375262L;
	public final static int NUM_EXAMPLES = 2000;
	public final static int NUM_TRAIN = 1600;
	private Boolean isTest = false;
/*	public WaferDataFetcher() {
		numOutcomes = 15;
		inputColumns = 210;
		totalExamples = NUM_EXAMPLES;
	}*/

	public WaferDataFetcher(Boolean isTest) {
		// TODO 自动生成的构造函数存根
		this.isTest = isTest;
		numOutcomes = 15;
		inputColumns = 210;
		totalExamples = NUM_EXAMPLES;
	}

	@Override
	public void fetch(int numExamples) {
		if (isTest){
			this.setCursor(NUM_TRAIN);		//若是Test过程，则移动cursor到测试集合开始
		}
		int from = cursor;		//起始为0，cursor默认为0
		
		int to = cursor + numExamples;		//终止为numExample
		if(to > totalExamples)
			to = totalExamples;
		
		try {
			initializeCurrFromList(WaferUtils.loadWafer(from, to));		//该函数从list<DataSet>生成DataSet
			cursor += numExamples;		//cursor移动到DataSet尾
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load EQ2000.csv");
		}
		
	}


}
