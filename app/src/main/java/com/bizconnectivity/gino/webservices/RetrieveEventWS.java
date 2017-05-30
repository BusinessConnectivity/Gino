package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.EventModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_RETRIEVE_EVENT;

public class RetrieveEventWS {

    public static List<EventModel> invokeRetrieveNearbyEvent(String latitude, String longitutde, String kilometer) {

        List<EventModel> eventModelList = new ArrayList<>();

        //create request
        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_EVENT);

        //property which holds input parameters
        PropertyInfo latitudePI = new PropertyInfo();
        //set name
        latitudePI.setName("latitude");
        //set value
        latitudePI.setValue(latitude);
        //set datatype
        latitudePI.setType(String.class);
        //add the property to request object
        request.addProperty(latitudePI);

        //property which holds input parameters
        PropertyInfo longitutdePI = new PropertyInfo();
        //set name
        longitutdePI.setName("longitude");
        //set value
        longitutdePI.setValue(longitutde);
        //set datatype
        longitutdePI.setType(String.class);
        //add the property to request object
        request.addProperty(longitutdePI);

        //property which holds input parameters
        PropertyInfo kilometerPI = new PropertyInfo();
        //set name
        kilometerPI.setName("kilometer");
        //set value
        kilometerPI.setValue(kilometer);
        //set datatype
        kilometerPI.setType(String.class);
        //add the property to request object
        request.addProperty(kilometerPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_EVENT, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveNearbyEvent: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;

                //get the desired property
                SoapObject soapObject = (SoapObject) response.getProperty(0);

                //retrieve diffGram from property
                SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

                if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

                    //retrieve document element from diffGram
                    SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

                    for (int i=0; i<newDataSet.getPropertyCount(); i++) {

                        //get the number of dataSet
                        SoapObject table = (SoapObject) newDataSet.getProperty(i);

                        EventModel eventModel = new EventModel();

                        if (isValidateProperty(table, "EventId")) {
                            eventModel.setEventID(Integer.parseInt(dataReturn(table, "EventId")));
                        }

                        if (isValidateProperty(table, "EventName")) {
                            eventModel.setEventName(dataReturn(table, "EventName"));
                        }

                        if (isValidateProperty(table, "EventDescription")) {
                            eventModel.setEventDescription(dataReturn(table, "EventDescription"));
                        }

                        if (isValidateProperty(table, "EventStartDateTime")) {
                            eventModel.setEventStartDateTime(dataReturn(table, "EventStartDateTime"));
                        }

                        if (isValidateProperty(table, "EventEndDateTime")) {
                            eventModel.setEventEndDateTime(dataReturn(table, "EventEndDateTime"));
                        }

                        if (isValidateProperty(table, "EventLocation")) {
                            eventModel.setEventLocation(dataReturn(table, "EventLocation"));
                        }

                        if (isValidateProperty(table, "EventOrganizer")) {
                            eventModel.setEventOrganizer(dataReturn(table, "EventOrganizer"));
                        }

                        if (isValidateProperty(table, "EventLatitude")) {
                            eventModel.setEventLatitude(dataReturn(table, "EventLatitude"));
                        }

                        if (isValidateProperty(table, "EventLongitude")) {
                            eventModel.setEventLongitude(dataReturn(table, "EventLongitude"));
                        }

                        if (isValidateProperty(table, "EventUrl")) {
                            eventModel.setEventURL(dataReturn(table, "EventUrl"));
                        }

                        if (isValidateProperty(table, "ImageUrl")) {
                            eventModel.setImageUrl(dataReturn(table, "ImageUrl"));
                        }

                        eventModelList.add(eventModel);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventModelList;
    }
}
