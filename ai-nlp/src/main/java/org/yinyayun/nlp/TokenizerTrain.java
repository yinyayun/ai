/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.nlp;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.yinyayun.nlp.common.NLPTrain;
import org.yinyayun.nlp.common.PropertiesLoader;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * TokenizerTrain.java 分词训练
 * 
 * @author yinyayun
 */
public class TokenizerTrain extends NLPTrain {

    public static void main(String[] args) {
        TokenizerTrain tokenizerTrain = new TokenizerTrain();
        tokenizerTrain.start(tokenizerTrain, args);
    }

    @Override
    public void train(PropertiesLoader propertiesLoader) throws Exception {
        Charset charset = Charset.forName("UTF-8");
        ObjectStream<String> lineStream = new PlainTextByLineStream(() -> new FileInputStream(trainFile), charset);
        TokenizerModel model;
        try (ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream)) {
            TrainingParameters trainingParameters = TrainingParameters.defaultParams();
            trainingParameters.put(TrainingParameters.THREADS_PARAM, threads);
            trainingParameters.put(TrainingParameters.CUTOFF_PARAM, cutoff);
            // trainingParameters.put(AbstractEventTrainer.DATA_INDEXER_PARAM,
            // AbstractEventTrainer.DATA_INDEXER_ONE_PASS_REAL_VALUE);
            model = TokenizerME.train(sampleStream, new TokenizerFactory("cn", null, false, null), trainingParameters);
        }
        if (model != null) {
            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelSavePath))) {
                model.serialize(modelOut);
            }
        }
    }
}
