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
import static com.bizconnectivity.gino.ConstantWS.WS_CHECK_USER;

public class CheckUserWS {

    public static boolean invokeCheckUser(String email) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_CHECK_USER);

        PropertyInfo emailPI = new PropertyInfo();
        // Set Name
        emailPI.setName("email");
        // Set Value
        emailPI.setValue(email);
        // Set dataType
        emailPI.setType(String.class);
        // Add the property to request object
        request.addProperty(emailPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CHECK_USER, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCheckUser: " + envelope.bodyIn.toString());

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
