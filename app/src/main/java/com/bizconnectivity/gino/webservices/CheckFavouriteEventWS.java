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
import static com.bizconnectivity.gino.ConstantWS.WS_CHECK_FAVOURITE_EVENT;

public class CheckFavouriteEventWS {

    public static Event invokeCheckFavouriteEvent(int memberId, int eventId) {

        Event event = new Event();

        SoapObject request = new SoapObject(NAMESPACE, WS_CHECK_FAVOURITE_EVENT);

        PropertyInfo memberIdPI = new PropertyInfo();
        // Set Name
        memberIdPI.setName("memberId");
        // Set Value
        memberIdPI.setValue(memberId);
        // Set dataType
        memberIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(memberIdPI);

        PropertyInfo eventIdPI = new PropertyInfo();
        // Set Name
        eventIdPI.setName("eventId");
        // Set Value
        eventIdPI.setValue(eventId);
        // Set dataType
        eventIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(eventIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CHECK_FAVOURITE_EVENT, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCheckFavouriteEvent: " + envelope.bodyIn.toString());

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

                    if (isValidateProperty(table, "MemberFavEventId")) {
                        event.setUserFavEventId(Integer.parseInt(dataReturn(table, "MemberFavEventId")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
}
