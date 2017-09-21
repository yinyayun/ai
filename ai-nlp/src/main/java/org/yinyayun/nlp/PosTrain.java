/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.nlp;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.yinyayun.nlp.common.NLPTrain;
import org.yinyayun.nlp.common.PropertiesLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * PosTrain.java 词性识别训练
 * 
 * @author yinyayun
 */
public class PosTrain extends NLPTrain {
    public static void main(String[] args) {
        PosTrain train = new PosTrain();
        train.start(train, args);
    }

    @Override
    public void train(PropertiesLoader propertiesLoader) throws Exception {
        POSModel model = null;
        try (InputStream dataIn = new FileInputStream(trainFile)) {
            ObjectStream<String> lineStream = new PlainTextByLineStream(() -> dataIn, StandardCharsets.UTF_8);
            ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);
            TrainingParameters parameters = TrainingParameters.defaultParams();
            parameters.put(TrainingParameters.THREADS_PARAM, threads);
            parameters.put(TrainingParameters.CUTOFF_PARAM, cutoff);
            parameters.put(TrainingParameters.ITERATIONS_PARAM, "200");
            // parameters.put(TrainingParameters.TRAINER_TYPE_PARAM, TrainerType.EVENT_MODEL_SEQUENCE_TRAINER);
            POSTaggerFactory factory = new POSTaggerFactory();
            // PosDictionary posDictionary = new PosDictionary();
            // posDictionary.loadDict();
            // factory.setTagDictionary(posDictionary);
            model = POSTaggerME.train("cn", sampleStream, parameters, factory);
        }
        if (model != null) {
            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelSavePath))) {
                model.serialize(modelOut);
            }
        }
    }
}
