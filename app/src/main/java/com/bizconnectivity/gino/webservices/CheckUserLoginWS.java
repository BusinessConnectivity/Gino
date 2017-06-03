package com.bizconnectivity.gino.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.gino.ConstantWS.*;

public class CheckUserLoginWS {

    public static boolean invokeCheckUserLogin(String email, String password) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_CHECK_USER_LOGIN);


        PropertyInfo emailPI = new PropertyInfo();
        // Set Name
        emailPI.setName("email");
        // Set Value
        emailPI.setValue(email);
        // Set dataType
        emailPI.setType(String.class);
        // Add the property to request object
        request.addProperty(emailPI);

        PropertyInfo passwordPI = new PropertyInfo();
        // Set Name
        passwordPI.setName("password");
        // Set Value
        passwordPI.setValue(password);
        // Set dataType
        passwordPI.setType(String.class);
        // Add the property to request object
        request.addProperty(passwordPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CHECK_USER_LOGIN, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCheckUserLogin: " + envelope.bodyIn.toString());

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                Log.d("TAG", "invokeCheckUserLogin: " + responseProperty.toString());

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
