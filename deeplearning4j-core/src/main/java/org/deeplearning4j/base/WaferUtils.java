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

package org.deeplearning4j.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.core.io.ClassPathResource;

public class WaferUtils {

   public static List<DataSet> loadWafer(int from,int to) throws IOException {
       ClassPathResource resource = new ClassPathResource("/EQ2000.csv");
       @SuppressWarnings("unchecked")
       List<String> lines = IOUtils.readLines(resource.getInputStream());		//one entry per line
       List<DataSet> list = new ArrayList<>();
       INDArray ret = Nd4j.ones(Math.abs(to - from), 210);		//numExample x numFeature ?
       List<String> outcomeTypes = new ArrayList<>();
       double[][] outcomes = new double[lines.size()][15];		//用numExample x 分类数 的数组存放分类结果
       int putCount = 0;
       for(int i = from; i < to; i++) {
           String line = lines.get(i);
           String[] split = line.split(",");		//用字符串数组split存储分隔的line

           addRow(ret,putCount++,split);		//把split的Feature项（也就是split除去最后一列）由字符串转化为Double，并作为一行存入矩阵ret

           String outcome = split[split.length - 1];		//把split的良率项（最后一列）存入outcome
         //从数据读取outcome类型并添加到outcomes
           if(!outcomeTypes.contains(outcome))
               outcomeTypes.add(outcome);
           double[] rowOutcome = new double[15];
           rowOutcome[outcomeTypes.indexOf(outcome)] = 1;
           outcomes[i] = rowOutcome;
       }

       for(int i = 0; i < ret.rows(); i++)
           list.add(new DataSet(ret.getRow(i), Nd4j.create(outcomes[i])));		//用ret和outcomes构造Dataset对象(有2个INDArray成员)，用list存储这些Dataset

       return list;
   }

   private static void addRow(INDArray ret,int row,String[] line) {
       double[] vector = new double[210];
       for(int i = 0; i < 210; i++)
           vector[i] = Double.parseDouble(line[i]);

       ret.putRow(row,Nd4j.create(vector));
   }
}
