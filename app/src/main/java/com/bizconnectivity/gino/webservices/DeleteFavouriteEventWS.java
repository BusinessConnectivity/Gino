package com.bizconnectivity.gino.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.webservices.ConstantWS.URL;
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_DELETE_FAVOURITE_EVENT;

public class DeleteFavouriteEventWS {

    public static boolean invokeDeleteFavouriteEvent(int memberFavEventId) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_DELETE_FAVOURITE_EVENT);

        PropertyInfo memberFavEventIdPI = new PropertyInfo();
        // Set Name
        memberFavEventIdPI.setName("memberFavEventId");
        // Set Value
        memberFavEventIdPI.setValue(memberFavEventId);
        // Set dataType
        memberFavEventIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(memberFavEventIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_DELETE_FAVOURITE_EVENT, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeDeleteFavouriteEvent: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                if (Boolean.parseBoolean(responseProperty.toString())) {

                    returnResult = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }
}
