package com.example.finalsimme;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class GetData {

    public GetData(){

    }

    public String getData(){

        String data = "";
        String finalJson = null;

        try {
            URL url = new URL("http://api.myjson.com/bins/1ckr7u");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finalJson = data;

        return finalJson;
    }

    public String getDataSOAP(String METHOD_NAME) {

        SoapPrimitive resultString;

        String finalJson = null;

        String SOAP_ACTION = "http://elnmirsrv17/simmeweb/" + METHOD_NAME;
        String NAMESPACE = "http://elnmirsrv17/simmeweb";
        String URL = "http://elnmirsrv17/simmeweb/testeservice.asmx?WSDL";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            finalJson = resultString.toString();

            Log.d(TAG, "Requisição SOAP feita");

        } catch (HttpResponseException e) {
            e.printStackTrace();
            Log.d(TAG, "HttpResponseException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d(TAG, "XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }

        return finalJson;
    }

    public String getDataSOAPEquipamento(String METHOD_NAME, String idBanco, String idInstalacao){

        SoapPrimitive resultString;

        String finalJson = null;

        String SOAP_ACTION = "http://elnmirsrv17/simmeweb/" + METHOD_NAME;
        String NAMESPACE = "http://elnmirsrv17/simmeweb";
        String URL = "http://elnmirsrv17/simmeweb/testeservice.asmx?WSDL";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("idBanco", idBanco);
            Request.addProperty("idInstalacao", idInstalacao);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            finalJson = resultString.toString();

            Log.d(TAG, "Requisição SOAP feita");

        } catch (HttpResponseException e) {
            e.printStackTrace();
            Log.d(TAG, "HttpResponseException");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d(TAG, "XmlPullParserException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }

        return finalJson;

    }

    public String getDataSOAPPonto(String METHOD_NAME, String idBanco, String idEquipamento){

        SoapPrimitive resultString;

        String finalJson = null;
        String erro = "erro";

        String SOAP_ACTION = "http://elnmirsrv17/simmeweb/" + METHOD_NAME;
        String NAMESPACE = "http://elnmirsrv17/simmeweb";
        String URL = "http://elnmirsrv17/simmeweb/testeservice.asmx?WSDL";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("idBanco", idBanco);
            Request.addProperty("idEquipamento", idEquipamento);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            finalJson = resultString.toString();

            Log.d(TAG, "Requisição SOAP feita");
            return finalJson;

        } catch (HttpResponseException e) {
            e.printStackTrace();
            Log.d(TAG, "HttpResponseException");
            return erro;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d(TAG, "XmlPullParserException");
            return erro;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
            return erro;
        }

    }

    public String getDataURL(){

        String data = "";
        String finalJson = null;

        try {
            URL url = new URL("http://api.myjson.com/bins/1ckr7u");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finalJson = data;

        return finalJson;

    }

}
