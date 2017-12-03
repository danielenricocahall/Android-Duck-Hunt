package com.example.danie.ppd_final_project;

import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

/*import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;*/

public class MainActivity extends Activity {

    protected static final int NUM_SAMPLES = 4;
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        gameView = new GameView(this, point);

        setContentView(gameView);
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                createAndUseNetwork();
            }
        });*/
    }

   /* protected void createAndUseNetwork()
    {
        DenseLayer inputLayer = new DenseLayer.Builder()
                .nIn(2)
                .nOut(3)
                .name("Input")
                .build();

        DenseLayer hiddenLayer = new DenseLayer.Builder()
                .nIn(3)
                .nOut(2)
                .name("Hidden")
                .build();

        OutputLayer outputLayer = new OutputLayer.Builder()
                .nIn(2)
                .nOut(1)
                .name("Output")
                .build();
        NeuralNetConfiguration.Builder nncBuilder = new NeuralNetConfiguration.Builder();
        nncBuilder.iterations(10000);
        nncBuilder.learningRate(0.01);
        NeuralNetConfiguration.ListBuilder listBuilder = nncBuilder.list();
        listBuilder.layer(0, inputLayer);
        listBuilder.layer(1, hiddenLayer);
        listBuilder.layer(2, outputLayer);
        listBuilder.backprop(true);
        MultiLayerNetwork myNetwork = new MultiLayerNetwork(listBuilder.build());
        myNetwork.init();
        INDArray trainingInputs = Nd4j.zeros(NUM_SAMPLES, inputLayer.getNIn());
        INDArray trainingOutputs = Nd4j.zeros(NUM_SAMPLES, outputLayer.getNOut());
        // If 0,0 show 0
        trainingInputs.putScalar(new int[]{0,0}, 0);
        trainingInputs.putScalar(new int[]{0,1}, 0);
        trainingOutputs.putScalar(new int[]{0,0}, 0);

// If 0,1 show 1
        trainingInputs.putScalar(new int[]{1,0}, 0);
        trainingInputs.putScalar(new int[]{1,1}, 1);
        trainingOutputs.putScalar(new int[]{1,0}, 1);

// If 1,0 show 1
        trainingInputs.putScalar(new int[]{2,0}, 1);
        trainingInputs.putScalar(new int[]{2,1}, 0);
        trainingOutputs.putScalar(new int[]{2,0}, 1);

// If 1,1 show 0
        trainingInputs.putScalar(new int[]{3,0}, 1);
        trainingInputs.putScalar(new int[]{3,1}, 1);
        trainingOutputs.putScalar(new int[]{3,0}, 0);
        DataSet myData = new DataSet(trainingInputs, trainingOutputs);
        myNetwork.fit(myData);


    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
