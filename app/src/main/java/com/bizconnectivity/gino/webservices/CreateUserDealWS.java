package com.bizconnectivity.gino.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.ConstantWS.NAMESPACE;
import static com.bizconnectivity.gino.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.gino.ConstantWS.URL;
import static com.bizconnectivity.gino.ConstantWS.WS_CREATE_USER_DEAL;

public class CreateUserDealWS {

    public static boolean invokeCreateUserDeal(int memberId, int dealId, int quantity, String totalPrice, String subTotalPrice) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_CREATE_USER_DEAL);

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

        PropertyInfo quantityPI = new PropertyInfo();
        // Set Name
        quantityPI.setName("quantity");
        // Set Value
        quantityPI.setValue(quantity);
        // Set dataType
        quantityPI.setType(int.class);
        // Add the property to request object
        request.addProperty(quantityPI);

        PropertyInfo totalPI = new PropertyInfo();
        // Set Name
        totalPI.setName("totalPrice");
        // Set Value
        totalPI.setValue(totalPrice);
        // Set dataType
        totalPI.setType(String.class);
        // Add the property to request object
        request.addProperty(totalPI);

        PropertyInfo subtotalPI = new PropertyInfo();
        // Set Name
        subtotalPI.setName("subTotalPrice");
        // Set Value
        subtotalPI.setValue(subTotalPrice);
        // Set dataType
        subtotalPI.setType(String.class);
        // Add the property to request object
        request.addProperty(subtotalPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CREATE_USER_DEAL, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCreateUserDeal: " + envelope.bodyIn.toString());

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
