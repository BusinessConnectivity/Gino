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

public class CreateUserWS {

    public static boolean invokeCreateUser(String name, String password, String email, String gender, String dob, String facebookId,
                                           String googleId, String photoUrl) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_CREATE_USER);

        PropertyInfo namePI = new PropertyInfo();
        // Set Name
        namePI.setName("name");
        // Set Value
        namePI.setValue(name);
        // Set dataType
        namePI.setType(String.class);
        // Add the property to request object
        request.addProperty(namePI);

        PropertyInfo passwordPI = new PropertyInfo();
        // Set Name
        passwordPI.setName("password");
        // Set Value
        passwordPI.setValue(password);
        // Set dataType
        passwordPI.setType(String.class);
        // Add the property to request object
        request.addProperty(passwordPI);

        PropertyInfo emailPI = new PropertyInfo();
        // Set Name
        emailPI.setName("email");
        // Set Value
        emailPI.setValue(email);
        // Set dataType
        emailPI.setType(String.class);
        // Add the property to request object
        request.addProperty(emailPI);

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

        PropertyInfo facebookIdPI = new PropertyInfo();
        // Set Name
        facebookIdPI.setName("facebookId");
        // Set Value
        facebookIdPI.setValue(facebookId);
        // Set dataType
        facebookIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(facebookIdPI);

        PropertyInfo googleIdPI = new PropertyInfo();
        // Set Name
        googleIdPI.setName("googleId");
        // Set Value
        googleIdPI.setValue(googleId);
        // Set dataType
        googleIdPI.setType(String.class);
        // Add the property to request object
        request.addProperty(googleIdPI);

        PropertyInfo photoUrlPI = new PropertyInfo();
        // Set Name
        photoUrlPI.setName("photoUrl");
        // Set Value
        photoUrlPI.setValue(photoUrl);
        // Set dataType
        photoUrlPI.setType(String.class);
        // Add the property to request object
        request.addProperty(photoUrlPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + WS_CREATE_USER, envelope);

            if (envelope.bodyIn instanceof SoapFault) {

                Log.d("TAG", "invokeCreateUser: " + envelope.bodyIn.toString());

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
