package com.bizconnectivity.gino.webservices;

import android.util.Log;

import com.bizconnectivity.gino.models.Event;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.Common.dataReturn;
import static com.bizconnectivity.gino.Common.isValidateProperty;
import static com.bizconnectivity.gino.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.ConstantWS.URL;
import static com.bizconnectivity.gino.ConstantWS.WS_RETRIEVE_EVENT_BY_ID;

public class RetrieveEventByIdWS {

    public static Event invokeRetrieveEventById(int eventId) {

        Event event = new Event();

        SoapObject request = new SoapObject(NAMESPACE, WS_RETRIEVE_EVENT_BY_ID);

        //property which holds input parameters
        PropertyInfo eventIdPI = new PropertyInfo();
        //set name
        eventIdPI.setName("eventId");
        //set value
        eventIdPI.setValue(eventId);
        //set datatype
        eventIdPI.setType(int.class);
        //add the property to request object
        request.addProperty(eventIdPI);

        //create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //set output SOAP object
        envelope.setOutputSoapObject(request);
        //create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_RETRIEVE_EVENT_BY_ID, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeRetrieveEventById: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;

                //get the desired property
                SoapObject soapObject = (SoapObject) response.getProperty(0);

                //retrieve diffGram from property
                SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

                if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

                    //retrieve document element from diffGram
                    SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

                    //get the number of dataSet
                    SoapObject table = (SoapObject) newDataSet.getProperty(0);

                    if (isValidateProperty(table, "EventId")) {
                        event.setEventID(Integer.parseInt(dataReturn(table, "EventId")));
                    }

                    if (isValidateProperty(table, "EventName")) {
                        event.setEventName(dataReturn(table, "EventName"));
                    }

                    if (isValidateProperty(table, "EventDescription")) {
                        event.setEventDescription(dataReturn(table, "EventDescription"));
                    }

                    if (isValidateProperty(table, "EventStartDateTime")) {
                        event.setEventStartDateTime(dataReturn(table, "EventStartDateTime"));
                    }

                    if (isValidateProperty(table, "EventEndDateTime")) {
                        event.setEventEndDateTime(dataReturn(table, "EventEndDateTime"));
                    }

                    if (isValidateProperty(table, "EventLocation")) {
                        event.setEventLocation(dataReturn(table, "EventLocation"));
                    }

                    if (isValidateProperty(table, "EventOrganizer")) {
                        event.setEventOrganizer(dataReturn(table, "EventOrganizer"));
                    }

                    if (isValidateProperty(table, "EventLatitude")) {
                        event.setEventLatitude(dataReturn(table, "EventLatitude"));
                    }

                    if (isValidateProperty(table, "EventLongitude")) {
                        event.setEventLongitude(dataReturn(table, "EventLongitude"));
                    }

                    if (isValidateProperty(table, "EventUrl")) {
                        event.setEventURL(dataReturn(table, "EventUrl"));
                    }

                    if (isValidateProperty(table, "ImageUrl")) {
                        event.setImageUrl(dataReturn(table, "ImageUrl"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
}
