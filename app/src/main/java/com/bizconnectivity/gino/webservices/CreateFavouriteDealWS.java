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
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_CREATE_FAVOURITE_DEAL;

public class CreateFavouriteDealWS {

    public static boolean invokeCreateFavouriteDeal(int memberId, int dealId) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_CREATE_FAVOURITE_DEAL);

        PropertyInfo memberIdPI = new PropertyInfo();
        // Set Name
        memberIdPI.setName("memberId");
        // Set Value
        memberIdPI.setValue(memberId);
        // Set dataType
        memberIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(memberIdPI);

        PropertyInfo dealIdPI = new PropertyInfo();
        // Set Name
        dealIdPI.setName("dealId");
        // Set Value
        dealIdPI.setValue(dealId);
        // Set dataType
        dealIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(dealIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CREATE_FAVOURITE_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCreateFavouriteDeal: " + envelope.bodyIn.toString());

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
