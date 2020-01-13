package com.kxland.mqtt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.ChartHelper;


public class Monitor2Fragment extends Fragment {
    private MqttAndroidClient client;
    private String TAG = "Monitor2Activity";
    private PahoMqttClient pahoMqttClient;

    TextView textEc, textPpm, textPh, textSuhu, textAirMix, textPupukA, textPupukB;


    private OnFragmentInteractionListener mListener;

    public Monitor2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor2, container, false);

        textEc = view.findViewById(R.id.textViewEc);
        textPpm = view.findViewById(R.id.textViewPpm);
        textPh = view.findViewById(R.id.textViewPh);
        textSuhu = view.findViewById(R.id.textViewSuhu);
        textAirMix = view.findViewById(R.id.textViewLevelAirMix);
        textPupukA = view.findViewById(R.id.textViewLevelPupukA);
        textPupukB = view.findViewById(R.id.textViewLevelPupukB);

        pahoMqttClient = new PahoMqttClient();

        //client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);
        client = pahoMqttClient.getMqttClient(getContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                try {
                    pahoMqttClient.subscribe(client, "hidroponik4/phdown", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/phdup", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/tinggiAirMix", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/tinggipupukA", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/tinggipupukB", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/nilaiEc", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/nilaiPH", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/nilaiSuhu", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/statusMix", 1);
                    pahoMqttClient.subscribe(client, "hidroponik4/statusCheck", 1);


                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //TODO: do chart

                if(s.equals("hidroponik4/nilaiEc")){
                    textEc.setText(mqttMessage.toString());
                    textPpm.setText(String.valueOf(Float.valueOf(textEc.getText().toString()) * 500));
                }
                if(s.equals("hidroponik4/nilaiSuhu"))textSuhu.setText(mqttMessage.toString()+ "\u00B0" +"C");
                if(s.equals("hidroponik4/tinggiAirMix"))textAirMix.setText(mqttMessage.toString() + " %");
                if(s.equals("hidroponik4/tinggipupukA"))textPupukA.setText(mqttMessage.toString() + " %");
                if(s.equals("hidroponik4/tinggipupukB"))textPupukB.setText(mqttMessage.toString() + " %");
                if(s.equals("hidroponik4/nilaiPH"))textPh.setText(mqttMessage.toString());


                //Log.d("dddd", "messageArrived: "+ s.toString()+ ""+ mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }
}
