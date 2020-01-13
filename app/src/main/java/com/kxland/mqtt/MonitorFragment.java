package com.kxland.mqtt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.ChartHelper;


public class MonitorFragment extends Fragment {
    private MqttAndroidClient client;
    private String TAG = "MonitorActivity";
    private PahoMqttClient pahoMqttClient;

    ChartHelper mChartEC;
    LineChart chartEC;

    ChartHelper mChartAirMix;
    LineChart chartAirMix;

    ChartHelper mChartPH;
    LineChart chartPH;

    ChartHelper mChartSuhu;
    LineChart chartSuhu;

    ChartHelper mChartPupukA;
    LineChart chartPupukA;

    ChartHelper mChartPupukB;
    LineChart chartPupukB;

    private OnFragmentInteractionListener mListener;

    public MonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monitor, container, false);

        chartEC = (LineChart) view.findViewById(R.id.chartEC);
        chartEC.getDescription().setText("Ec");
        mChartEC = new ChartHelper(chartEC);


        chartPH = (LineChart) view.findViewById(R.id.chartPH);
        chartPH.getDescription().setText("pH");
        mChartPH = new ChartHelper(chartPH);

        chartAirMix = (LineChart) view.findViewById(R.id.chartAirMix);
        chartAirMix.getDescription().setText("Level Mix");
        mChartAirMix = new ChartHelper(chartAirMix);

        chartSuhu = (LineChart) view.findViewById(R.id.chartSuhu);
        chartSuhu.getDescription().setText("Suhu");
        mChartSuhu = new ChartHelper(chartSuhu);

        chartPupukA = (LineChart) view.findViewById(R.id.chartPupukA);
        chartPupukA.getDescription().setText("Level Pupuk A");
        mChartPupukA = new ChartHelper(chartPupukA);

        chartPupukB = (LineChart) view.findViewById(R.id.chartPupukB);
        chartPupukB.getDescription().setText("Level Pupuk B");
        mChartPupukB = new ChartHelper(chartPupukB);

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

                if(s.equals("hidroponik4/nilaiEc"))mChartEC.addEntry(Float.valueOf(mqttMessage.toString()));
                if(s.equals("hidroponik4/tinggiAirMix"))mChartAirMix.addEntry(Float.valueOf(mqttMessage.toString()));
                if(s.equals("hidroponik4/nilaiPH"))mChartPH.addEntry(Float.valueOf(mqttMessage.toString()));
                if(s.equals("hidroponik4/nilaiSuhu"))mChartSuhu.addEntry(Float.valueOf(mqttMessage.toString()));
                if(s.equals("hidroponik4/tinggipupukA"))mChartPupukA.addEntry(Float.valueOf(mqttMessage.toString()));
                if(s.equals("hidroponik4/tinggipupukB"))mChartPupukB.addEntry(Float.valueOf(mqttMessage.toString()));


                //Log.d("dddd", "messageArrived: "+ s.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_monitor, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Monitor");
        }
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
