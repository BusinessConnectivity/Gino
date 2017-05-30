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
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_UPDATE_USER_DEAL;

public class UpdateUserDealPurchaseWS {

    public static boolean invokeUpdateUserDealPurchase(int memberDealId, int quantity) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_UPDATE_USER_DEAL);

        PropertyInfo memberDealIdPI = new PropertyInfo();
        // Set Name
        memberDealIdPI.setName("memberDealId");
        // Set Value
        memberDealIdPI.setValue(memberDealId);
        // Set dataType
        memberDealIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(memberDealIdPI);

        PropertyInfo quantityPI = new PropertyInfo();
        // Set Name
        quantityPI.setName("quantity");
        // Set Value
        quantityPI.setValue(quantity);
        // Set dataType
        quantityPI.setType(int.class);
        // Add the property to request object
        request.addProperty(quantityPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_UPDATE_USER_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeUpdateUserDealPurchase: " + envelope.bodyIn.toString());

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
