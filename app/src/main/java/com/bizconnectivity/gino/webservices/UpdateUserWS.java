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
import static com.bizconnectivity.gino.webservices.ConstantWS.WS_UPDATE_USER;

public class UpdateUserWS {

    public static boolean invokeUpdateUser(String email, String name, String gender, String dob) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_UPDATE_USER);

        //property which holds input parameters
        PropertyInfo emailPI = new PropertyInfo();
        //set name
        emailPI.setName("email");
        //set value
        emailPI.setValue(email);
        //set datatype
        emailPI.setType(String.class);
        //add the property to request object
        request.addProperty(emailPI);

        PropertyInfo namePI = new PropertyInfo();
        // Set Name
        namePI.setName("name");
        // Set Value
        namePI.setValue(name);
        // Set dataType
        namePI.setType(String.class);
        // Add the property to request object
        request.addProperty(namePI);

        PropertyInfo genderPI = new PropertyInfo();
        // Set Name
        genderPI.setName("gender");
        // Set Value
        genderPI.setValue(gender);
        // Set dataType
        genderPI.setType(String.class);
        // Add the property to request object
        request.addProperty(genderPI);

        PropertyInfo dobPI = new PropertyInfo();
        // Set Name
        dobPI.setName("dob");
        // Set Value
        dobPI.setValue(dob);
        // Set dataType
        dobPI.setType(String.class);
        // Add the property to request object
        request.addProperty(dobPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_UPDATE_USER, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeUpdateUser: " + envelope.bodyIn.toString());

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
