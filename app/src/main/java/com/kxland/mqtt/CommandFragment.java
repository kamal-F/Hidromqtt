package com.kxland.mqtt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommandFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommandFragment extends Fragment {

    private MqttAndroidClient client;
    private String TAG = "Main3Activity";
    private PahoMqttClient pahoMqttClient;

    private EditText textMessage, textTopic, subscribeTopic, unSubscribeTopic;
    private Button publishMessage, subscribe, unSubscribe;
    private  Button buttonStartService, buttonStopService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CommandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommandFragment newInstance(String param1, String param2) {
        CommandFragment fragment = new CommandFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_command, container, false);


        pahoMqttClient = new PahoMqttClient();

        textTopic = view.findViewById(R.id.textTopic);
        textMessage = (EditText) view.findViewById(R.id.textMessage);
        publishMessage = (Button) view.findViewById(R.id.publishMessage);

        subscribe = (Button) view.findViewById(R.id.subscribe);
        unSubscribe = (Button) view.findViewById(R.id.unSubscribe);

        buttonStartService = view.findViewById(R.id.buttonServiceOn);
        buttonStopService = view.findViewById(R.id.buttonServiceOff);

        subscribeTopic = (EditText) view.findViewById(R.id.subscribeTopic);
        unSubscribeTopic = (EditText) view.findViewById(R.id.unSubscribeTopic);
        client = pahoMqttClient.getMqttClient(getContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

        publishMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tpc = textTopic.getText().toString().trim();
                String msg = textMessage.getText().toString().trim();
                if (!msg.isEmpty() && !tpc.isEmpty()) {
                    try {
                        //pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC_PH_UP);
                        pahoMqttClient.publishMessage(client, msg, 1, tpc);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = subscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        pahoMqttClient.subscribe(client, topic, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        unSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = unSubscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        pahoMqttClient.unSubscribe(client, topic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(),MqttMessageService.class));
            }
        });

        buttonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getActivity(),MqttMessageService.class));

            }
        });

        //Intent intent = new Intent(getContext(), MqttMessageService.class);
        //startService(intent);
//        getActivity().startService(new Intent(getActivity(),MqttMessageService.class));
//        getActivity().stopService(new Intent(getActivity(),MqttMessageService.class));
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Command");
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
